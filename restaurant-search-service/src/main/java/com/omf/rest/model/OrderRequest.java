package com.omf.rest.model;

import com.omf.model.ItemDetail;
import lombok.Data;

@Data
public class OrderRequest {
    private String transactionId;
    private String restaurantId;
    private ItemDetail itemDetail;
    private int totalPrice;
    private long orderTime;
    private String specialNote;
    private long deliveryTime;
    private String paymentId;
    private String customerName;
    private String customerCellNo;
}