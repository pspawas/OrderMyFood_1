package com.omf.service.impl;

import com.omf.constant.RestaurantMessage;
import com.omf.exception.ErrorConstant;
import com.omf.exception.OrderMyFoodException;
import com.omf.model.Restaurant;
import com.omf.repository.RestaurantRepository;
import com.omf.rest.model.RestaurantRequest;
import com.omf.rest.model.RestaurantResponse;
import com.omf.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    MongoTemplate mongoTemplate;

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        Restaurant restaurant = new Restaurant();
        BeanUtils.copyProperties(restaurantRequest, restaurant);
        List<Restaurant> restaurants = restaurantRepository.findByName(restaurantRequest.getName());
        Restaurant createdRestaurant;

        if (restaurants.size() == 0) createdRestaurant = restaurantRepository.save(restaurant);
        else
            throw new OrderMyFoodException(ErrorConstant.REST90);

        BeanUtils.copyProperties(createdRestaurant, restaurantResponse);
        restaurantResponse.setTransactionId(UUID.randomUUID().toString());
        restaurantResponse.setStatus(RestaurantMessage.CREATED);
        return restaurantResponse;
    }
}
