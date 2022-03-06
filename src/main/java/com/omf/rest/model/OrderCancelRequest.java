package com.omf.rest.model;

import lombok.Data;

@Data
public class OrderCancelRequest {
    private String[] orderIds;
    private String customerName;
}
