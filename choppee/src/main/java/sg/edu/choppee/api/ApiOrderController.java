// ApiOrderController.java
// Replaces: OrderController (redirects → JSON responses)

package sg.edu.choppee.api;

import sg.edu.choppee.model.PurchaseOrder;
import sg.edu.choppee.service.CartService;
import sg.edu.choppee.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ApiOrderController {

    @Autowired private OrderService orderService;
    @Autowired private CartService  cartService;

    private Long userId(HttpServletRequest req) {
        return (Long) req.getAttribute("authenticatedUserId");
    }

    public record CheckoutRequest(String shippingAddress, String notes) {}

    // POST /api/checkout/place  (OLD: POST /checkout/place)
    @PostMapping("/api/checkout/place")
    public ResponseEntity<?> placeOrder(@RequestBody CheckoutRequest body,
                                        HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        try {
            PurchaseOrder order = orderService.placeOrder(
                uid, body.shippingAddress(), body.notes());
            return ResponseEntity.ok(order);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
        }
    }

    // GET /api/orders  (OLD: GET /orders → orderHistory())
    @GetMapping("/api/orders")
    public ResponseEntity<?> list(HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        List<PurchaseOrder> orders = orderService.getOrdersForUser(uid);
        return ResponseEntity.ok(orders);
    }

    // GET /api/orders/:id  (OLD: GET /orders/{id} → orderDetail())
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id,
                                    HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        try {
            PurchaseOrder order = orderService.getOrderById(id, uid);
            return ResponseEntity.ok(order);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404)
                .body(Map.of("message", ex.getMessage()));
        }
    }
}
