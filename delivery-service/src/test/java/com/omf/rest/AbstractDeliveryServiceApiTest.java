package com.omf.rest;

import com.omf.model.CreditCardInfo;
import com.omf.model.Order;
import com.omf.model.Payment;
import com.omf.repository.OrderRepository;
import com.omf.repository.PaymentRepository;
import com.omf.service.impl.DeliveryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.mockito.Mockito.mock;

@ActiveProfiles("HTTPTC")
@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = {DeliveryServiceApi.class, TestMongoConfig.class})
@WebMvcTest(controllers = {DeliveryServiceApi.class})
abstract class AbstractDeliveryServiceApiTest {
    private final static String ORDER_ID = "12345";
    private final static int ORDER_TOTAL_PRICE = 700;
    private final String ORDERMYFOOD_URL = "http://order-my-food";
    @Autowired
    MockMvc mvc;
    Payment payment;
    @MockBean
    DeliveryServiceImpl deliveryServiceImpl;
    /* @LocalServerPort
     int mongoDBPort;*/
    private CreditCardInfo creditCardInfo;
    private Order order;
    @Mock
    private RestTemplate restTemplate;
    private HttpEntity<Order> entity;
    @Mock
    private MongoTemplate mongoTemplate;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void setUp() {
        HttpHeaders httpHeaders = new HttpHeaders();
        MockitoAnnotations.initMocks(this);
        restTemplate = mock(RestTemplate.class);
        orderRepository = mock(OrderRepository.class);
        paymentRepository = mock(PaymentRepository.class);
        // deliveryService = mock(DeliveryServiceImpl.class);

        payment = new Payment();
        payment.setAmount(ORDER_TOTAL_PRICE);
        payment.setOrderId(ORDER_ID);
        payment.setPaymentId(UUID.randomUUID().toString());
        creditCardInfo = new CreditCardInfo();
        payment.setCreditCardInfo(creditCardInfo);
        order = new Order();
        order.setId(ORDER_ID);
        order.setTotalPrice(ORDER_TOTAL_PRICE);
        entity = new HttpEntity<>(order, httpHeaders);
    }

    @AfterEach
    void cleanTestData() {
        mongoTemplate.dropCollection(Payment.class);
    }

    @Profile("HTTPTC")
    @ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = DeliveryServiceApi.class), useDefaultFilters = false)
    @Configuration
    @EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
    public static class LoadConfig {

    }
}
