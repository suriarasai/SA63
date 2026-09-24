package sg.edu.choppee.controller;

import sg.edu.choppee.model.PurchaseOrder;
import sg.edu.choppee.service.CartService;
import sg.edu.choppee.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Checkout + order history.
 *
 * Purchase flow:
 *   GET  /checkout         → confirm cart, enter shipping address
 *   POST /checkout/place   → calls OrderService.placeOrder() → redirect to success
 *   GET  /orders           → user's order history
 *   GET  /orders/{id}      → order detail
 */
@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CartService  cartService;

    //  Guard 

    private Long requireLogin(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    //  Checkout form 

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        Long userId = requireLogin(session);
        if (userId == null) return "redirect:/login";

        var cart = cartService.getCartForUser(userId);
        if (cart.getItems().isEmpty()) return "redirect:/cart";

        model.addAttribute("cart", cart);
        return "checkout";
    }

    //  Place the order (core purchase logic) 

    @PostMapping("/checkout/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             @RequestParam(required = false) String notes,
                             HttpSession session,
                             RedirectAttributes ra) {
        Long userId = requireLogin(session);
        if (userId == null) return "redirect:/login";

        try {
            PurchaseOrder order = orderService.placeOrder(userId, shippingAddress, notes);
            ra.addFlashAttribute("order", order);
            return "redirect:/orders/" + order.getId() + "/success";
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/checkout";
        }
    }

    //  Order success confirmation 

    @GetMapping("/orders/{id}/success")
    public String orderSuccess(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = requireLogin(session);
        if (userId == null) return "redirect:/login";

        PurchaseOrder order = orderService.getOrderById(id, userId);
        model.addAttribute("order", order);
        return "order-success";
    }

    //  Order history 

    @GetMapping("/orders")
    public String orderHistory(HttpSession session, Model model) {
        Long userId = requireLogin(session);
        if (userId == null) return "redirect:/login";

        List<PurchaseOrder> orders = orderService.getOrdersForUser(userId);
        model.addAttribute("orders", orders);
        return "orders";
    }

    // Order detail 

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = requireLogin(session);
        if (userId == null) return "redirect:/login";

        PurchaseOrder order = orderService.getOrderById(id, userId);
        model.addAttribute("order", order);
        return "order-detail";
    }
}
