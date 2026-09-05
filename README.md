# TripSentinel

TripSentinel is a privacy-first safety check-in platform designed for solo travelers, hikers, and independent people.

The project focuses on planned-return verification rather than live tracking. Users can create a trip, define an expected return time, and notify trusted contacts if they fail to check in within a configured grace period.

The long-term goal is to provide a lightweight, privacy-focused alternative to traditional safety and location-sharing applications, with particular attention to users who may not have an established support network.

## Current Status

🚧 Backend foundation under active development.

The Spring Boot backend currently includes:

* Users and roles
* Responder availability
* Check-in sessions and check-in methods
* Session events
* REST controllers, services, repositories, DTOs, and validation
* MariaDB schema management with Flyway
* Unit, repository, controller, and integration tests

Authentication, authorization, frontend development, notifications, scheduling, and deployment remain to be implemented.

## Technology Stack

### Frontend

* React
* Vite
* TypeScript
* Progressive Web App (PWA)

### Backend

* Java 25
* Spring Boot

### Database

* MariaDB (planned)

### Infrastructure

* Docker
* GitHub Actions
* Azure (planned deployment)

## Planned MVP

* User accounts
* Trusted contacts
* Trip creation
* Planned return times
* Scheduled check-ins
* Email notifications
* Escalation workflow
* Interactive maps

## Project Philosophy

TripSentinel is **not** an emergency service, live tracking platform, or rescue system.

Its purpose is simple:

> If someone doesn't return as planned, someone should notice.
