package group1.intern.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProductListController {
    @GetMapping("/product-list")
    public String getMethodName() {
        return "screens/products/index";
    }
    
}
