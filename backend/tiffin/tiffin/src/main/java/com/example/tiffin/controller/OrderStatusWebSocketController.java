package com.example.tiffin.controller;

import com.example.tiffin.dto.OrderStatusMessageDTO;
import com.example.tiffin.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kitchen")
public class OrderStatusWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private OrderService orderService;

    @PostMapping("/update-status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody OrderStatusMessageDTO statusMessage) {
        try {
            // Step 1: Update database
            orderService.updateOrderStatus(statusMessage.getOrderId(), statusMessage.getStatus());

            // Step 2: Broadcast WebSocket message
            messagingTemplate.convertAndSend(
                    "/topic/orders/" + statusMessage.getOrderId(),
                    statusMessage
            );

            return ResponseEntity.ok("Order status updated and sent.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
