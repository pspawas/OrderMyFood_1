package com.omf.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omf.model.CreditCardInfo;
import com.omf.model.Order;
import com.omf.model.OrderResponse;
import com.omf.model.Payment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class DeliveryServiceMockRestServiceServerUnitTest {

    private final static String ORDER_ID = "12345";
    private final static int ORDER_TOTAL_PRICE = 700;
    private static final String PAID = "PAID";
    private final String ORDERMYFOOD_URL = "http://order-my-food";
    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();
    private Payment payment;
    private CreditCardInfo creditCardInfo;

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void test() throws Exception {
        payment = new Payment();
        payment.setAmount(ORDER_TOTAL_PRICE);
        payment.setOrderId(ORDER_ID);
        payment.setPaymentId(UUID.randomUUID().toString());

        creditCardInfo = new CreditCardInfo();
        payment.setCreditCardInfo(creditCardInfo);

        ResponseEntity<OrderResponse> responseEntityPut = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<List<Order>> responseEntityGet = new ResponseEntity<>(HttpStatus.OK);

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(ORDERMYFOOD_URL + "/api/orders/" + ORDER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(responseEntityGet))
                );

        deliveryService.processPaymentDelivery(payment);
        mockServer.verify();
    }
}