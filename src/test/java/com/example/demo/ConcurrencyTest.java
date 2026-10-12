package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ConcurrencyTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    public void testConcurrentTransfers() throws InterruptedException {
        // Setup: create 2 users with wallets
        User alice = walletService.createUserWithWallet("Alice", "alice@test.com");
        User bob = walletService.createUserWithWallet("Bob", "bob@test.com");

        Wallet aliceWallet = walletRepository.findAll().stream()
                .filter(w -> w.getUser().getId().equals(alice.getId()))
                .findFirst().orElseThrow();

        Wallet bobWallet = walletRepository.findAll().stream()
                .filter(w -> w.getUser().getId().equals(bob.getId()))
                .findFirst().orElseThrow();

        // Deposit 1000 into Alice's wallet
        walletService.deposit(aliceWallet.getId(), new BigDecimal("1000"));
        BigDecimal bobStartBalance = walletRepository.findById(bobWallet.getId()).orElseThrow().getBalance();

        // Fire 100 concurrent transfers of 1 rupee each from Alice to Bob
        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    walletService.transfer(aliceWallet.getId(), bobWallet.getId(), BigDecimal.ONE);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // some may fail
                } finally {
                    endGate.countDown();
                }
            });
        }

        startGate.countDown();
        endGate.await();
        executor.shutdown();

        // Reload from DB
        Wallet aliceFinal = walletRepository.findById(aliceWallet.getId()).orElseThrow();
        Wallet bobFinal = walletRepository.findById(bobWallet.getId()).orElseThrow();

        System.out.println("=================================");
        System.out.println("Successful transfers: " + successCount.get());
        System.out.println("Alice final balance: " + aliceFinal.getBalance());
        System.out.println("Bob final balance: " + bobFinal.getBalance());
        System.out.println("Expected Alice: " + (1000 - successCount.get()));
        System.out.println("Expected Bob: " + (bobStartBalance.intValue() + successCount.get()));
        System.out.println("=================================");
    }
}