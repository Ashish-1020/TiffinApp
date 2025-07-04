package com.example.tiffin.services;


import com.example.tiffin.dto.CartItemDto;
import com.example.tiffin.model.CartItem;
import com.example.tiffin.model.Meal;
import com.example.tiffin.model.User;
import com.example.tiffin.repository.CartRepository;
import com.example.tiffin.repository.MealRepository;
import com.example.tiffin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;


    public List<CartItemDto> getUserCartMinimal(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<CartItem> items = cartRepository.findByUser(user);

        return items.stream()
                .map(item -> new CartItemDto(item.getMeal().getId(), item.getQuantity()))
                .toList();
    }

    public void addOrUpdateCartItem(String email, String mealId, int quantity) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Meal meal = mealRepository.findById(mealId).orElseThrow();

        CartItem cartItem = cartRepository.findByUserAndMeal(user, meal)
                .orElse(new CartItem());

        cartItem.setUser(user);
        cartItem.setMeal(meal);
        cartItem.setQuantity(quantity);

        cartRepository.save(cartItem);
    }

    public void removeCartItem(String email, String mealId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Meal meal = mealRepository.findById(mealId).orElseThrow();
        cartRepository.findByUserAndMeal(user, meal).ifPresent(cartRepository::delete);
    }

    @Transactional
    public  void removeAllCartItem(String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        cartRepository.deleteByUser(user);
    }
}
