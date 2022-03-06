package com.omf.rest;

import com.omf.model.CreditCardInfo;
import com.omf.model.Order;
import com.omf.model.OrderResponse;
import com.omf.model.Payment;
import com.omf.repository.OrderRepository;
import com.omf.repository.PaymentRepository;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.powermock.api.mockito.PowerMockito.when;

/*@ActiveProfiles("IT")
@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {DeliveryServiceApp.class, TestMongoConfig.class})
@TestPropertySource(value = "classpath:application-test.properties")
*//*@RunWith(PowerMockRunner.class)
@PrepareForTest(DeliveryServiceImpl.class)*/
public class DeliveryServiceApiTest {
    private final static String ORDER_ID = "12345";
    private final static int ORDER_TOTAL_PRICE = 700;
    private static final String PAID = "PAID";
    private final String ORDERMYFOOD_URL = "http://order-my-food";
    HttpHeaders httpHeaders = new HttpHeaders();
    @LocalServerPort
    private int mongoDBPort;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RestTemplate restTemplate1;
    private RestTemplate restTemplateDeliveryService = new RestTemplate();
    @Autowired
    private
    MongoTemplate mongoTemplate;
    private Payment payment;
    private CreditCardInfo creditCardInfo;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    //  @Test
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
/*        Mockito.when(deliveryServiceImpl.processPaymentDelivery(payment)).thenReturn(responseEntity);
        mvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON_VALUE).
                content(new ObjectMapper().writeValueAsString(payment))).andExpect(status().isOk());*/

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Payment> entityPayment = new HttpEntity<>(payment, httpHeaders);
        HttpEntity<Order> entityOrder = new HttpEntity<>(order, httpHeaders);

        String baseURL = "http://localhost:" + mongoDBPort + "/api/payments";

        ResponseEntity<OrderResponse> responseEntityPut = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<List<Order>> responseEntityGet = new ResponseEntity<>(HttpStatus.OK);

        /*        DeliveryServiceImpl classUnderTest = Mockito.spy(new DeliveryServiceImpl(orderRepository, paymentRepository, restTemplate));*/
        // Mockito.doReturn(responseEntity1).when(classUnderTest).

        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
        when(restTemplate, "exchange", ORDERMYFOOD_URL + "/api/orders/", HttpMethod.PUT, entityOrder, OrderResponse.class).thenReturn(responseEntityPut);
        when(restTemplate, "exchange", ORDERMYFOOD_URL + "/api/orders/" + ORDER_ID, HttpMethod.GET, null, responseType).thenReturn(responseEntityGet);
        ResponseEntity<OrderResponse> orderResponse = restTemplateDeliveryService.postForEntity(baseURL, entityPayment, OrderResponse.class);
        //    Assertions.assertEquals(201, orderResponse.getStatusCode().value());
    }

    private long getDeliveryTime() {
        int randomPeriod = 10 + new Random().nextInt(40);
        return System.currentTimeMillis() + randomPeriod * 60 * 1000;
    }

    @Profile("HTTPTC")
    @ComponentScan(basePackages = "com.omf", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = DeliveryServiceApi.class), useDefaultFilters = false)
    @Configuration
    @EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
    static class LoadConfig {

    }
}