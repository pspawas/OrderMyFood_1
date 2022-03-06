package com.omf.rest;

import com.omf.model.OrderResponse;
import com.omf.model.Payment;
import com.omf.service.impl.DeliveryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DeliveryServiceApi {
    private DeliveryServiceImpl deliveryServiceImpl;

    @Autowired
    public DeliveryServiceApi(DeliveryServiceImpl deliveryServiceImpl) {
        this.deliveryServiceImpl = deliveryServiceImpl;
    }

    @RequestMapping(value = "/payments", method = RequestMethod.POST)
    public ResponseEntity<OrderResponse> pay(@RequestBody Payment payment) {
        return deliveryServiceImpl.processPaymentDelivery(payment);
    }
}
