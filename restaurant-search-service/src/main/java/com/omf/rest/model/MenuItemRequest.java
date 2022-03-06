package com.omf.rest.model;

import lombok.Data;

@Data
public class MenuItemRequest {
    private String transactionId;
    private String restaurantId;
    private String name;
    private String description;
    private int price;
}
