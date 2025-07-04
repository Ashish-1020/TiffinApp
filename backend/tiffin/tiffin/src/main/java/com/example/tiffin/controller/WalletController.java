package com.example.tiffin.controller;



import com.example.tiffin.services.WalletService;
import com.example.tiffin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtUtil jwtUtil;

    private String extractEmail(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return jwtUtil.extractUsername(header.substring(7));
    }

    @GetMapping("/balance")
    public Map<String, Object> getBalance(HttpServletRequest request) {
        String email = extractEmail(request);
        double balance = walletService.getBalance(email);
        return Map.of("balance", balance);
    }
    @PostMapping("/add")
    public Map<String, Object> addBalance(HttpServletRequest request,
                                          @RequestParam double amount) {
        String email = extractEmail(request);

        if (amount > 0) {
            walletService.addBalance(email, amount);
            return Map.of("message", "Balance added successfully", "status", true);
        } else if (amount < 0) {
            boolean success = walletService.deductBalance(email, Math.abs(amount));
            if (success) {
                return Map.of("message", "Amount deducted successfully", "status", true);
            } else {
                return Map.of("message", "Insufficient balance", "status", false);
            }
        } else {
            return Map.of("message", "Amount cannot be zero", "status", false);
        }
    }


}

