package com.example.tiffin.services;


import com.example.tiffin.model.User;
import com.example.tiffin.model.Wallet;
import com.example.tiffin.repository.UserRepository;
import com.example.tiffin.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public double getBalance(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return walletRepository.findByUser(user).map(Wallet::getBalance).orElse(0.0);
    }

    public void addBalance(String email, double amount) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Wallet wallet = walletRepository.findByUser(user).orElse(new Wallet());
        wallet.setUser(user);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    @Transactional
    public boolean deductBalance(String email, double amount) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Wallet wallet = walletRepository.findByUserForUpdate(user).orElseThrow();

        if (wallet.getBalance() < amount) return false;

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
        return true;
    }


    public void createWalletForUser(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);
    }
}
