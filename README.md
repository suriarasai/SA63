# SA63 - Enterprise Web Application Development with Java & Spring
Spring and Angular Full Stack Development - A hands-on, 7-day intensive course on building robust, scalable, and production-ready web applications using modern Java, Spring Boot, and cloud-ready architectural patterns.

---

## Course Overview

This course guides experienced Java developers through the lifecycle of full-stack, enterprise-grade web development. 

Using an **iterative, data-first build methodology**, you will architect an application from the storage tier up to modern UI and RESTful service boundaries:
* **Iteration 1 (Days 1–4):** Monolithic foundations, Object-Relational Mapping (JPA/Hibernate), MVC Controllers, and classic Web UI.
* **Iteration 2 (Days 4–7):** RESTful APIs, Service Layer Architecture, Transaction Management, Postman API Testing, Pagination, and Cloud-Native Configuration.

---

## Prerequisites & Environment Setup

* **Java Development Kit (JDK):** Version 21 LTS
* **IDE:** Spring Tool Suite (STS) / Eclipse with Spring Tools plugin (or IntelliJ IDEA)
* **Build Tool:** Apache Maven 3.8+
* **Database:** MySQL Server 8.x and MySQL Workbench
* **API Testing:** Postman Desktop App

---

## 7-Day Curriculum Breakdown

| Day | Focus Area | Core Competencies & Milestones |
|---|---|---|
| **Day 1** | **Foundations & Single-Table JPA** | Development setup (STS, Maven), MySQL connectivity, Spring Boot intro, JPA Entity mapping, MySQL Workbench verification. |
| **Day 2** | **Relational ORM & Unit Testing** | Complex relationships (`@OneToMany`, `@ManyToMany`), JPQL/Derived queries, ER vs. OR mental models, JPA Unit Testing. |
| **Day 3** | **Spring MVC & HTTP Mechanics** | HTTP verbs, URL routing science, Spring Controllers, Request mappings, Forward vs. Redirect flows, Classic Web UI. |
| **Day 4** | **Iteration 1 Capstone & REST Intro** | **Morning:** Iteration 1 full CRUD user story completion.<br>**Afternoon:** REST principles, JSON serialization, `@RestController`. |
| **Day 5** | **Service Layer & Enterprise Scopes** | Service layer abstraction, Interface-driven design, Declarative `@Transactional` scopes, Auditing, Session management. |
| **Day 6** | **Pagination, Postman & Config** | Server-side pagination & sorting (`Pageable`), Postman automated testing, Externalized configuration/Spring Cloud Config, Advanced UI workflows. |
| **Day 7** | **Iteration 2 Capstone & Case Studies** | **Morning:** Iteration 2 feature expansion & integration.<br>**Afternoon:** Advanced enterprise case studies and reference architectures. |

---

## Daily Module Details

### Day 1: System Architecture, Spring & Single-Table JPA
* **Two-Iteration Architecture Roadmap:** Understanding how the monolithic MVP evolves into a service-oriented application.
* **Tooling Setup:** Installing and configuring STS (Eclipse), verifying JDK environments, and dissecting Maven `pom.xml` dependencies.
* **Database Layer:** Connecting Spring to MySQL Server via MySQL Workbench.
* **Data-First ORM:** Defining the first `@Entity`, mapping fields to columns, and validating database schema generation via Workbench.

---

### Day 2: Relational Mapping (ORM vs. ER) & JPA Testing
* **Relational Mapping:** Navigating `@OneToOne`, `@OneToMany`, `@ManyToOne`, and `@ManyToMany` associations.
* **Mindset Shift:** Bridging Entity-Relationship (ER) tabular thinking with Object-Relational (OR) graph modeling.
* **JPA Repositories & JPQL:** Custom finder methods, derived queries, and native SQL vs. JPQL.
* **JPA Unit Testing:** Writing clean repository unit tests using `@DataJpaTest` and assertion libraries.

---

### Day 3: HTTP Protocol, Spring MVC & Web UI
* **HTTP & Routing Science:** Deep dive into request methods (`GET`, `POST`, `PUT`, `DELETE`), headers, query parameters, and path variables.
* **Controller Routing:** Building `@Controller` handlers, resolving view templates, and handling `Model` data binding.
* **Navigation Flow:** Implementing Controller Forward vs. Redirect (`redirect:/...`) patterns to prevent duplicate form submissions (PRG Pattern).
* **CRUD Integration:** Wiring initial HTML form submissions to database mutations.

---

### Day 4: Iteration 1 Capstone & REST API Transition
* **Morning (Iteration 1 Milestone):** Complete an end-to-end user story integrating the UI, Controller, and JPA layers into a working monolithic CRUD app.
* **Afternoon (RESTful Paradigm):** 
  * Shifting from server-side rendering to stateless APIs.
  * `@RestController`, `@ResponseBody`, and HTTP status codes (`200`, `201`, `400`, `404`, `500`).
  * Request validation using `@Valid` and handling JSON payloads.

---

### Day 5: Service Layer, Transaction Boundaries & Auditing
* **Service Layer Abstraction:** Decoupling business rules from controllers using interfaces and `@Service` implementations.
* **Transaction Management:** Demystifying `@Transactional`, rollback policies, ACID guarantees, and propagation levels.
* **Audit Logging & Security Hooks:** Implementing entity lifecycle listeners (`@PrePersist`, `@PreUpdate`) for enterprise audit trails.
* **Session vs. Stateless Management:** Managing user contexts and temporary state across multiple requests.

---

### Day 6: Pagination, Advanced Postman Testing & Configuration
* **Data Pagination & Sorting:** Efficient queries with Spring Data `Pageable`, `Page<T>`, and slice limits to prevent out-of-memory errors on large datasets.
* **API Verification with Postman:** Building test collections, managing environment variables, and writing automated status/JSON contract assertions.
* **Configuration Management:** Centralizing application properties, profile-based configuration (`dev`, `staging`, `prod`), and Spring Cloud Config concepts.
* **Enhanced UI Handling:** Dynamic data rendering with pagination controls and filter panels.

---

### Day 7: Iteration 2 Capstone & Enterprise Architectures
* **Morning (Iteration 2 Milestone):** Deliver advanced, production-style user stories incorporating paginated REST APIs, transactional workflows, and input validations.
* **Afternoon (Case Studies & Best Practices):**
  * Walkthrough of real-world enterprise architectures (caching strategies, cloud database readiness, microservice migration pathways).
  * Reference repositories and code patterns for independent exploration.

---

## Repository Structure

```text
├── day-01-jpa-starter/           # Single-table mapping and workbench scripts
├── day-02-relational-jpa/         # Relational entities and unit tests
├── day-03-spring-mvc/             # Web controllers, routing, and UI views
├── day-04-iteration-1-capstone/   # Monolith CRUD capstone + REST intro
├── day-05-service-transactions/   # Service interfaces, transactions, and audit logs
├── day-06-pagination-config/      # Pageable APIs, Postman collection files, and config profiles
├── day-07-iteration-2-final/      # Final enterprise application code
└── case-studies/                  # Reference architectures and advanced samples
