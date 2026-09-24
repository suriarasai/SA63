-- ============================================================
--  Choppee – schema.sql
--  H2 compatible DDL
--  All tables dropped and recreated on startup.
-- ============================================================

-- Users (String, LocalDate, LocalDateTime, Boolean)
DROP TABLE IF EXISTS product_tags;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS purchase_orders;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- ── users ────────────────────────────────────────────────────────────────────
CREATE TABLE users (
    id         BIGINT      AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(120) NOT NULL UNIQUE,
    password   VARCHAR(120) NOT NULL,
    first_name VARCHAR(60),
    last_name  VARCHAR(60),
    birth_date DATE,                       -- java.time.LocalDate
    created_at TIMESTAMP,                  -- java.time.LocalDateTime
    active     BOOLEAN      DEFAULT TRUE
);

-- ── categories ───────────────────────────────────────────────────────────────
CREATE TABLE categories (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(80)  NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- ── tags ─────────────────────────────────────────────────────────────────────
CREATE TABLE tags (
    id   BIGINT      AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- ── products (ManyToOne → categories) ────────────────────────────────────────
CREATE TABLE products (
    id               BIGINT        AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(120)  NOT NULL,
    description      VARCHAR(1000),
    brand            VARCHAR(80),
    price            DOUBLE        NOT NULL,              -- Double
    discount_percent DOUBLE        DEFAULT 0.0,           -- Double
    stock_quantity   INT           DEFAULT 0,             -- Integer
    image_url        VARCHAR(255),
    created_at       TIMESTAMP,                           -- LocalDateTime
    active           BOOLEAN       DEFAULT TRUE,
    category_id      BIGINT        NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- ── product_tags (ManyToMany join table) ─────────────────────────────────────
CREATE TABLE product_tags (
    product_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (product_id, tag_id),
    CONSTRAINT fk_pt_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_pt_tag     FOREIGN KEY (tag_id)     REFERENCES tags(id)
);

-- ── carts (OneToOne → users) ─────────────────────────────────────────────────
CREATE TABLE carts (
    id         BIGINT    AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT    NOT NULL UNIQUE,                -- OneToOne FK
    created_at TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ── cart_items (ManyToOne → carts, ManyToOne → products) ─────────────────────
CREATE TABLE cart_items (
    id         BIGINT    AUTO_INCREMENT PRIMARY KEY,
    cart_id    BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,
    quantity   INT       NOT NULL DEFAULT 1,             -- Integer
    added_at   TIMESTAMP,                                -- LocalDateTime
    CONSTRAINT fk_ci_cart    FOREIGN KEY (cart_id)    REFERENCES carts(id),
    CONSTRAINT fk_ci_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ── purchase_orders (ManyToOne → users) ──────────────────────────────────────
CREATE TABLE purchase_orders (
    id               BIGINT        AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT        NOT NULL,
    total_amount     DOUBLE        NOT NULL,             -- Double
    shipping_address VARCHAR(500),
    notes            VARCHAR(300),
    status           VARCHAR(20)   NOT NULL DEFAULT 'PENDING',  -- Enum as String
    order_date       TIMESTAMP     NOT NULL,             -- LocalDateTime
    delivered_at     TIMESTAMP,                          -- LocalDateTime (nullable)
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ── order_items (ManyToOne → purchase_orders, ManyToOne → products) ──────────
CREATE TABLE order_items (
    id         BIGINT  AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT  NOT NULL,
    product_id BIGINT  NOT NULL,
    quantity   INT     NOT NULL,                         -- Integer
    unit_price DOUBLE  NOT NULL,                         -- Double (price snapshot)
    CONSTRAINT fk_oi_order   FOREIGN KEY (order_id)   REFERENCES purchase_orders(id),
    CONSTRAINT fk_oi_product FOREIGN KEY (product_id) REFERENCES products(id)
);
