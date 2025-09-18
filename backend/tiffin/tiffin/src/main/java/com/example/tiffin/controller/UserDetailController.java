package com.example.tiffin.controller;

import com.example.tiffin.dto.UserFullDetailDTO;
import com.example.tiffin.model.User;
import com.example.tiffin.model.UserDetail;
import com.example.tiffin.repository.UserRepository;
import com.example.tiffin.services.UserDetailService;
import com.example.tiffin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-details")
public class UserDetailController {

    @Autowired
    private  UserDetailService detailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;




        private Long getUserIdFromToken(HttpServletRequest request) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7); // remove "Bearer "
            String username = jwtUtil.extractUsername(token);

            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                    .getId();
        }

        @PostMapping
        public ResponseEntity<UserDetail> createDetail(@RequestBody Map<String, String> payload,
                                                       HttpServletRequest request) {
            Long userId = getUserIdFromToken(request);
            UserDetail detail = detailService.createDetail(userId, payload.get("phoneNumber"), payload.get("address"));
            return ResponseEntity.ok(detail);
        }

        @PutMapping
        public ResponseEntity<UserDetail> updateDetail(@RequestBody Map<String, String> payload,
                                                       HttpServletRequest request) {
            Long userId = getUserIdFromToken(request);
            UserDetail detail = detailService.updateDetail(userId, payload.get("phoneNumber"), payload.get("address"));
            return ResponseEntity.ok(detail);
        }

        @GetMapping
        public ResponseEntity<UserDetail> getDetail(HttpServletRequest request) {
            Long userId = getUserIdFromToken(request);
            return ResponseEntity.ok(detailService.getDetail(userId));
        }

        @DeleteMapping
        public ResponseEntity<Void> deleteDetail(HttpServletRequest request) {
            Long userId = getUserIdFromToken(request);
            detailService.deleteDetail(userId);
            return ResponseEntity.noContent().build();
        }

    @GetMapping("/full")
    public ResponseEntity<UserFullDetailDTO> getFullUserDetail(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetail userDetail = detailService.getDetail(userId);

        UserFullDetailDTO dto = new UserFullDetailDTO(
                user.getName(),
                user.getEmail(),
                userDetail.getPhoneNumber(),
                userDetail.getAddress()
        );

        return ResponseEntity.ok(dto);
    }

}
