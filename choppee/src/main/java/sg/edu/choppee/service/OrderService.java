package sg.edu.choppee.service;

import sg.edu.choppee.model.*;
import sg.edu.choppee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OrderService – the core purchase-flow logic.
 *
 * ─── What placeOrder() does in one atomic transaction ────────────────────
 *  Step 1 → Load the User and their Cart from the database.
 *  Step 2 → Validate that the cart is not empty.
 *  Step 3 → For every CartItem: check there is enough stock.
 *  Step 4 → Build a PurchaseOrder with price snapshots (OrderItem.unitPrice).
 *           Storing the price at the time of purchase means historical orders
 *           remain accurate even if a product's price changes later.
 *  Step 5 → Deduct the purchased quantity from each Product's stockQuantity.
 *  Step 6 → Save the order (JPA cascades the save to all OrderItem children).
 *  Step 7 → Call CartService.clearCart() to empty the user's cart.
 *  Step 8 → Return the saved PurchaseOrder to the controller.
 *
 * If ANY of the above steps throws an exception, the @Transactional boundary
 * automatically rolls back every database change made in this call –
 * no partial orders, no phantom stock deductions.
 * ─────────────────────────────────────────────────────────────────────────
 */
@Service
public class OrderService {

    // Spring Data repositories injected by Spring's dependency injection container.
    @Autowired private OrderRepository   orderRepository;   // manages PurchaseOrder entities
    @Autowired private CartService       cartService;       // reuses cart logic (clear cart)
    @Autowired private UserRepository    userRepository;    // used to load the buying User
    @Autowired private ProductRepository productRepository; // used to deduct stock

    //  Place Order 

    /**
     * The most important method in the application.
     * All database changes are wrapped in a single atomic transaction.
     *
     * propagation = REQUIRED
     *   Start a new transaction (no outer transaction exists at controller level).
     *   CartService.clearCart() is also REQUIRED, so it JOINS this transaction –
     *   if anything fails after clearCart(), the cart is automatically restored.
     *
     * isolation = SERIALIZABLE
     *   The strictest isolation level.  It prevents:
     *     • Dirty reads   – reading another transaction's uncommitted changes.
     *     • Non-repeatable reads – a re-read of the same row returns different data.
     *     • Phantom reads – a re-run query returns different rows (new inserts).
     *
     *   Why SERIALIZABLE for order placement?
     *   Imagine two users simultaneously buying the LAST unit of a product:
     *     TX-A reads stockQuantity = 1 → passes the stock check
     *     TX-B reads stockQuantity = 1 → passes the stock check
     *     TX-A deducts stock → stockQuantity = 0, commits
     *     TX-B deducts stock → stockQuantity = -1  ← over-sell bug!
     *
     *   SERIALIZABLE prevents this by ensuring the two transactions execute
     *   as if they ran one after the other, not concurrently.  The second
     *   transaction will either wait for the first to commit, or be aborted
     *   with a serialisation failure that the caller can retry.
     *
     *   Trade-off: SERIALIZABLE is slower than READ_COMMITTED because the
     *   database must hold range locks.  For a course demo with H2 and a
     *   handful of concurrent users this has no measurable impact.
     *
     * rollbackFor = Exception.class
     *   Rolls back on BOTH checked exceptions (e.g., IOException) AND
     *   unchecked exceptions (RuntimeException and its subclasses).
     *   Using Exception.class here is deliberately conservative: we never
     *   want a partially saved order to remain in the database.
     *
     * timeout = 30
     *   If the transaction has not completed within 30 seconds, Spring will
     *   forcibly roll it back.  This prevents lock escalation in the database
     *   under high load.
     */
    @Transactional(
        propagation  = Propagation.REQUIRED,
        isolation    = Isolation.SERIALIZABLE,
        rollbackFor  = Exception.class,
        timeout      = 30
    )
    public PurchaseOrder placeOrder(Long userId, String shippingAddress, String notes) {

        // ── Step 1: Load the User ────────────────────────────────────────────
        // We need the User entity to attach to the PurchaseOrder (FK user_id).
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Load the user's current cart.  getCartForUser() is also @Transactional
        // (REQUIRED) so it participates in THIS transaction (no new transaction opened).
        Cart cart = cartService.getCartForUser(userId);

        // ── Step 2: Validate the cart is not empty ───────────────────────────
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place an order with an empty cart.");
        }

        // ── Step 3 & 4: Build the order + validate stock ─────────────────────
        // Create a new PurchaseOrder entity (not yet saved to the database).
        PurchaseOrder order = new PurchaseOrder();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setNotes(notes);
        order.setStatus(OrderStatus.CONFIRMED); // skip PENDING; confirm immediately

        double total = 0.0;

        // Iterate over every line-item in the cart.
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            int     qty     = cartItem.getQuantity();

            // ── Step 3: Stock check ─────────────────────────────────────────
            // If the product has less stock than the requested quantity, abort.
            // Because isolation = SERIALIZABLE, no other transaction can modify
            // the stockQuantity between this check and the deduction below.
            if (product.getStockQuantity() < qty) {
                throw new RuntimeException(
                    "Insufficient stock for '" + product.getName() +
                    "'. Requested: " + qty +
                    ", Available: " + product.getStockQuantity());
            }

            // ── Step 4: Capture price snapshot ─────────────────────────────
            // getEffectivePrice() returns price after discount.
            // Storing this in OrderItem.unitPrice means the order total will
            // still be correct if the product price is changed in the future.
            double unitPrice = product.getEffectivePrice();
            total += unitPrice * qty;

            // Create an OrderItem (the "line" in the order) and attach it.
            OrderItem lineItem = new OrderItem(order, product, qty, unitPrice);
            order.getItems().add(lineItem);

            // ── Step 5: Deduct stock ────────────────────────────────────────
            // Reduce the in-stock count by the purchased quantity.
            product.setStockQuantity(product.getStockQuantity() - qty);
            productRepository.save(product); // SQL: UPDATE products SET stock_quantity = ? WHERE id = ?
        }

        // Round to 2 decimal places to avoid floating-point drift (e.g., $99.9999999).
        order.setTotalAmount(Math.round(total * 100.0) / 100.0);

        // ── Step 6: Save the order ───────────────────────────────────────────
        // orderRepository.save() persists the PurchaseOrder row.
        // CascadeType.ALL on PurchaseOrder.items means JPA also inserts every
        // OrderItem in the list automatically (cascade = save children with parent).
        PurchaseOrder saved = orderRepository.save(order);

        // ── Step 7: Clear the cart ───────────────────────────────────────────
        // clearCart() is REQUIRED propagation → joins THIS transaction.
        // If something fails AFTER this call (hypothetically), the cart rows
        // are restored by the rollback as well.
        cartService.clearCart(userId);

        // ── Step 8: Return the completed order ──────────────────────────────
        // The controller will redirect to the order-success confirmation page.
        return saved;
    }

    //  Query operations 

    /**
     * Returns all orders for the given user, newest first.
     *
     * readOnly = true  – JPA skips dirty-checking; read-only optimisation.
     * propagation = REQUIRED – join existing or start a new transaction.
     * isolation = READ_COMMITTED – consistent read; no need for higher isolation.
     */
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation   = Isolation.READ_COMMITTED,
        readOnly    = true,
        timeout     = 10
    )
    public List<PurchaseOrder> getOrdersForUser(Long userId) {
        // Spring Data generates: SELECT * FROM purchase_orders WHERE user_id = ?
        //                        ORDER BY order_date DESC
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    /**
     * Returns a single order by ID, verifying it belongs to the given user.
     *
     * readOnly = true  – pure read; no writes needed.
     * isolation = READ_COMMITTED – we just need a consistent snapshot of the order.
     *
     * Throws RuntimeException if the order does not exist or belongs to a
     * different user (prevents information disclosure between accounts).
     */
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation   = Isolation.READ_COMMITTED,
        readOnly    = true,
        timeout     = 10
    )
    public PurchaseOrder getOrderById(Long orderId, Long userId) {
        // Load the order or throw a descriptive exception.
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // Ownership check: the order must belong to the requesting user.
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: order does not belong to this user.");
        }

        return order;
    }
}
