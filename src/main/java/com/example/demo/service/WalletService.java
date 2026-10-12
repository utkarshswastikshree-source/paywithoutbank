package com.example.demo.service;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.entity.Wallet;
import com.example.demo.exception.WalletException;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Create a new user + wallet together
    public User createUserWithWallet(String name, String email) {
        User user = new User(email, name);
        user = userRepository.save(user);

        Wallet wallet = new Wallet(user);
        walletRepository.save(wallet);

        return user;
    }

    // Add money to a wallet
    @Transactional
    public Wallet deposit(Long walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("Deposit amount must be positive");
        }

        Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet saved = walletRepository.save(wallet);

        transactionRepository.save(new Transaction(saved, amount, "DEPOSIT"));

        return saved;
    }

    // Get wallet balance
    public BigDecimal getBalance(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException("Wallet not found"));
        return wallet.getBalance();
    }

    // Transfer money between two wallets — LOCKED for concurrency safety
    @Transactional
    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("Transfer amount must be positive");
        }
        if (fromWalletId.equals(toWalletId)) {
            throw new WalletException("Cannot transfer to the same wallet");
        }

        // LOCKED fetch for source wallet — prevents race conditions
        Wallet fromWallet = walletRepository.findByIdWithLock(fromWalletId)
                .orElseThrow(() -> new WalletException("Source wallet not found"));

        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new WalletException("Destination wallet not found"));

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new WalletException("Insufficient balance");
        }

        // Debit source
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        walletRepository.save(fromWallet);
        transactionRepository.save(new Transaction(fromWallet, amount, "TRANSFER_OUT"));

        // Credit destination
        toWallet.setBalance(toWallet.getBalance().add(amount));
        walletRepository.save(toWallet);
        transactionRepository.save(new Transaction(toWallet, amount, "TRANSFER_IN"));
    }
}