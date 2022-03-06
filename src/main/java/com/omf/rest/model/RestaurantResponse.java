package com.omf.rest.model;

import lombok.Data;

@Data
public class RestaurantResponse {
    private String transactionId;
    private String status;
    private String restaurantId;
}
