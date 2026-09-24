-- ============================================================
--  Choppee – data.sql
--  Auto-populated seed data for development / demo.
-- ============================================================

-- ── Categories ───────────────────────────────────────────────────────────────
INSERT INTO categories (name, description) VALUES
  ('Chef Knives',    'High-precision kitchen blades for the professional cook'),
  ('Hunting Knives', 'Rugged fixed and folding blades for the outdoors'),
  ('Axes & Hatchets','Splitting mauls, felling axes and camping hatchets'),
  ('Protective Gear','Cut-resistant gloves, blade guards and storage solutions');

-- ── Tags ─────────────────────────────────────────────────────────────────────
INSERT INTO tags (name) VALUES
  ('Premium'),
  ('Handcrafted'),
  ('Professional'),
  ('Outdoor'),
  ('Stainless Steel'),
  ('Carbon Steel'),
  ('Limited Edition'),
  ('Beginner Friendly');

-- ── Products ─────────────────────────────────────────────────────────────────
-- ( name, description, brand, price, discount_percent, stock_qty, image_url, created_at, active, category_id )

INSERT INTO products (name, description, brand, price, discount_percent, stock_quantity, image_url, created_at, active, category_id) VALUES
(
  'Gyuto Pro 210mm',
  'A legendary all-purpose Japanese chef knife. Full-tang VG-10 steel, hand-sharpened to 15° edge. Pakkawood handle for supreme balance.',
  'SharpEdge Co.',
  249.99, 0.0, 25,
  'https://placehold.co/600x400/1a1a2e/FFFFFF?text=Gyuto+Pro+210mm',
  NOW(), TRUE, 1
),
(
  'Damascus Santoku 165mm',
  '67-layer Damascus steel with hammered tsuchime finish. Exceptional food release. Hand-forged in Japan.',
  'BladeForge Japan',
  379.00, 10.0, 12,
  'https://placehold.co/600x400/16213e/FFFFFF?text=Damascus+Santoku',
  NOW(), TRUE, 1
),
(
  'Nakiri Vegetable Knife',
  'Double-bevel blade, straight spine ideal for push-cuts through vegetables. Lightweight, 165mm AUS-10 steel.',
  'SharpEdge Co.',
  149.95, 0.0, 30,
  'https://placehold.co/600x400/0f3460/FFFFFF?text=Nakiri+Knife',
  NOW(), TRUE, 1
),
(
  'Bushcraft Fixed Blade',
  'Full-tang 1095 high-carbon steel. Scandinavian grind for woodworking and field dressing. Leather sheath included.',
  'WildBlade USA',
  189.99, 5.0, 18,
  'https://placehold.co/600x400/2d4a22/FFFFFF?text=Bushcraft+Blade',
  NOW(), TRUE, 2
),
(
  'Folding Hunter Tanto',
  'Liner-lock mechanism, S30V steel blade, G-10 handle. Compact enough for a belt pouch, tough enough for the deep woods.',
  'WildBlade USA',
  124.50, 0.0, 40,
  'https://placehold.co/600x400/4a3728/FFFFFF?text=Folding+Tanto',
  NOW(), TRUE, 2
),
(
  'Felling Axe 3.5 lb',
  'Drop-forged 1055 carbon steel head, hickory handle. Competition-grade bit geometry. Comes with a full leather mask.',
  'NorthWood Tools',
  219.00, 15.0, 8,
  'https://placehold.co/600x400/3d2b1f/FFFFFF?text=Felling+Axe',
  NOW(), TRUE, 3
),
(
  'Splitting Maul 6 lb',
  'Wedge-shaped head designed to split large rounds in a single swing. Fibreglass handle absorbs shock.',
  'NorthWood Tools',
  175.00, 0.0, 15,
  'https://placehold.co/600x400/5c3d2e/FFFFFF?text=Splitting+Maul',
  NOW(), TRUE, 3
),
(
  'Camp Hatchet 1.5 lb',
  'Lightweight competition hatchet. Ideal for kindling and tent stakes. Polished head, engraved logo.',
  'NorthWood Tools',
  89.95, 20.0, 22,
  'https://placehold.co/600x400/2c4a3e/FFFFFF?text=Camp+Hatchet',
  NOW(), TRUE, 3
),
(
  'Cut-Resistant Gloves (L/XL)',
  'ANSI A9 rated HPPE/steel-fibre gloves. Ambidextrous. Machine washable. Essential for safe blade handling.',
  'SafeGrip',
  49.99, 0.0, 60,
  'https://placehold.co/600x400/1c1c2e/FFFFFF?text=Cut+Gloves',
  NOW(), TRUE, 4
),
(
  'Magnetic Knife Strip 50cm',
  'Solid walnut board with N52 neodymium magnets. Holds up to 10 knives. Easy wall-mount hardware included.',
  'KitchenOrder',
  64.00, 0.0, 35,
  'https://placehold.co/600x400/3b2f2f/FFFFFF?text=Knife+Strip',
  NOW(), TRUE, 4
);

-- ── Product ↔ Tag associations (ManyToMany) ───────────────────────────────────
-- Gyuto Pro 210mm → Premium, Professional, Stainless Steel
INSERT INTO product_tags (product_id, tag_id) VALUES (1,1),(1,3),(1,5);
-- Damascus Santoku → Premium, Handcrafted, Carbon Steel, Limited Edition
INSERT INTO product_tags (product_id, tag_id) VALUES (2,1),(2,2),(2,6),(2,7);
-- Nakiri → Professional, Stainless Steel, Beginner Friendly
INSERT INTO product_tags (product_id, tag_id) VALUES (3,3),(3,5),(3,8);
-- Bushcraft → Outdoor, Carbon Steel, Handcrafted
INSERT INTO product_tags (product_id, tag_id) VALUES (4,4),(4,6),(4,2);
-- Folding Hunter → Outdoor, Premium
INSERT INTO product_tags (product_id, tag_id) VALUES (5,4),(5,1);
-- Felling Axe → Outdoor, Premium, Handcrafted
INSERT INTO product_tags (product_id, tag_id) VALUES (6,4),(6,1),(6,2);
-- Splitting Maul → Outdoor, Beginner Friendly
INSERT INTO product_tags (product_id, tag_id) VALUES (7,4),(7,8);
-- Camp Hatchet → Outdoor, Limited Edition
INSERT INTO product_tags (product_id, tag_id) VALUES (8,4),(8,7);
-- Cut-Resistant Gloves → Professional, Beginner Friendly
INSERT INTO product_tags (product_id, tag_id) VALUES (9,3),(9,8);
-- Knife Strip → Premium, Handcrafted
INSERT INTO product_tags (product_id, tag_id) VALUES (10,1),(10,2);

-- ── Demo Users ────────────────────────────────────────────────────────────────
-- Passwords are plain-text for demo. Use BCrypt in production.
INSERT INTO users (username, email, password, first_name, last_name, birth_date, created_at, active) VALUES
  ('john_doe',  'john@example.com',  'password123', 'John',  'Doe',    '1990-05-14', NOW(), TRUE),
  ('jane_smith','jane@example.com',  'secret456',   'Jane',  'Smith',  '1985-11-30', NOW(), TRUE),
  ('admin',     'admin@choppee.com', 'admin',        'Admin', 'Choppee','1980-01-01', NOW(), TRUE);

-- ── Carts (OneToOne with users) ───────────────────────────────────────────────
INSERT INTO carts (user_id, created_at) VALUES (1, NOW()), (2, NOW()), (3, NOW());

-- ── Sample cart items for john_doe (user 1, cart 1) ──────────────────────────
INSERT INTO cart_items (cart_id, product_id, quantity, added_at) VALUES
  (1, 1, 1, NOW()),   -- Gyuto Pro
  (1, 9, 2, NOW());   -- 2× Cut-Resistant Gloves

-- ── Sample historical purchase orders ────────────────────────────────────────
-- jane_smith placed an order (user 2)
INSERT INTO purchase_orders (user_id, total_amount, shipping_address, notes, status, order_date, delivered_at) VALUES
  (2, 414.99,
   '42 Lumberjack Lane, Portland OR 97201',
   'Leave at the door',
   'DELIVERED',
   '2025-01-10 09:30:00',
   '2025-01-14 15:00:00'),
  (1, 189.99,
   '7 Blade Street, Austin TX 78701',
   NULL,
   'SHIPPED',
   '2025-03-01 14:00:00',
   NULL);

-- ── Order items for jane's delivered order (order 1) ─────────────────────────
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
  (1, 6,  1, 185.75),  -- Felling Axe (with 15% discount applied)
  (1, 9,  2,  49.99),  -- 2× Gloves
  (1, 10, 1,  64.00);  -- Knife Strip

-- ── Order items for john's shipped order (order 2) ────────────────────────────
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
  (2, 4, 1, 189.99);  -- Bushcraft Fixed Blade (5% discount applied)
