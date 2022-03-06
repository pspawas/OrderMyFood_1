package com.omf.rest.model;

import lombok.Data;

@Data
public class MenuItemResponse {
    private String transactionId;
    private String status;
    private String restaurantId;
}
