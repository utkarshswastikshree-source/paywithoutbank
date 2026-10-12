package com.example.demo.controller;

import com.example.demo.entity.Wallet;
import com.example.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    // Request body for deposit
    public static class DepositRequest {
        public BigDecimal amount;
    }

    @PostMapping("/{id}/deposit")
    public Wallet deposit(@PathVariable Long id, @RequestBody DepositRequest req) {
        return walletService.deposit(id, req.amount);
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable Long id) {
        return walletService.getBalance(id);
    }
    public static class TransferRequest {
        public Long fromWalletId;
        public Long toWalletId;
        public BigDecimal amount;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest req) {
        walletService.transfer(req.fromWalletId, req.toWalletId, req.amount);
        return "Transfer successful";
    }
}