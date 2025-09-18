package com.example.tiffin.services;


import com.example.tiffin.model.User;
import com.example.tiffin.model.UserDetail;
import com.example.tiffin.repository.UserDetailRepository;
import com.example.tiffin.repository.UserRepository;
import com.example.tiffin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    public UserDetail createDetail(Long userId, String phoneNumber, String address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDetailRepository.existsById(userId)) {
            throw new RuntimeException("Detail already exists for user");
        }

        UserDetail detail = new UserDetail();
        detail.setUser(user);
        detail.setPhoneNumber(phoneNumber);
        detail.setAddress(address);
        return userDetailRepository.save(detail);
    }

    public UserDetail updateDetail(Long userId, String phoneNumber, String address) {
        UserDetail detail = userDetailRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User detail not found"));

        detail.setPhoneNumber(phoneNumber);
        detail.setAddress(address);
        return userDetailRepository.save(detail);
    }

    public void deleteDetail(Long userId) {
        userDetailRepository.deleteById(userId);
    }

    public UserDetail getDetail(Long userId) {
        return userDetailRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Detail not found"));
    }


}
