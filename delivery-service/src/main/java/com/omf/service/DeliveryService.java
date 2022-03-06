package com.omf.service;

import com.omf.model.OrderResponse;
import com.omf.model.Payment;
import org.springframework.http.ResponseEntity;

public interface DeliveryService {
    ResponseEntity<OrderResponse> processPaymentDelivery(Payment payment);
}