package com.omf.service;

import com.omf.rest.model.RestaurantRequest;
import com.omf.rest.model.RestaurantResponse;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest restaurant);
}
