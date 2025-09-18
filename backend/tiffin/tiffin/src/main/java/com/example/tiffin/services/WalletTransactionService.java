package com.example.tiffin.services;

import com.example.tiffin.model.TransactionType;
import com.example.tiffin.model.User;
import com.example.tiffin.model.WalletTransaction;
import com.example.tiffin.repository.UserRepository;
import com.example.tiffin.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WalletTransactionService {

    @Autowired
    private WalletTransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public void recordTransaction(String email, double amount, TransactionType type) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalletTransaction tx = new WalletTransaction(user, amount, type);
        transactionRepository.save(tx);
    }

    public List<WalletTransaction> getUserTransactions(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUserId(user.getId());
    }
}

