# Technical Decisions

This document records the major technical and architectural decisions made during the development of TripSentinel.

It is intended to explain **why the system is designed the way it is**, rather than serve as a development log or task list.

---

# 2026-07-06

## Project Structure

**Decision**

Use a single repository containing separate backend, frontend, documentation, and infrastructure areas.

```text
TripSentinel/
├── backend/
├── frontend/
├── docs/
├── docker-compose.yml
└── README.md
```

**Reasoning**

The frontend and backend are separate applications but form one product. Keeping them in the same repository simplifies development, documentation, versioning, and future CI/CD while still maintaining a clear separation between application layers.

---

## Backend Technology

**Decision**

Use:

* Spring Boot 4.1
* Maven
* Oracle JDK 25 LTS
* JAR packaging

**Reasoning**

Spring Boot provides the REST, validation, persistence, testing, and security infrastructure required by the application without requiring custom framework development.

Java 25 provides a current LTS runtime, while Maven gives the project a conventional and reproducible dependency and build structure.

JAR packaging keeps deployment self-contained and does not require an external application server.

---

## Relational Database

**Decision**

Use MariaDB 11.8.6 as the application database and run it through Docker Compose during development.

**Reasoning**

TripSentinel's core data is strongly relational:

* Users have roles.
* Responders have availability.
* Sessions belong to customers and responders.
* Sessions support multiple check-in methods.
* Sessions have historical events.

A relational database naturally represents these relationships and provides foreign keys, uniqueness constraints, transactions, and indexing.

Docker Compose provides a reproducible database environment without tying development to a manually configured local installation.

---

## Database-First Schema Management

**Decision**

The database schema is explicitly defined through SQL migrations rather than generated from JPA entities.

Flyway is the single authority for schema changes.

Hibernate is configured with:

```yaml
ddl-auto: validate
open-in-view: false
```

**Reasoning**

Allowing Hibernate to generate or modify the schema would make database changes implicit and harder to review.

Flyway makes every schema change:

* Explicit.
* Version controlled.
* Reproducible.
* Reviewable.
* Deployable consistently across environments.

`ddl-auto=validate` still allows Hibernate to detect mismatches between the Java model and the database without modifying the schema.

Disabling Open Session in View also prevents persistence concerns from leaking into the web layer and encourages required data to be handled deliberately inside the service layer.

---

## Single User Model

**Decision**

Store administrators, customers, and responders in one `users` table rather than separate tables for each type.

Roles are represented separately through:

```text
users
  ↓
user_roles
  ↓
roles
```

A user may have multiple roles.

**Reasoning**

ADMIN, CUSTOMER, and RESPONDER are application responsibilities rather than fundamentally different types of people.

Separate user tables would duplicate common information such as:

* Name.
* Email.
* Password.
* Phone number.
* Account status.

The many-to-many role model also allows combinations such as a responder who is simultaneously a customer without creating duplicate accounts.

This structure additionally provides a natural foundation for later authorization.

---

## User Deactivation Instead of Deletion

**Decision**

Users have an `ACTIVE` / `INACTIVE` status. Normal application workflows deactivate users rather than physically deleting them.

**Reasoning**

Users can be referenced by historical sessions, availability records, and events.

Deleting those users could either break historical relationships or require cascading deletion of information that should be retained.

Deactivation preserves historical integrity while preventing the account from remaining operational.

---

## Separate Responder Availability

**Decision**

Responder availability is stored separately from users and check-in sessions.

**Reasoning**

Availability is time-dependent operational data rather than a permanent property of a responder.

A responder may have many different availability windows, and those windows may exist without being assigned to a session.

Keeping availability separate allows scheduling and session assignment to evolve independently.

---

## Session-Specific Information

**Decision**

Information relevant to a particular safety period belongs to the check-in session rather than the user's permanent profile.

This includes information such as:

* Location description.
* Important notes.
* Medical information relevant to that session.
* Expected return/check-in times.

**Reasoning**

A user's circumstances can change between sessions.

Storing this information on the user would risk using outdated information and would incorrectly treat temporary safety context as permanent profile data.

---

## Session Status and Event History

**Decision**

Maintain both:

1. A current status on `check_in_sessions`.
2. A separate `session_events` history.

**Reasoning**

The two structures solve different problems.

The session status answers:

> What state is this session in now?

The event history answers:

> What happened during this session?

Reading the current state should not require reconstructing it from an event stream. At the same time, storing only the current status would discard useful historical information.

This provides simple operational queries while preserving an audit trail.

---

## Database Indexes

**Decision**

Create indexes for important foreign-key relationships as part of the initial database design.

**Reasoning**

The application frequently retrieves records through relationships such as:

* Sessions by customer.
* Sessions by responder.
* Events by session.
* Availability by responder.

These relationships are known parts of the domain rather than speculative optimization, so indexing them from the beginning avoids predictable query-performance problems.

---

# 2026-08-22

## Layered Backend Architecture

**Decision**

Use the following application flow:

```text
Controller
    ↓
Service
    ↓
Mapper
    ↓
Repository
    ↓
Database
```

with clear responsibilities for each component.

### Controller

Responsible for:

* HTTP endpoints.
* Request DTOs.
* Response DTOs.
* HTTP-level interaction.

### Service

Responsible for:

* Business rules.
* Entity lookup.
* Relationship resolution.
* Coordinating repository operations.

### Mapper

Responsible for:

* Entity → DTO conversion.
* DTO → entity field mapping.

### Repository

Responsible for:

* Persistence.
* Database queries.

**Reasoning**

Separating these responsibilities prevents HTTP concerns, persistence concerns, and business logic from becoming mixed.

In particular, relationship resolution belongs in services because it requires repository access and business knowledge.

Mappers should not become hidden service classes, and controllers should not contain database logic.

The structure also makes each layer easier to test independently.

---

## Constructor Injection

**Decision**

Use constructor injection for backend dependencies.

**Reasoning**

Constructor injection makes required dependencies explicit and allows classes to be instantiated easily during testing.

It also prevents dependencies from being changed after construction and avoids hidden field injection performed by the framework.

---

## DTOs as the API Boundary

**Decision**

JPA entities are not exposed directly through the REST API.

Request and response DTOs define the external API.

Different operations may use different DTOs even when they affect the same entity.

**Reasoning**

Persistence entities represent how data is stored. DTOs represent what a client is allowed or required to send and receive.

Using separate DTOs prevents the database model from accidentally becoming the public API contract.

It also allows operations to expose different fields.

For example, an administrator updating a user and a customer updating their own profile should not necessarily have access to the same fields.

---

## Database-Backed Check-In Methods

**Decision**

Represent check-in methods as reference data rather than storing them directly as an enum field on the session.

Sessions and methods have a many-to-many relationship through `session_check_in_methods`.

The domain supports:

* PHONE
* SMS
* EMAIL
* APP

with PHONE currently seeded.

**Reasoning**

A session may support more than one check-in method.

Representing the relationship explicitly avoids embedding a collection into a single database column and keeps the schema normalized.

It also allows additional methods to be introduced as reference data without redesigning the session table.

---

## Explicit Session Lifecycle

**Decision**

Represent the current session lifecycle through defined statuses:

* `PLANNED`
* `ACTIVE`
* `CHECKED_IN`
* `MISSED`
* `ESCALATED`
* `CANCELLED`

**Reasoning**

A safety session has meaningful states with different operational consequences.

Representing them explicitly avoids relying on combinations of timestamps or nullable fields to infer the current state.

This makes both application logic and future authorization rules easier to reason about.

---

## Cancellation Instead of Session Deletion

**Decision**

A cancelled session remains in the database with status `CANCELLED`.

**Reasoning**

A created safety session is part of the application's history even when it never completes.

Deleting it would remove information that may be relevant to:

* Audit history.
* Customer history.
* Responder activity.
* Troubleshooting.

Cancellation therefore represents a state transition rather than deletion.

---

## No GPS or Live Tracking in the MVP

**Decision**

TripSentinel does not continuously track the customer's location.

Sessions contain a textual location description such as:

```text
Nordmarka south
```

**Reasoning**

TripSentinel is designed around planned-return verification rather than live surveillance.

Continuous GPS tracking would introduce:

* Additional privacy concerns.
* More sensitive stored data.
* Mobile-device requirements.
* Battery and connectivity considerations.
* Mapping dependencies.
* Considerably more implementation complexity.

None of these are required to validate the core check-in concept.

Location tracking can therefore remain a future capability rather than part of the MVP.

---

# 2026-08-25

## Business Logic Belongs in Services

**Decision**

Rules requiring application state or relationships are handled by services rather than controllers, repositories, or mappers.

Examples include:

* Resolving role IDs.
* Resolving check-in methods.
* Finding referenced users.
* Assigning relationships.
* Updating existing entities.
* Checking whether referenced resources exist.

**Reasoning**

Repositories should answer persistence questions, not determine business behavior.

Controllers should translate HTTP interaction, not coordinate database relationships.

Mappers should translate data, not query the database.

Centralizing these responsibilities in services keeps business behavior reusable regardless of how the application is accessed.

---

## Role Assignment Through Persisted Roles

**Decision**

Users reference persisted `Role` entities rather than storing independent role strings directly on each user.

**Reasoning**

Roles are shared reference data.

Using persisted roles:

* Avoids duplicated role definitions.
* Enforces valid relationships.
* Supports users with multiple roles.
* Provides a consistent model for future authorization.

---

## Separate Administrative and User Operations

**Decision**

Administrative user operations and customer self-service operations use different service methods and DTOs.

**Reasoning**

The two actors have different authority.

An administrator may need to modify information such as:

* Roles.
* Account status.
* Other administrative properties.

A customer modifying their own profile should not automatically receive those capabilities.

Keeping the operations separate makes those boundaries explicit before authentication and authorization are introduced.

---

## Partial and Full Updates Are Distinct Operations

**Decision**

Administrative PATCH behavior and full update behavior are handled separately.

A partial update changes only values supplied by the request, while a full update represents the complete editable state expected by that operation.

**Reasoning**

Treating both operations identically can accidentally erase existing values when fields are omitted.

Explicitly separating their semantics makes update behavior predictable and provides a clearer REST API contract.

---

# 2026-08-27

## Integration Tests Use MariaDB

**Decision**

Integration tests run against a dedicated MariaDB database rather than replacing MariaDB with an in-memory database such as H2.

**Reasoning**

The database itself is part of what the integration tests need to verify.

Using MariaDB means the tests exercise:

* Actual SQL behavior.
* Flyway migrations.
* Database constraints.
* JPA mappings.
* Relationships.
* MariaDB-specific behavior.

An in-memory replacement could allow tests to pass even though the real database behaves differently.

Using the production database technology therefore provides stronger confidence in the backend integration.

---

## Dedicated Integration-Test Database

**Decision**

Use a separate database:

```text
trip_sentinel_test
```

with its own Spring `test` profile.

**Reasoning**

Integration tests perform real persistence operations and therefore must not run against development data.

A separate database provides isolation while still using exactly the same database technology and Flyway migrations.

---

## Transactional Integration Tests

**Decision**

Integration tests run inside transactions that are rolled back after each test.

**Reasoning**

Integration tests need real persistence behavior without permanently modifying the test environment.

Transactional rollback provides:

* Test isolation.
* Repeatable execution.
* Minimal cleanup logic.
* Protection against one test influencing another.

The test can still inspect persisted state before the transaction is rolled back.

---

## Verify Both API and Persistence Behavior

**Decision**

Integration tests may verify an operation through both its HTTP response and direct repository inspection.

**Reasoning**

An HTTP response alone proves that the API returned the expected representation, but it does not necessarily prove that the expected database state was created.

Checking both sides verifies the complete behavior:

```text
HTTP request
      ↓
API result
      ↓
Persistence result
```

This is particularly useful for create and update operations.

---

# 2026-08-31

## Integration Tests Are Organized by Domain

**Decision**

Keep integration tests grouped around the application's domains rather than grouping them by HTTP method or technical layer.

Examples:

* `UserIntegrationTest`
* `ResponderAvailabilityIntegrationTest`
* `CheckInSessionIntegrationTest`
* `SessionEventIntegrationTest`
* `RoleIntegrationTest`
* `CheckInMethodIntegrationTest`

**Reasoning**

The purpose of an integration test is to verify application behavior across layers.

Organizing tests around domains keeps related behavior together and mirrors the structure developers use when reasoning about the application.

It also allows successful and unsuccessful behavior for the same API to remain in the same test class.

---

## Establish Successful Flows Before Failure Coverage

**Decision**

Complete the primary successful integration flows before expanding the suite into detailed failure handling.

**Reasoning**

A failing error-handling test is difficult to interpret if the underlying successful operation has not already been proven.

Establishing the successful flows first confirms that:

* Routing works.
* Serialization works.
* Services work.
* Mappers work.
* Persistence works.
* Flyway and JPA agree on the schema.

Failure behavior can then be tested against a known working baseline.

---

# 2026-09-01

## Centralized API Error Handling

**Decision**

Expected application failures are represented through domain exceptions and translated into HTTP responses by a central `GlobalExceptionHandler`.

The primary exception categories are:

* `ResourceNotFoundException`
* `BadRequestException`
* `ConflictException`

**Reasoning**

Without centralized handling, individual controllers would need to translate service failures into HTTP responses independently.

That would duplicate logic and could cause different endpoints to represent the same failure differently.

Centralized handling creates one translation boundary between:

```text
Application/domain failure
            ↓
HTTP API representation
```

Services therefore describe **what went wrong**, while the exception handler determines **how that failure appears through HTTP**.

---

## Standard Error Response

**Decision**

API errors use a consistent response structure containing:

```text
status
error
message
timestamp
```

**Reasoning**

Frontend code should not need different error-parsing logic for every endpoint.

A common structure provides a predictable API contract while still allowing the `message` to explain the specific problem.

It also prevents framework-generated error formats from becoming an accidental part of the application's API.

---

## Separate Structural Validation From Business Validation

**Decision**

Use DTO validation for structural request requirements and service validation for rules requiring domain or database knowledge.

### DTO validation

Examples:

* Required fields.
* Email format.
* Minimum password length.
* Required dates.

### Service validation

Examples:

* Whether a referenced user exists.
* Whether a role exists.
* Whether a check-in method exists.
* Whether an email is already used.

**Reasoning**

These represent different kinds of invalid input.

DTO validation can determine whether the request has the correct structure without accessing application state.

Business validation requires knowledge of existing application data and therefore belongs in the service layer.

Keeping them separate prevents controllers and DTOs from becoming dependent on persistence.

---

## Duplicate Email Is a Conflict

**Decision**

Attempting to create or update a user with an email already assigned to another user produces `409 Conflict`.

The database unique constraint remains in place as the final integrity guarantee.

**Reasoning**

The database constraint protects data integrity but is not an ideal API contract.

Allowing the database exception to escape would expose persistence details and could result in a generic server error.

An explicit service check allows the API to communicate that:

* The request itself is valid.
* The requested state conflicts with an existing resource.

For updates, the current user's ID is excluded so that keeping their existing email remains valid.

---

## Invalid References Are Classified by Meaning

**Decision**

Do not treat every failed lookup identically.

Examples:

* Requesting a nonexistent resource → `404 Not Found`.
* Creating a session with an invalid check-in method reference → `400 Bad Request`.
* Using an email already assigned to another user → `409 Conflict`.

**Reasoning**

The same technical operation—a failed database lookup—can represent different API situations.

HTTP status codes should reflect the meaning of the failure from the client's perspective rather than the repository operation that detected it.

---

## Let Spring Reject Invalid Enum Path Values

**Decision**

Invalid enum values supplied directly as typed path parameters are rejected by Spring's request binding rather than manually validated in services.

For example:

```text
/api/roles/by-name/UNKNOWN
```

cannot be converted to the expected enum type and therefore results in `400 Bad Request`.

**Reasoning**

The value is outside the API's declared input type and the request cannot validly reach the service.

Adding service validation for the same condition would duplicate framework behavior.

The service remains responsible for the separate situation where the enum value itself is valid but corresponding persisted reference data cannot be found.

---

## Representative Failure Integration Tests

**Decision**

Integration testing verifies each meaningful failure mechanism, but does not attempt to test every possible invalid-input permutation through the full application stack.

**Reasoning**

The purpose of integration tests is to verify that components interact correctly.

For example, one representative missing-resource case can prove:

```text
Service
  ↓
ResourceNotFoundException
  ↓
GlobalExceptionHandler
  ↓
404 response
```

Repeating the identical mechanism for every possible field and endpoint would increase maintenance without proportionally increasing confidence.

More detailed validation permutations can be tested at narrower layers when they provide value.

---

## Authentication Is Deliberately Deferred

**Decision**

Do not implement full authentication and authorization before building the initial frontend.

Spring Security remains part of the intended architecture, but the backend stays accessible during this development phase and integration tests currently run without security filters.

**Alternatives Considered**

1. Implement authentication before frontend development.
2. Build a temporary/mock frontend role system.
3. Keep the API open during frontend development and add real security afterward.

**Selected:** Option 3.

**Reasoning**

TripSentinel has three important application perspectives:

* ADMIN
* CUSTOMER
* RESPONDER

During frontend development these views need to be opened, compared, tested, and changed frequently.

Introducing authentication now would require repeatedly switching accounts or roles while the actual user flows are still being designed.

A mock-role implementation would remove some of that friction but would itself be temporary code that later needs to be replaced.

Keeping the development API open provides the simplest environment for establishing the frontend workflows.

**Consequence**

Authentication and authorization are still mandatory before the application is considered complete.

Once the frontend workflows are established, security can be implemented around real application behavior rather than assumptions about how those workflows will operate.

---

## No Temporary Frontend Role Simulation

**Decision**

Do not build a mock ADMIN/CUSTOMER/RESPONDER authentication or role selector solely for frontend development.

**Reasoning**

The application already has a real multi-role user model.

A temporary simulation would duplicate part of the future authentication state without providing production value.

Direct access to the different frontend areas is sufficient while the backend is intentionally open.

Later, those views can be connected directly to the authenticated user's actual roles.

---

## Security Will Enforce Existing Domain Boundaries

**Decision**

Role-based authorization will be introduced after the frontend workflows are established rather than redesigning the domain around Spring Security.

**Reasoning**

The application already models ADMIN, CUSTOMER, and RESPONDER as domain roles.

Authentication should determine **who the user is**, and authorization should enforce **which existing application operations that user may perform**.

Security therefore becomes a protection layer around the established service/API design instead of becoming the structure that defines the application's business model.
