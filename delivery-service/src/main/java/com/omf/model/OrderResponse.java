package com.omf.model;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderResponse {
    private String transactionId;
    private String orderId = UUID.randomUUID().toString();
    private String status;
    private String restaurantId;
    private String paymentStatus;
}
