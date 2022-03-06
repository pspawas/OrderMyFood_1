package com.omf.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omf.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
@EnableBinding(Sink.class)
@Slf4j
public class DeliveryServiceSink {
    @Autowired
    private com.omf.service.impl.DeliveryServiceImpl DeliveryServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void updatePayment(Payment payment) throws Exception {
        log.info("Payment information received for orderId: " + payment.getOrderId());
        DeliveryServiceImpl.processPaymentDelivery(payment);
    }
}
