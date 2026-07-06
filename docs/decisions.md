# Technical Decisions

This document records the major technical decisions made throughout the project and the reasoning behind them.

---

## 2026-07-06

### Project

**Repository Name:** TripSentinel

**Reasoning**

* Descriptive and memorable.
* Clearly communicates the project's purpose.
* Suitable for a portfolio project.
* Flexible enough to evolve beyond hiking into general planned-return safety.

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
* Root-level Docker Compose for infrastructure.

---

### Java

**Version:** Oracle JDK 25 LTS

**Reasoning**

* Current Long-Term Support release.
* Appropriate for a new Spring Boot project.
* Long support lifecycle.
* Stable foundation for the project.

---

### Database

**Database:** MariaDB 11.8.6

**Reasoning**

* Mature SQL database.
* Excellent Spring Boot compatibility.
* Well suited for relational application data.
* Familiar technology.

---

### Containerization

**Tool:** Docker Compose

**Current Service:** MariaDB

**Reasoning**

* Infrastructure defined as code.
* Reproducible development environment.
* No local database installation required.
* Easy onboarding for future contributors.

---

### Database Migrations

**Tool:** Flyway (planned)

**Reasoning**

* Version-controlled database schema.
* Repeatable migrations.
* Consistent database state across environments.
* Integrates well with Spring Boot.

---

### Frontend

**Framework:** React

**Build Tool:** Vite

**Language:** TypeScript

**Reasoning**

* Existing experience.
* Modern tooling.
* Strong ecosystem.
* Suitable for Progressive Web Applications.

---

### Documentation

**Location:** `/docs`

**Reasoning**

* Keeps documentation separate from source code.
* Easy to expand with architecture, API, deployment, and database documentation.

---

### Development Philosophy

* Build the project incrementally.
* Keep the MVP small.
* Avoid premature optimization.
* Prioritize reproducibility.
* Record important technical decisions.
* Treat development as if working within a professional team.
