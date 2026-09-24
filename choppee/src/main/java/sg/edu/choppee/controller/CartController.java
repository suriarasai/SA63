package sg.edu.choppee.controller;

import sg.edu.choppee.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Shopping cart operations: view, add item, update quantity, remove item.
 * All write operations require an active session (user must be logged in).
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;

    //  Helper 

    private Long requireLogin(HttpSession session, RedirectAttributes ra) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) ra.addFlashAttribute("error", "Please log in first.");
        return userId;
    }

    //  View cart 

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        model.addAttribute("cart", cartService.getCartForUser(userId));
        return "cart";
    }

    //  Add to cart 

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session,
                            RedirectAttributes ra) {
        Long userId = requireLogin(session, ra);
        if (userId == null) return "redirect:/login";

        try {
            cartService.addToCart(userId, productId, quantity);
            ra.addFlashAttribute("success", "Item added to cart!");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/cart";
    }

    //  Update quantity 

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                 @RequestParam Integer quantity,
                                 HttpSession session,
                                 RedirectAttributes ra) {
        Long userId = requireLogin(session, ra);
        if (userId == null) return "redirect:/login";

        try {
            cartService.updateQuantity(userId, cartItemId, quantity);
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/cart";
    }

    //  Remove item 

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long cartItemId,
                             HttpSession session,
                             RedirectAttributes ra) {
        Long userId = requireLogin(session, ra);
        if (userId == null) return "redirect:/login";

        try {
            cartService.removeItem(userId, cartItemId);
            ra.addFlashAttribute("success", "Item removed.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/cart";
    }
}
