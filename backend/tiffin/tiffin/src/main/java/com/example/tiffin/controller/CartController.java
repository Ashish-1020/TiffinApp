package com.example.tiffin.controller;


import com.example.tiffin.dto.CartItemDto;
import com.example.tiffin.services.CartService;
import com.example.tiffin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    private String extractEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        }
        throw new RuntimeException("Invalid or missing JWT token.");
    }

    // 🔹 Get user cart
    @GetMapping
    public List<CartItemDto> getUserCart(HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        return cartService.getUserCartMinimal(email);
    }
    // 🔹 Add/update cart item
    @PostMapping("/add")
    public void addToCart(HttpServletRequest request,
                          @RequestParam String mealId,
                          @RequestParam int quantity) {
        String email = extractEmailFromRequest(request);
        cartService.addOrUpdateCartItem(email, mealId, quantity);
    }

    // 🔹 Remove cart item
    @DeleteMapping("/remove")
    public void removeFromCart(HttpServletRequest request,
                               @RequestParam String mealId) {
        String email = extractEmailFromRequest(request);
        cartService.removeCartItem(email, mealId);
    }


    //Empty Cart
    @DeleteMapping("/removeAll")
    public  void removeAll(HttpServletRequest request){
        String email = extractEmailFromRequest(request);
        cartService.removeAllCartItem(email);
    }
}
