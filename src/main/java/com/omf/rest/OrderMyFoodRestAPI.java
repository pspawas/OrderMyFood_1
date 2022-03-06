package com.omf.rest;

import com.omf.model.Order;
import com.omf.rest.model.MenuItemRequest;
import com.omf.rest.model.OrderCancelRequest;
import com.omf.rest.model.OrderRequest;
import com.omf.rest.model.RestaurantRequest;
import com.omf.service.MenuItemService;
import com.omf.service.OrderService;
import com.omf.service.RestaurantService;
import com.omf.validation.MenuItemValidator;
import com.omf.validation.RestaurantValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderMyFoodRestAPI {

    private static final String MENU_ITEMS_RESTAURANT_ID = "/menuItems/{restaurantId}";
    private static final String RESTAURANTS_RESTAURANT_ID_ORDERS = "/restaurants/{restaurantId}/orders";
    private static final String ORDERS_CANCEL = "/orders/cancel";
    private static final String CUSTOMERS_CUSTOMER_NAME = "/customers/{customerName}";
    private static final String RESTAURANTS = "/restaurants";
    private static final String RESTAURANTS_RESTAURANT_ID_MENU_ITEMS = "/restaurants/{restaurantId}/menuItems";
    private static final String CUSTOMERS_ORDER_ID = "/orders/{orderId}";
    private static final String UPDATE_ORDER = "/update-order";
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final RestaurantValidator restaurantValidator;
    private final OrderService orderService;

    @Autowired
    public OrderMyFoodRestAPI(RestaurantService restaurantService, MenuItemService menuItemService, OrderService orderService, RestaurantValidator restaurantValidator) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
        this.restaurantValidator = restaurantValidator;
        this.orderService = orderService;
    }

    @RequestMapping(value = RESTAURANTS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        log.info("Creating Restaurant with detail {} ", restaurantRequest);
        restaurantRequest.setTransactionId(UUID.randomUUID().toString());
        new RestaurantValidator().validateRestaurant(restaurantRequest);
        ResponseEntity responseEntity = new ResponseEntity<>(restaurantService.createRestaurant(restaurantRequest),
                HttpStatus.CREATED);
        return responseEntity;
    }

    @RequestMapping(value = RESTAURANTS_RESTAURANT_ID_MENU_ITEMS, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createMenuItem(@RequestBody MenuItemRequest menuItemRequest, @PathVariable String restaurantId) {
        log.info("Creating Menu Item with detail : {} for restaurant id {}", menuItemRequest, restaurantId);
        menuItemRequest.setTransactionId(UUID.randomUUID().toString());
        new MenuItemValidator().validateMenuItem(menuItemRequest);
        menuItemRequest.setRestaurantId(restaurantId);
        ResponseEntity responseEntity = new ResponseEntity<>(menuItemService.createMenuItem(menuItemRequest), HttpStatus.CREATED);
        return responseEntity;
    }

    @RequestMapping(value = MENU_ITEMS_RESTAURANT_ID, method = RequestMethod.GET)
    public ResponseEntity<Object> getMenuItems(@PathVariable String restaurantId) {
        log.info("Getting Menu Item Id {} ", restaurantId);
        ResponseEntity responseEntity = new ResponseEntity<>(menuItemService.findAllMenusByRestaurantId(restaurantId), HttpStatus.FOUND);
        return responseEntity;
    }

    @RequestMapping(value = RESTAURANTS_RESTAURANT_ID_ORDERS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createOder(@RequestBody OrderRequest orderRequest, @PathVariable String restaurantId) {
        log.info("Creating Order with detail {} for restaurant Id {} ", orderRequest, restaurantId);
        orderRequest.setRestaurantId(restaurantId);
        new RestaurantValidator().validateOrder(orderRequest);
        return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.CREATED);
    }

    @RequestMapping(value = RESTAURANTS_RESTAURANT_ID_ORDERS, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateOderForRestaurant(@RequestBody OrderRequest orderRequest, @PathVariable String restaurantId) {
        log.info("Updating Order with detail {} for restaurant Id {} ", orderRequest, restaurantId);
        orderRequest.setRestaurantId(restaurantId);
        new RestaurantValidator().validateOrder(orderRequest);
        return new ResponseEntity<>(orderService.updateOrder(orderRequest), HttpStatus.OK);
    }

    @RequestMapping(value = UPDATE_ORDER, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateOderAfterPayment(@RequestBody Order order) {
        log.info("Paying Order with detail {} ", order);
        return new ResponseEntity<>(orderService.updateOderAfterPayment(order), HttpStatus.OK);
    }

    @RequestMapping(value = ORDERS_CANCEL, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cancelOder(@RequestBody OrderCancelRequest orderCancelRequest) {
        log.info("Canceling Order with detail {}", orderCancelRequest);
        new RestaurantValidator().validateCancelOrder(orderCancelRequest);
        return new ResponseEntity<>(orderService.cancelOrder(orderCancelRequest), HttpStatus.OK);
    }

    @RequestMapping(value = CUSTOMERS_CUSTOMER_NAME, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrderByCustomerName(@PathVariable String customerName) {
        log.info("Getting Order By Customer Name {}", customerName);
        return new ResponseEntity<>(orderService.getOrderByCustomerName(customerName), HttpStatus.OK);
    }

    @RequestMapping(value = CUSTOMERS_ORDER_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getByOrderId(@PathVariable String orderId) {
        log.info("Getting Order By order id {}", orderId);
        return new ResponseEntity<>(orderService.getByOrderId(orderId), HttpStatus.OK);
    }
}
