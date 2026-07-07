# Technical Decisions

This document records the major technical decisions made throughout the project and the reasoning behind them.

---

## 2026-07-06

### Project

**Repository Name:** TripSentinel

**Reasoning**

* Descriptive and memorable.
* Suitable for a portfolio project.
* Flexible enough to expand beyond hiking into planned-return safety.

---

### Version Control

**Platform:** GitHub

**Repository:** Public

**Reasoning**

* Portfolio visibility.
* Version history.
* Easy collaboration.
* CI/CD integration later.

---

### Project Structure

```text
TripSentinel/
├── backend/
├── frontend/
├── docs/
├── docker-compose.yml
└── README.md
```

**Reasoning**

* Clear separation of frontend and backend.
* Documentation separated from source code.
* Infrastructure managed from the project root.

---

### Backend

**Framework:** Spring Boot 4.1

**Build Tool:** Maven

**Java:** Oracle JDK 25 LTS

**Packaging:** JAR

**Reasoning**

* Current stable technology stack.
* Long-term support for Java.
* Self-contained deployment.
* Familiar and well-supported ecosystem.

---

### Database

**Database:** MariaDB 11.8.6

**Deployment:** Docker Compose

**Reasoning**

* Mature relational SQL database.
* Excellent Spring Boot compatibility.
* Reproducible local development.
* No local database installation required.

---

### Database Migrations

**Tool:** Flyway

**Reasoning**

* Database schema managed through version-controlled SQL migrations.
* Schema changes are never performed automatically by Hibernate.

---

### Database Design

**Approach**

* Database-first design.
* Schema created manually before JPA entities.
* Normalized relational model.
* Flyway migration defines the initial schema.

**Key Decisions**

* Single `users` table.
* Many-to-many relationship between users and roles (`user_roles`).
* Users may have multiple roles (e.g. CUSTOMER and RESPONDER).
* Session-specific data separated from user data.
* Availability stored independently from sessions.
* Session history recorded through `session_events`.
* Database optimized with foreign key indexes from the initial migration.

**Reasoning**

* Supports future expansion without major schema changes.
* Keeps responsibilities separated.
* Simplifies authentication while allowing multiple roles.
* Preserves an audit trail of session activity.
* Improves query performance on common lookups.

---

### JPA Configuration

**Hibernate**

* `ddl-auto=validate`
* `open-in-view=false`

**Reasoning**

* Flyway is the single source of truth for schema changes.
* Hibernate validates the schema instead of modifying it.
* Encourages a clean service-layer architecture.

---

### Application Configuration

**Configuration Format:** YAML

**Files**

* `application.yml`
* `application-docker.yml`

**Reasoning**

* Separate local and Docker environments.
* Easier to extend with additional profiles later.
* YAML provides better readability for hierarchical configuration.

---

### Documentation

**Location:** `/docs`

**Reasoning**

* Record architectural decisions.
* Document project evolution.
* Keep documentation separate from source code.

---

### Development Philosophy

* Build incrementally.
* Keep the MVP small.
* Avoid premature optimization.
* Infrastructure as code.
* Record technical decisions as they are implemented.
* Treat the project as if developed within a professional team.
