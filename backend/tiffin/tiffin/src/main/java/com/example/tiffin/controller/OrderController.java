package com.example.tiffin.controller;

import com.example.tiffin.dto.OrderRequestDTO;
import com.example.tiffin.dto.OrderResponseDTO;
import com.example.tiffin.dto.OrderSummaryDTO;
import com.example.tiffin.services.OrderService;
import com.example.tiffin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    private String extractEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token); // or getEmail(token)
        }
        throw new RuntimeException("Invalid or missing JWT token.");
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO dto,
            HttpServletRequest request) {

        String email = extractEmailFromRequest(request);
        return ResponseEntity.ok(orderService.createOrder(dto, email));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @PathVariable String orderId,
            HttpServletRequest request) {

        String email = extractEmailFromRequest(request);
        return ResponseEntity.ok(orderService.getOrderById(orderId, email));
    }



    // ✅ New endpoint to get all orders for the authenticated user
    @GetMapping("/all")
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrders(HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        return ResponseEntity.ok(orderService.getAllOrdersForUser(email));
    }
}
