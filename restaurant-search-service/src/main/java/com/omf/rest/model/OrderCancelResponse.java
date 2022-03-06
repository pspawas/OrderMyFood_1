package com.omf.rest.model;

import lombok.Data;

@Data
public class OrderCancelResponse {
    private String transactionId;
    private String status;
}
