package sg.edu.choppee.config;

import sg.edu.choppee.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService;

    @ModelAttribute("loggedInUser")
    public String loggedInUser(HttpSession session) {
        return (String) session.getAttribute("username");
    }

    @ModelAttribute("loggedInUserId")
    public Long loggedInUserId(HttpSession session) {
        return toUserId(session);   // ← use shared helper
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        Long userId = toUserId(session);   // ← same helper
        if (userId == null) return 0;
        return cartService.countItems(userId);
    }

    // Safely resolves userId regardless of whether it was stored as Long or String
    private Long toUserId(HttpSession session) {
        Object raw = session.getAttribute("userId");
        if (raw == null)          return null;
        if (raw instanceof Long l) return l;
        try { return Long.parseLong(raw.toString()); }
        catch (NumberFormatException e) { return null; }
    }
}
