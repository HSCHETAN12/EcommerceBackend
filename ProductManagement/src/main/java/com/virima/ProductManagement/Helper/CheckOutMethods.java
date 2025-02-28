package com.virima.ProductManagement.Helper;


import com.virima.ProductManagement.Entity.*;
import com.virima.ProductManagement.ProductException;
import com.virima.ProductManagement.Repository.OrderAudictRepository;
import com.virima.ProductManagement.Repository.OrderRepository;
import com.virima.ProductManagement.Repository.TransactionRepository;
import com.virima.ProductManagement.Repository.WalletAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CheckOutMethods {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WalletAuditRepository walletAuditRepository;

    @Autowired
    OrderAudictRepository orderAudictRepository;



    public Orders createOrder(int userId, Cart cart, double totalPrice) {
        // Create an order object based on cart and user details
        Orders order = new Orders();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus("PENDING");
        return order;
    }

      public void logFailedTransaction(int userId, double amount) {
        // Log the failed transaction in the Transaction table
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setStatus("FAILED");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void logSuccessfulTransaction(int userId, double amount, int orderId) throws ProductException {
        // Log the successful transaction in the Transaction table, associating it with the order
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transaction.setTimestamp(LocalDateTime.now());

        // Fetch order by ID and associate with the transaction
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ProductException("Order not found"));
        transaction.setOrder(order);

        transactionRepository.save(transaction);
    }

    public void logWalletAudit(int userId, double amount, double newBalance, String transactionType) {
        // Create a WalletAudit entry to track the wallet transaction
        WalletAudit walletAudit = new WalletAudit();
        walletAudit.setUserId(userId);
        walletAudit.setAmount(amount);
        walletAudit.setBalanceAfterTransaction(newBalance);
        walletAudit.setTransactionType(transactionType);
        walletAudit.setTransactiondate(LocalDateTime.now());

        walletAuditRepository.save(walletAudit);
    }

    public void logOrderAudit(int orderId, String previousStatus, String newStatus) {
        // Create an OrderAudit entry to track the order status change
        OrderAudit orderAudit = new OrderAudit();
        orderAudit.setOrderId(orderId);
        orderAudit.setPreviousStatus(previousStatus);
        orderAudit.setNewStatus(newStatus);
        orderAudit.setTimestamp(LocalDateTime.now());

        orderAudictRepository.save(orderAudit);
    }

}
