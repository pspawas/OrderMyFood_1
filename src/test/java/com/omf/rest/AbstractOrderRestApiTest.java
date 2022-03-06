package com.omf.rest;

import com.omf.OrderMyFoodApp;
import com.omf.config.TestMongoConfig;
import com.omf.model.ItemDetail;
import com.omf.model.MenuItem;
import com.omf.model.Order;
import com.omf.model.Restaurant;
import com.omf.rest.model.MenuItemRequest;
import com.omf.rest.model.OrderCancelRequest;
import com.omf.rest.model.OrderRequest;
import com.omf.rest.model.RestaurantRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@ActiveProfiles("IT")
@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {OrderMyFoodApp.class, TestMongoConfig.class})
@TestPropertySource(value = "classpath:application-test.properties")
abstract class AbstractOrderRestApiTest {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    @LocalServerPort
    int mongoDBPort;
    @Autowired
    private
    MongoTemplate mongoTemplate;

    RestaurantRequest createRestaurantRequest() {
        RestaurantRequest restaurantRequest = new RestaurantRequest();
        restaurantRequest.setTransactionId(UUID.randomUUID().toString());
        restaurantRequest.setName("ABC 1 Pvt");
        restaurantRequest.setLocation("Bengaluru");
        restaurantRequest.setDistance(100);
        restaurantRequest.setCuisine("Veg");
        restaurantRequest.setBudget(350);
        return restaurantRequest;
    }

    MenuItemRequest createRestaurantMenuItemRequest() {
        MenuItemRequest menuItemRequest = new MenuItemRequest();
        menuItemRequest.setTransactionId(UUID.randomUUID().toString());
        menuItemRequest.setName("Menu 1 Pvt");
        menuItemRequest.setDescription("Bengaluru");
        menuItemRequest.setPrice(100);
        return menuItemRequest;
    }

    OrderRequest createOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setTransactionId(UUID.randomUUID().toString());
        orderRequest.setItemDetail(createItemDetail());
        orderRequest.setSpecialNote("Full Spicy");
        orderRequest.setCustomerName("Pawas");
        orderRequest.setCustomerCellNo("8888899999");
        return orderRequest;
    }

    OrderCancelRequest createOrderCancelRequest() {
        OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
        orderCancelRequest.setCustomerName("Pawas");
        orderCancelRequest.setOrderIds(new String[]{"123", "234", "345"});
        return orderCancelRequest;
    }

    private ItemDetail createItemDetail() {
        ItemDetail itemDetail = new ItemDetail();
        itemDetail.setName("First Order");
        itemDetail.setPrice(100);
        itemDetail.setQuantity(10);
        return itemDetail;
    }

    @BeforeEach
    void createTestData() {
        //  mongoTemplate.save(createRestaurantRequest());
    }

    @AfterEach
    void cleanTestData() {
        mongoTemplate.dropCollection(Restaurant.class);
        mongoTemplate.dropCollection(MenuItem.class);
        mongoTemplate.dropCollection(Order.class);
    }
}
