package com.example.tiffin.util;


import com.example.tiffin.dto.OrderSummaryDTO;
import com.example.tiffin.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderSummaryDTO toSummaryDTO(Order order) {
        List<String> meals;
        try {
            meals = objectMapper.readValue(order.getListOfMeals(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            meals = Collections.singletonList("Invalid meal data");
        }

        return new OrderSummaryDTO(
                order.getOrderId(),
                order.getTime(),
                meals,
                order.getTotalCost(),
                order.getDeliveryStatus().name()
        );
    }

    public List<OrderSummaryDTO> toSummaryDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }
}
