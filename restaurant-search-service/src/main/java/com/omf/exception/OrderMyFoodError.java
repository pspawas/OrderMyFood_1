package com.omf.exception;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderMyFoodError {
    private String transactionId;
    private long dateTime;
    private String errorCode;
    private String details;

    public static OrderMyFoodError build(String errorCode) {
        OrderMyFoodError o = new OrderMyFoodError();
        o.setDetails(ErrorConstant.REST_ERRORS.get(errorCode));
        o.setErrorCode(errorCode);
        o.setTransactionId(UUID.randomUUID().toString());
        o.setDateTime(System.currentTimeMillis());
        return o;
    }
}
