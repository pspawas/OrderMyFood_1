package com.omf.service.impl;

import com.omf.model.CreditCardInfo;
import com.omf.model.Order;
import com.omf.model.OrderResponse;
import com.omf.model.Payment;
import com.omf.repository.OrderRepository;
import com.omf.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class DeliveryServiceImpl {

    private static final String PAID = "PAID";
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final String ORDERMYFOOD_URL = "http://order-my-food";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final RestTemplate restTemplate;

    @Autowired
    public DeliveryServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<OrderResponse> processPaymentDelivery(Payment payment) {
        log.info("executing processPaymentDelivery...");
        ResponseEntity<OrderResponse> orderResponseEntity = null;
        String orderId = payment.getOrderId();
        if (orderId == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(ORDERMYFOOD_URL + "/api/orders/" + orderId, HttpMethod.GET, null, responseType);
        Order order = responseEntity.getBody().get(0);
        if (order != null && !PAID.equalsIgnoreCase(order.getStatus()) && validateCreditCardDetails(payment.getCreditCardInfo()))
            if (order.getTotalPrice() != payment.getAmount()) {
                String errorMessage = "Payment total amount: " + payment.getAmount() + " doesn't match with order price: " +
                        order.getTotalPrice() + ", for orderId = " + orderId;
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                order.setPaymentId(payment.getId());
                order.setDeliveryTime(getDeliveryTime());
                order.setStatus(PAID);

                HttpEntity<Order> entity = new HttpEntity<>(order, httpHeaders);
                //   orderResponseEntity = callOrderMyFoodAPIOrderPost(orderId, entity);
                orderResponseEntity = restTemplate.exchange(ORDERMYFOOD_URL + "/api/update-order", HttpMethod.PUT, entity, OrderResponse.class);
                if (PAID.equalsIgnoreCase(orderResponseEntity.getBody().getPaymentStatus())
                        && paymentRepository.findByPaymentId(payment.getPaymentId()) == null) {
                    payment = paymentRepository.save(payment);
                    log.info("Payment for order id {} saved." + payment.getPaymentId());
                }
            }
        else {
            String errorMessage = order == null ? "Failed to get order for orderId: " + orderId
                    : "Or Invalid credit card information for orderId : " + orderId;
        }
        return orderResponseEntity;
    }

    private ResponseEntity<OrderResponse> callOrderMyFoodAPIOrderPost(String orderId, HttpEntity<Order> entity) {
        return restTemplate.exchange(ORDERMYFOOD_URL + "/api/orders/" + orderId, HttpMethod.PUT, entity, OrderResponse.class);
    }

    private ResponseEntity<List<Order>> callOrderMyFoodAPIOrderGet(String orderId, ParameterizedTypeReference<List<Order>> responseType) {
        return restTemplate.exchange(ORDERMYFOOD_URL + "/api/orders/" + orderId, HttpMethod.GET, null, responseType);
    }

    private boolean validateCreditCardDetails(CreditCardInfo creditCardInfo) {
        return true;
    }

    private long getDeliveryTime() {
        int randomPeriod = 10 + new Random().nextInt(40);
        return System.currentTimeMillis() + randomPeriod * 60 * 1000;
    }
}
