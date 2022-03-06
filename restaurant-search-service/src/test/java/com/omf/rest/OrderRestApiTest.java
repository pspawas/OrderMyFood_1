package com.omf.rest;

import com.omf.model.Order;
import com.omf.rest.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Random;

public class OrderRestApiTest extends AbstractOrderRestApiTest {

    private String getErrorCode(String message) {
        return message.split(",")[2].replace("\"", "").split(":")[1];
    }

    private ResponseEntity<RestaurantResponse> createTestRestaurantData() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        return restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
    }

    private ResponseEntity<OrderResponse> createTestOrderData() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        OrderRequest orderRequest = createOrderRequest();
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().
                getRestaurantId() + "/orders";
        return restTemplate.postForEntity(baseURL, entity, OrderResponse.class);
    }

    @Test
    public void createOrderData_1() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        OrderRequest orderRequest = createOrderRequest();
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().
                getRestaurantId() + "/orders";
        ResponseEntity<OrderResponse> orderResponse = restTemplate.postForEntity(baseURL, entity, OrderResponse.class);
        Assertions.assertEquals(201, orderResponse.getStatusCode().value());
    }

    @Test
    public void createOrderData_2() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        OrderRequest orderRequest = createOrderRequest();
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/orders";
        try {
            restTemplate.postForEntity(baseURL, entity, OrderResponse.class);
        } catch (HttpClientErrorException e) {
            Assertions.assertEquals(404, e.getRawStatusCode());
        }
    }

    @Test
    public void createOrderData_3() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        OrderRequest orderRequest = createOrderRequest();
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/orders";
        try {
            restTemplate.postForEntity(baseURL, entity, OrderResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("ORDER90", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createOrderData_4() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.getItemDetail().setQuantity(20);
        orderRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/orders";
        restTemplate.put(baseURL, entity, OrderResponse.class);

        String baseURLForViewOrders = "http://localhost:" + mongoDBPort + "/api/customers/" + orderRequest.getCustomerName();
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Order>> responseEntity1 = restTemplate.exchange(baseURLForViewOrders, HttpMethod.GET, null, responseType);
        Assertions.assertEquals(20, responseEntity1.getBody().get(0).getItemDetail().getQuantity());
    }

    @Test
    public void createOrderData_5() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setCustomerName(null);
        orderRequest.setCustomerCellNo(null);
        orderRequest.setSpecialNote(null);
        orderRequest.setItemDetail(null);
        orderRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/orders";
        try {
            restTemplate.put(baseURL, entity, OrderResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals(500, e.getRawStatusCode());
        }
    }

    @Test
    public void getOrderData_1() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/orders/" + responseEntity.getBody().getOrderId();
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Order>> responseEntity1 = restTemplate.exchange(baseURL, HttpMethod.GET, null, responseType);
        Assertions.assertEquals(responseEntity.getBody().getRestaurantId(), responseEntity1.getBody().get(0).getRestaurantId());
    }

    @Test
    public void cancelOrderData_1() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        OrderCancelRequest orderCancelRequest = createOrderCancelRequest();
        orderCancelRequest.setOrderIds(new String[]{responseEntity.getBody().getOrderId()});
        HttpEntity<OrderCancelRequest> entity = new HttpEntity<>(orderCancelRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/orders/cancel";
        restTemplate.put(baseURL, entity, OrderCancelResponse.class);
        String baseURLForViewOrders = "http://localhost:" + mongoDBPort + "/api/customers/" + orderCancelRequest.getCustomerName();
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Order>> responseEntity1 = restTemplate.exchange(baseURLForViewOrders, HttpMethod.GET, null, responseType);
        Assertions.assertEquals("CANCELED", responseEntity1.getBody().get(0).getStatus());
    }

    @Test
    public void cancelOrderData_2() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
        orderCancelRequest.setOrderIds(new String[]{responseEntity.getBody().getOrderId()});
        HttpEntity<OrderCancelRequest> entity = new HttpEntity<>(orderCancelRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/orders/cancel";
        try {
            restTemplate.put(baseURL, entity, OrderCancelResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("ORDER109", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void cancelOrderData_3() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
        orderCancelRequest.setCustomerName("Pawas");
        orderCancelRequest.setOrderIds(new String[]{responseEntity.getBody().getOrderId()});
        HttpEntity<OrderCancelRequest> entity = new HttpEntity<>(orderCancelRequest, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/orders/cancel";
        try {
            restTemplate.put(baseURL, entity, OrderCancelResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("ORDER117", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void updateOderAfterPayment_1() {
        ResponseEntity<OrderResponse> responseEntity = createTestOrderData();
        Order order = new Order();
        order.setOrderId(responseEntity.getBody().getOrderId());
        order.setDeliveryTime(getDeliveryTime());
        order.setStatus("PAID");
        HttpEntity<Order> entity = new HttpEntity<>(order, httpHeaders);
        String baseURL = "http://localhost:" + mongoDBPort + "/api/update-order";
        restTemplate.put(baseURL, entity, OrderResponse.class);

        String baseURLForViewOrders = "http://localhost:" + mongoDBPort + "/api/orders/" + responseEntity.getBody().getOrderId();
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Order>> responseEntity1 = restTemplate.exchange(baseURLForViewOrders, HttpMethod.GET, null, responseType);
        Assertions.assertEquals("PAID", responseEntity1.getBody().get(0).getStatus());
    }

    private long getDeliveryTime() {
        int randomPeriod = 10 + new Random().nextInt(40);
        return System.currentTimeMillis() + randomPeriod * 60 * 1000;
    }
}
