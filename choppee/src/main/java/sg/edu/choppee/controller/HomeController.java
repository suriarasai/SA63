package sg.edu.choppee.controller;

import sg.edu.choppee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Landing page – shows featured products and current deals.
 */
@Controller
public class HomeController {

    @Autowired private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featured", productService.findFeatured());
        model.addAttribute("deals",    productService.findDeals());
        model.addAttribute("categories", productService.findAllCategories());
        return "index";
    }
}
