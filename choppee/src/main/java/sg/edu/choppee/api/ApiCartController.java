// ApiCartController.java
// Replaces: CartController (form POSTs → REST verbs)

package sg.edu.choppee.api;

import sg.edu.choppee.model.Cart;
import sg.edu.choppee.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class ApiCartController {

    @Autowired private CartService cartService;

    private Long userId(HttpServletRequest req) {
        return (Long) req.getAttribute("authenticatedUserId");
    }

    // GET /api/cart  (OLD: GET /cart → viewCart())
    @GetMapping
    public ResponseEntity<?> view(HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(cartService.getCartForUser(uid));
    }

    // POST /api/cart/add  (OLD: POST /cart/add)
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body,
                                 HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        Long productId = Long.valueOf(body.get("productId").toString());
        Integer qty    = Integer.valueOf(body.getOrDefault("quantity","1").toString());
        try {
            cartService.addToCart(uid, productId, qty);
            return ResponseEntity.ok(cartService.getCartForUser(uid));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
        }
    }

    // PUT /api/cart/update  (OLD: POST /cart/update)
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Map<String, Object> body,
                                    HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        Long    itemId = Long.valueOf(body.get("cartItemId").toString());
        Integer qty    = Integer.valueOf(body.get("quantity").toString());
        try {
            cartService.updateQuantity(uid, itemId, qty);
            return ResponseEntity.ok(cartService.getCartForUser(uid));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
        }
    }

    // DELETE /api/cart/remove/:itemId  (OLD: POST /cart/remove)
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<?> remove(@PathVariable Long itemId,
                                    HttpServletRequest req) {
        Long uid = userId(req);
        if (uid == null) return ResponseEntity.status(401).build();
        try {
            cartService.removeItem(uid, itemId);
            return ResponseEntity.ok(cartService.getCartForUser(uid));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
        }
    }
}
