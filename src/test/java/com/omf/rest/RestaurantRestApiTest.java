package com.omf.rest;

import com.omf.exception.ErrorConstant;
import com.omf.model.MenuItem;
import com.omf.rest.model.MenuItemRequest;
import com.omf.rest.model.MenuItemResponse;
import com.omf.rest.model.RestaurantRequest;
import com.omf.rest.model.RestaurantResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

public class RestaurantRestApiTest extends AbstractRestaurantRestApiTest {

    private String getErrorCode(String message) {
        return message.split(",")[2].replace("\"", "").split(":")[1];
    }

    private ResponseEntity<RestaurantResponse> createTestRestaurantData() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        return restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
    }

    private ResponseEntity<MenuItemResponse> createTestMenuItemData() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/menuItems";

        MenuItemRequest menuItemRequest = createRestaurantMenuItemRequest();
        menuItemRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<MenuItemRequest> entity = new HttpEntity<>(menuItemRequest, httpHeaders);
        return restTemplate.postForEntity(baseURL, entity, MenuItemResponse.class);
    }

    @Test
    public void createRestaurant_1() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        Assertions.assertEquals(201, restaurantResponse.getStatusCode().value());
    }

    @Test
    public void createRestaurant_2() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        restaurantRequest.setName(null);
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        try {
            restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("REST100", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createRestaurant_3() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        restaurantRequest.setLocation(null);
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        try {
            restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("REST101", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createRestaurant_4() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        restaurantRequest.setDistance(0);
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        try {
            restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("REST102", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createRestaurant_5() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        restaurantRequest.setCuisine(null);
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        try {
            restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("REST103", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createRestaurant_6() {
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        restaurantRequest.setBudget(0);
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        try {
            restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("REST104", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createRestaurant_7() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants";
        RestaurantRequest restaurantRequest = createRestaurantRequest();
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurantRequest, httpHeaders);
        ResponseEntity<RestaurantResponse> restaurantResponse = null;
        try {
            restaurantResponse = restTemplate.postForEntity(baseURL, entity, RestaurantResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("REST90", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createMenuItem_1() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/menuItems";
        MenuItemRequest menuItemRequest = createRestaurantMenuItemRequest();
        menuItemRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<MenuItemRequest> entity = new HttpEntity<>(menuItemRequest, httpHeaders);
        ResponseEntity<MenuItemResponse> menuItemResponse = restTemplate.postForEntity(baseURL, entity, MenuItemResponse.class);
        Assertions.assertEquals(201, menuItemResponse.getStatusCode().value());
    }

    @Test
    public void createMenuItem_2() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/menuItems";
        MenuItemRequest menuItemRequest = createRestaurantMenuItemRequest();
        menuItemRequest.setName(null);
        menuItemRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<MenuItemRequest> entity = new HttpEntity<>(menuItemRequest, httpHeaders);
        try {
            ResponseEntity<MenuItemResponse> menuItemResponse = restTemplate.postForEntity(baseURL, entity, MenuItemResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("MENU100", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createMenuItem_3() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/menuItems";
        MenuItemRequest menuItemRequest = createRestaurantMenuItemRequest();
        menuItemRequest.setDescription(null);
        menuItemRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<MenuItemRequest> entity = new HttpEntity<>(menuItemRequest, httpHeaders);
        try {
            ResponseEntity<MenuItemResponse> menuItemResponse = restTemplate.postForEntity(baseURL, entity, MenuItemResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals(ErrorConstant.MENU101, getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createMenuItem_4() {
        ResponseEntity<RestaurantResponse> responseEntity = createTestRestaurantData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + responseEntity.getBody().getRestaurantId() + "/menuItems";
        MenuItemRequest menuItemRequest = createRestaurantMenuItemRequest();
        menuItemRequest.setPrice(0);
        menuItemRequest.setRestaurantId(responseEntity.getBody().getRestaurantId());
        HttpEntity<MenuItemRequest> entity = new HttpEntity<>(menuItemRequest, httpHeaders);
        try {
            ResponseEntity<MenuItemResponse> menuItemResponse = restTemplate.postForEntity(baseURL, entity, MenuItemResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("MENU102", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void createMenuItem_5() {
        ResponseEntity<MenuItemResponse> createdMenuItemResponse = createTestMenuItemData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/restaurants/" + createdMenuItemResponse.getBody().getRestaurantId() + "/menuItems";
        MenuItemRequest menuItemRequest = createRestaurantMenuItemRequest();
        menuItemRequest.setRestaurantId(createdMenuItemResponse.getBody().getRestaurantId());
        HttpEntity<MenuItemRequest> entity = new HttpEntity<>(menuItemRequest, httpHeaders);
        try {
            ResponseEntity<MenuItemResponse> menuItemResponse = restTemplate.postForEntity(baseURL, entity, MenuItemResponse.class);
        } catch (HttpServerErrorException e) {
            Assertions.assertEquals("MENU90", getErrorCode(e.getMessage()));
        }
    }

    @Test
    public void getMenuItems_1() {
        ResponseEntity<MenuItemResponse> createdMenuItemResponse = createTestMenuItemData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/menuItems/" + createdMenuItemResponse.getBody().getRestaurantId();
        ParameterizedTypeReference<List<MenuItem>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<MenuItem>> menuItemResponse = restTemplate.exchange(baseURL, HttpMethod.GET, null, responseType);
        Assertions.assertEquals("Menu 1 Pvt", menuItemResponse.getBody().get(0).getName());
    }

    @Test
    public void getMenuItems_2() {
        ResponseEntity<MenuItemResponse> createdMenuItemResponse = createTestMenuItemData();
        String baseURL = "http://localhost:" + mongoDBPort + "/api/menuItems/" + createdMenuItemResponse.getBody().getRestaurantId() + "001";
        ParameterizedTypeReference<List<MenuItem>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<MenuItem>> menuItemResponse = restTemplate.exchange(baseURL, HttpMethod.GET, null, responseType);
        Assertions.assertEquals(0, menuItemResponse.getBody().size());
    }
}
