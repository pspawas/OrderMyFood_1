package com.omf.service.impl;

import com.omf.model.CreditCardInfo;
import com.omf.model.Order;
import com.omf.model.OrderResponse;
import com.omf.model.Payment;
import com.omf.repository.OrderRepository;
import com.omf.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryServiceImplTest {
    private final static String ORDER_ID = "12345";
    private final static int ORDER_TOTAL_PRICE = 700;
    private static final String PAID = "PAID";
    private final String ORDERMYFOOD_URL = "http://order-my-food";
    @Mock
    private RestTemplate restTemplate;
    private RestTemplate restTemplate1;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private DeliveryServiceImpl deliveryServiceImpl = new DeliveryServiceImpl(orderRepository, paymentRepository, restTemplate);
    private Payment payment;
    private CreditCardInfo creditCardInfo;
    private Order order;

    @BeforeEach
    public void setUp() {
        //   MockitoAnnotations.initMocks(this);
        //  restTemplate = mock(RestTemplate.class);
        orderRepository = mock(OrderRepository.class);
        paymentRepository = mock(PaymentRepository.class);

        payment = new Payment();
        payment.setAmount(ORDER_TOTAL_PRICE);
        payment.setOrderId(ORDER_ID);
        creditCardInfo = new CreditCardInfo();
        payment.setCreditCardInfo(creditCardInfo);
        order = new Order();
        order.setId(ORDER_ID);
        order.setTotalPrice(ORDER_TOTAL_PRICE);
    }

    private long getDeliveryTime() {
        int randomPeriod = 10 + new Random().nextInt(40);
        return System.currentTimeMillis() + randomPeriod * 60 * 1000;
    }

    // @Test
    public void createOrderData_1() throws Exception {
        payment = new Payment();
        payment.setAmount(ORDER_TOTAL_PRICE);
        payment.setOrderId(ORDER_ID);
        payment.setPaymentId(UUID.randomUUID().toString());

        creditCardInfo = new CreditCardInfo();
        payment.setCreditCardInfo(creditCardInfo);

        Order order = new Order();
        order.setPaymentId(payment.getId());
        order.setDeliveryTime(getDeliveryTime());
        order.setStatus(PAID);

        ResponseEntity<OrderResponse> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Payment> entityPayment = new HttpEntity<>(payment, httpHeaders);
        HttpEntity<Order> entityOrder = new HttpEntity<>(order, httpHeaders);

        ResponseEntity<OrderResponse> responseEntityPut = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<List<Order>> responseEntityGet = new ResponseEntity<>(HttpStatus.OK);

        /*        DeliveryServiceImpl classUnderTest = Mockito.spy(new DeliveryServiceImpl(orderRepository, paymentRepository, restTemplate));*/
        // Mockito.doReturn(responseEntity1).when(classUnderTest).

        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
  /*      when(restTemplate, "exchange", ORDERMYFOOD_URL + "/api/orders/", HttpMethod.PUT, entityOrder, OrderResponse.class).thenReturn(responseEntityPut);

        when(restTemplate, "exchange", ORDERMYFOOD_URL + "/api/orders/" + ORDER_ID, HttpMethod.GET, null, responseType).thenReturn(responseEntityGet);*/

        Mockito.when(restTemplate.exchange(ORDERMYFOOD_URL + "/api/orders/" + ORDER_ID, HttpMethod.GET, null, responseType)).thenReturn(responseEntityGet);

        ResponseEntity<OrderResponse> responseEntity1 = deliveryServiceImpl.processPaymentDelivery(payment);
        /*  ResponseEntity<OrderResponse> orderResponse = restTemplateDeliveryService.postForEntity(baseURL, entityPayment, OrderResponse.class);*/
        //    Assertions.assertEquals(201, orderResponse.getStatusCode().value());
    }
}