package com.example.tiffin.services;

import com.example.tiffin.dto.OrderRequestDTO;
import com.example.tiffin.dto.OrderResponseDTO;
import com.example.tiffin.dto.OrderSummaryDTO;
import com.example.tiffin.model.*;
import com.example.tiffin.repository.OrderRepository;
import com.example.tiffin.repository.UserRepository;
import com.example.tiffin.util.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;

    public OrderResponseDTO createOrder(OrderRequestDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setListOfMeals(dto.getListOfMeals());
        order.setTotalCost(dto.getTotalCost());
        order.setTransactionType(TransactionforOrderType.valueOf(dto.getTransactionType()));
        order.setTransactionId(dto.getTransactionId());
        order.setName(dto.getName());
        order.setAddress(dto.getAddress());
        order.setMobileNo(dto.getMobileNo());
        order.setTime(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryStatus.PREPARING);
        order.setUser(user);

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public OrderResponseDTO getOrderById(String orderId, String email) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access to this order");
        }

        return mapToResponse(order);
    }

    private OrderResponseDTO mapToResponse(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setListOfMeals(order.getListOfMeals());
        dto.setTotalCost(order.getTotalCost());
        dto.setTransactionType(order.getTransactionType().name());
        dto.setTransactionId(order.getTransactionId());
        dto.setName(order.getName());
        dto.setAddress(order.getAddress());
        dto.setMobileNo(order.getMobileNo());
        dto.setDeliveryStatus(order.getDeliveryStatus().name());
        dto.setTime(order.getTime());
        return dto;
    }


    public void updateOrderStatus(String orderId, DeliveryStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Order order = optionalOrder.get();
        order.setDeliveryStatus(newStatus);
        orderRepository.save(order); // saves updated status to DB
    }



    public List<OrderSummaryDTO> getAllOrdersForUser(String email) {
        List<Order> orders = orderRepository.findByUserEmail(email);
        return orderMapper.toSummaryDTOList(orders);
    }





}


