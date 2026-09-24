# 🔪 Choppee — Spring Boot Demo Application

> **A beginner-friendly e-commerce demo** for a premium knife and lumberjack-gear
> store.  Built with Spring Boot 3, Spring Data JPA, Thymeleaf, Spring Session,
> and an in-memory H2 database.

---

## Table of Contents

1. [What the App Does](#what-the-app-does)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Package Naming Convention](#package-naming-convention)
5. [Prerequisites](#prerequisites)
6. [How to Run the Application](#how-to-run-the-application)
7. [Demo Credentials](#demo-credentials)
8. [Accessing the H2 Database Console](#accessing-the-h2-database-console)
9. [Application URLs at a Glance](#application-urls-at-a-glance)
10. [Database Schema Overview](#database-schema-overview)
11. [JPA Associations Demonstrated](#jpa-associations-demonstrated)
12. [Transaction Management — Deep Dive](#transaction-management--deep-dive)
13. [Key Design Decisions](#key-design-decisions)
14. [Common Beginner Questions](#common-beginner-questions)
15. [Known Limitations & Production Gaps](#known-limitations--production-gaps)

---

## What the App Does

Choppee is a simplified online store where users can:

| Action | URL |
|--------|-----|
| Browse products & filter by category | `/products` |
| Search products by name or brand | `/products?q=chef` |
| View a product detail page | `/products/{id}` |
| Register for an account | `/register` |
| Log in / log out | `/login`, `/logout` |
| Add items to a shopping cart | `/cart/add` (POST) |
| View and modify the cart | `/cart` |
| Place an order (checkout) | `/checkout` → `/checkout/place` |
| View order history | `/orders` |
| Inspect a past order's details | `/orders/{id}` |

---

## Technology Stack

| Layer | Technology | Why |
|-------|-----------|-----|
| Language | Java 17 (LTS) | Long-term-support release; `record`, `var`, sealed types |
| Framework | Spring Boot 3.x | Auto-configuration, embedded Tomcat, starter POMs |
| Web Layer | Spring Web MVC | Controller → Service → Repository pattern |
| View Layer | Thymeleaf | Server-side HTML templating; natural HTML |
| Persistence | Spring Data JPA + Hibernate | ORM; avoids hand-writing SQL for entities |
| Database | H2 (in-memory) | Zero setup; schema reset on every restart |
| Sessions | Spring Session (in-memory) | Manages login state via `HttpSession` |
| Build Tool | Maven (`pom.xml`) | Dependency management & packaging |
| Stylesheet | Custom CSS (Google Fonts) | Clean, beginner-readable UI |

---

## Project Structure

```
choppee/
├── pom.xml                          ← Maven build config & dependencies
└── src/
    └── main/
        ├── java/
        │   └── sg/edu/choppee/      ← Root package
        │       ├── ChoppeeApplication.java       ← @SpringBootApplication entry point
        │       │
        │       ├── config/          ← Spring configuration classes
        │       │   ├── GlobalControllerAdvice.java  ← injects loggedInUser + cartItemCount
        │       │   │                                   into every Thymeleaf page
        │       │   ├── SessionConfig.java           ← Spring Session in-memory store
        │       │   └── WebConfig.java               ← MVC view controller shortcuts
        │       │
        │       ├── controller/      ← HTTP request handlers (the "C" in MVC)
        │       │   ├── HomeController.java
        │       │   ├── ProductController.java
        │       │   ├── CartController.java
        │       │   ├── OrderController.java
        │       │   └── UserController.java
        │       │
        │       ├── model/           ← JPA entity classes (mapped to DB tables)
        │       │   ├── User.java
        │       │   ├── Cart.java
        │       │   ├── CartItem.java
        │       │   ├── Product.java
        │       │   ├── Category.java
        │       │   ├── Tag.java
        │       │   ├── PurchaseOrder.java
        │       │   ├── OrderItem.java
        │       │   └── OrderStatus.java   ← enum: PENDING, CONFIRMED, SHIPPED …
        │       │
        │       ├── repository/      ← Spring Data JPA interfaces (auto-implemented)
        │       │   ├── UserRepository.java
        │       │   ├── CartRepository.java
        │       │   ├── CartItemRepository.java
        │       │   ├── ProductRepository.java
        │       │   ├── CategoryRepository.java
        │       │   ├── TagRepository.java
        │       │   ├── OrderRepository.java
        │       │   └── OrderItemRepository.java
        │       │
        │       └── service/         ← Business logic layer
        │           ├── UserService.java
        │           ├── CartService.java
        │           ├── ProductService.java
        │           └── OrderService.java
        │
        └── resources/
            ├── application.properties   ← datasource, JPA, server, logging config
            ├── schema.sql               ← DDL: CREATE TABLE statements (runs on startup)
            ├── data.sql                 ← DML: INSERT seed data (runs after schema.sql)
            │
            ├── static/
            │   └── css/
            │       └── style.css        ← stylesheet served at /css/style.css
            │
            └── templates/               ← Thymeleaf HTML templates
                ├── fragments/
                │   ├── header.html      ← navigation bar (included in every page)
                │   └── footer.html      ← footer links (included in every page)
                ├── index.html           ← home page
                ├── login.html
                ├── register.html
                ├── products.html
                ├── product-detail.html
                ├── cart.html
                ├── checkout.html
                ├── orders.html
                ├── order-detail.html
                └── order-success.html
```

---

## Package Naming Convention

All Java classes live under the root package **`sg.edu.choppee`**.

```
sg          ← country-code top-level domain (Singapore)
  .edu      ← educational institution sub-domain
    .choppee ← application / project name
```

Sub-packages follow the standard Spring layered architecture:

| Sub-package | Contents |
|-------------|----------|
| `sg.edu.choppee.config` | Spring `@Configuration` classes |
| `sg.edu.choppee.controller` | `@Controller` classes handling HTTP requests |
| `sg.edu.choppee.model` | JPA `@Entity` classes (mapped to DB tables) |
| `sg.edu.choppee.repository` | `JpaRepository` interfaces |
| `sg.edu.choppee.service` | `@Service` classes containing business logic |

---

## Prerequisites

You need the following installed on your computer:

| Tool | Minimum Version | Check with |
|------|-----------------|-----------|
| Java JDK | 17 | `java -version` |
| Apache Maven | 3.8 | `mvn -version` |
| Internet access | — | needed once to download Maven dependencies |

> **IDE (optional but recommended):** Spring Tool Suite 4 (STS4), IntelliJ IDEA,
> or VS Code with the Extension Pack for Java.

---

## How to Run the Application

### Option A — Using Maven on the command line

```bash
# 1. Open a terminal and navigate to the project root (where pom.xml lives)
cd path/to/choppee

# 2. Build and launch
mvn spring-boot:run
```

You should see log output ending in something like:

```
Started ChoppeeApplication in 3.4 seconds (process running for 3.8)
```

### Option B — Run the pre-built JAR

```bash
cd path/to/choppee
mvn package -DskipTests          # builds target/choppee-1.0.0.jar
java -jar target/choppee-1.0.0.jar
```

### Option C — From an IDE

1. Import as an **Existing Maven Project**.
2. Open `ChoppeeApplication.java`.
3. Right-click → **Run As → Spring Boot App** (STS/Eclipse), or
   click the green ▶ **Run** button (IntelliJ).

### Open the app in your browser

```
http://localhost:8080
```

---

## Demo Credentials

The `data.sql` file pre-loads three users:

| Username | Password | Notes |
|----------|----------|-------|
| `john_doe` | `password123` | Regular shopper |
| `jane_smith` | `secret456` | Regular shopper |
| `admin` | `admin` | Also a regular user (no admin UI in this demo) |

---

## Accessing the H2 Database Console

H2 ships with a web-based SQL console that lets you inspect the live database
while the application is running.

1. Visit: **http://localhost:8080/h2-console**
2. Fill in the connection form:
   - **JDBC URL:** `jdbc:h2:mem:choppeedb`
   - **User Name:** `sa`
   - **Password:** *(leave blank)*
3. Click **Connect**.

You can now run any SQL query, e.g.:

```sql
SELECT * FROM users;
SELECT * FROM products ORDER BY price DESC;
SELECT * FROM purchase_orders;
```

> **Note:** Because the database is **in-memory**, all data is wiped when the
> application stops.  `schema.sql` and `data.sql` are re-run automatically on
> every restart.

---

## Application URLs at a Glance

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/` | Home page (featured products + deals) |
| GET | `/products` | Browse all active products |
| GET | `/products?q=chef` | Search products by name / brand |
| GET | `/products?category=1` | Filter by category ID |
| GET | `/products/{id}` | Product detail page |
| GET | `/login` | Login form |
| POST | `/login` | Submit login credentials |
| GET | `/register` | Registration form |
| POST | `/register` | Submit new account |
| GET | `/logout` | Invalidates the session and redirects to login |
| GET | `/cart` | View your shopping cart |
| POST | `/cart/add` | Add a product to the cart |
| POST | `/cart/update` | Change a line-item's quantity |
| POST | `/cart/remove` | Remove a line-item from the cart |
| GET | `/checkout` | Checkout form (shipping address) |
| POST | `/checkout/place` | Place the order |
| GET | `/orders` | Order history |
| GET | `/orders/{id}` | Order detail |
| GET | `/orders/{id}/success` | Post-checkout confirmation page |
| GET | `/h2-console` | H2 database web console (dev only) |

---

## Database Schema Overview

```
users
  id (PK), username, email, password, first_name, last_name,
  birth_date, created_at, active

categories
  id (PK), name, description

tags
  id (PK), name

products
  id (PK), name, description, brand, price, discount_percent,
  stock_quantity, image_url, created_at, active,
  category_id (FK → categories.id)

product_tags  ← join table for ManyToMany
  product_id (FK → products.id), tag_id (FK → tags.id)

carts
  id (PK), created_at,
  user_id (FK → users.id, UNIQUE)    ← enforces OneToOne

cart_items
  id (PK), quantity, added_at,
  cart_id (FK → carts.id),
  product_id (FK → products.id)

purchase_orders
  id (PK), total_amount, shipping_address, notes, status,
  order_date, delivered_at,
  user_id (FK → users.id)

order_items
  id (PK), quantity, unit_price,
  order_id (FK → purchase_orders.id),
  product_id (FK → products.id)
```

---

## JPA Associations Demonstrated

This project intentionally demonstrates all four JPA association types:

### @OneToOne — User ↔ Cart
```java
// User.java (inverse side)
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
private Cart cart;

// Cart.java (owner side — holds the FK column user_id)
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", unique = true)
private User user;
```
One user has exactly one cart.  The `UNIQUE` constraint on `user_id` enforces this at the database level.

---

### @OneToMany / @ManyToOne — Cart ↔ CartItem
```java
// Cart.java (one side)
@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
private List<CartItem> items;

// CartItem.java (many side — holds the FK column cart_id)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cart_id", nullable = false)
private Cart cart;
```
One cart contains many cart items.  `orphanRemoval = true` means that if a `CartItem` is removed from the `items` list and the `Cart` is saved, JPA issues a `DELETE` statement automatically.

---

### @ManyToMany — Product ↔ Tag
```java
// Product.java (owner side — manages join table product_tags)
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
    name = "product_tags",
    joinColumns        = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
private Set<Tag> tags;

// Tag.java (inverse side)
@ManyToMany(mappedBy = "tags")
private Set<Product> products;
```
One product can have many tags; one tag can label many products.

---

## Transaction Management — Deep Dive

Spring's `@Transactional` annotation wraps a method in a database transaction.
If the method throws an exception, every SQL statement inside is rolled back.

### Key attributes explained

```java
@Transactional(
    propagation  = Propagation.REQUIRED,       // join existing tx or start new
    isolation    = Isolation.SERIALIZABLE,     // strictest: no dirty/phantom reads
    readOnly     = false,                      // we will write data
    rollbackFor  = Exception.class,            // rollback on any exception
    timeout      = 30                          // abort after 30 seconds
)
```

#### `propagation` — what to do when called from inside another transaction

| Value | Behaviour |
|-------|-----------|
| `REQUIRED` *(default)* | Join the existing transaction if one exists; start a new one if not. Most service methods use this. |
| `REQUIRES_NEW` | Always start a brand-new transaction; suspend the caller's transaction. |
| `SUPPORTS` | Use the existing transaction if one is present; run without one if not. Suitable for lightweight reads. |
| `MANDATORY` | Must be called from within an existing transaction; throws if not. |
| `NOT_SUPPORTED` | Run without a transaction, even if one is active. |
| `NEVER` | Must NOT be inside a transaction; throws if one is active. |
| `NESTED` | Creates a savepoint inside the current transaction. |

#### `isolation` — visibility of uncommitted data from OTHER transactions

| Level | Dirty Reads | Non-Repeatable Reads | Phantom Reads | Use when |
|-------|-------------|---------------------|---------------|----------|
| `READ_UNCOMMITTED` | ✅ allowed | ✅ allowed | ✅ allowed | Never in production |
| `READ_COMMITTED` | ❌ prevented | ✅ allowed | ✅ allowed | Most CRUD operations |
| `REPEATABLE_READ` | ❌ prevented | ❌ prevented | ✅ allowed | Registration / uniqueness checks |
| `SERIALIZABLE` | ❌ prevented | ❌ prevented | ❌ prevented | Stock deduction at checkout |

**Definitions:**
- **Dirty read** — reading data that another transaction has written but not yet committed (and might roll back).
- **Non-repeatable read** — running the same SELECT twice in one transaction returns different rows because another transaction committed an UPDATE in between.
- **Phantom read** — running the same range SELECT twice returns different rows because another transaction committed an INSERT in between.

#### `readOnly = true`
Tells Hibernate to skip the *dirty-checking* pass at transaction commit time.
Dirty-checking is how Hibernate detects which entities you have modified so it can
issue UPDATE statements.  If you know you are only reading data, setting
`readOnly = true` saves CPU and memory.

#### `rollbackFor`
By default Spring rolls back only on `RuntimeException` (unchecked) and `Error`.
If you want checked exceptions (e.g., `IOException`) to also trigger a rollback,
set `rollbackFor = Exception.class`.

---

### How the `placeOrder()` transaction works step-by-step

```
Controller             OrderService              CartService
    │                       │                        │
    │  placeOrder()         │                        │
    │──────────────────────►│                        │
    │                       │  BEGIN TX (SERIALIZABLE)
    │                       │                        │
    │                       │  1. load User          │
    │                       │  2. getCartForUser() ──►│ (joins the SAME tx)
    │                       │◄───────────────────────│
    │                       │  3. validate stock     │
    │                       │  4. build OrderItems   │
    │                       │  5. deduct stock       │
    │                       │  6. save order         │
    │                       │  7. clearCart() ───────►│ (joins the SAME tx)
    │                       │◄───────────────────────│
    │                       │                        │
    │                       │  COMMIT TX
    │◄──────────────────────│
    │  redirect to /success  │
```

If ANY step throws → the entire transaction rolls back:
- No order is saved
- No stock is deducted
- The cart is not cleared
- The user sees an error message and can try again

---

## Key Design Decisions

### Why `PurchaseOrder` instead of `Order`?
`ORDER` is a reserved keyword in SQL.  Naming the entity `Order` would require
quoting the table name in every query.  `PurchaseOrder` is clearer and avoids
the problem entirely.

### Why store `unit_price` in `OrderItem`?
Product prices can change over time.  If we only stored a reference to the
product, old orders would show the current price instead of what the customer
actually paid.  Taking a *price snapshot* at checkout preserves the correct
historical record.

### Why use `@Enumerated(EnumType.STRING)` for `OrderStatus`?
If we used the default `EnumType.ORDINAL`, the integer stored in the database
(`0`, `1`, `2` …) would change if we ever re-ordered the enum constants.
Using `STRING` stores `"PENDING"`, `"CONFIRMED"` etc., which is refactoring-safe.

### Why Spring Session instead of the default `HttpSession`?
Spring Session decouples session storage from the web container.  Swapping
`MapSessionRepository` (in-memory) for `RedisIndexedSessionRepository` (Redis)
requires changing only one bean in `SessionConfig.java` — no controller code
changes at all.

### Why is the cart created at registration time?
`UserService.register()` creates both the `User` and the `Cart` in a single
transaction.  This guarantees every active user always has a cart, so
`CartService` never has to handle a "missing cart" edge case for a valid user.

---

## Common Beginner Questions

**Q: The app starts but I see a "Whitelabel Error Page". What's wrong?**  
A: Make sure you are visiting `http://localhost:8080` (not https, not port 80).
Also check the console log for stack traces.

**Q: I changed a Thymeleaf template but the browser still shows the old page.**  
A: Thymeleaf cache is disabled in `application.properties` (`spring.thymeleaf.cache=false`).
Try a hard refresh (`Ctrl + Shift + R` / `Cmd + Shift + R`) or clear browser cache.

**Q: Where is the data stored? I want to see the database tables.**  
A: The H2 database lives entirely in RAM.  Use the H2 console at
`http://localhost:8080/h2-console` (see above).  All data is wiped on every restart.

**Q: How do I add a new product?**  
A: This demo has no admin UI.  Add a row directly in `data.sql` and restart, or
use the H2 console to execute an INSERT statement while the app is running.

**Q: What does `@Autowired` do?**  
A: It asks Spring's dependency injection container to find a bean of the
required type and inject it automatically.  You never call `new CartRepository()`
yourself — Spring handles instantiation.

**Q: What is the difference between `@Controller` and `@Service`?**  
A: `@Controller` handles HTTP requests and returns view names or redirect URLs.
`@Service` contains business logic and is called by controllers.  The separation
keeps controllers thin and business logic testable in isolation.

**Q: Why use `Optional<T>` in repository methods?**  
A: Repository methods like `findById()` might return `null` if the row doesn't
exist.  `Optional<T>` forces the caller to explicitly handle the "not found"
case (via `.orElseThrow()`, `.orElse()`, `.map()` etc.) rather than risk a
`NullPointerException` at runtime.

---

## Known Limitations & Production Gaps

This is a **teaching demo**, not a production system.  Before deploying to a
real environment you would need:

| Gap | Fix |
|-----|-----|
| Plain-text passwords | Use `BCryptPasswordEncoder` from Spring Security |
| No CSRF protection | Enable Spring Security's CSRF token support |
| In-memory H2 database | Replace with PostgreSQL / MySQL / MariaDB |
| In-memory sessions | Replace with Redis-backed `RedisIndexedSessionRepository` |
| No input sanitisation | Add Bean Validation (`@NotBlank`, `@Size`, etc.) to form models |
| No image hosting | Store product images in S3 / CDN |
| Single-threaded stock lock | Use database-level locking (`SELECT … FOR UPDATE`) or optimistic locking (`@Version`) |
| No email confirmation | Add registration verification email |
| No HTTPS | Configure TLS / terminate at a load balancer |

---

*Built for educational purposes — Spring Boot 3.x · Spring Data JPA · Thymeleaf · H2 · Java 17*
