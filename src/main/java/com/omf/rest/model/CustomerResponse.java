package com.omf.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.omf.model.ItemDetail;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Document
public class CustomerResponse {
    private String restaurantId;
    private ItemDetail itemDetail;
    private int totalPrice;
    private long orderTime;
    private String specialNote;
    private long deliveryTime;
    private String paymentId;
    private String customerName;
    private String customerCellNo;
    private String status;
}
