package com.omf.rest.model;

import lombok.Data;

@Data
public class RestaurantRequest {
    int distance;
    String cuisine;
    int budget;
    private String transactionId;
    private String name;
    private String location;
}
