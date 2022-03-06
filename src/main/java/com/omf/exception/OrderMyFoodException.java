package com.omf.exception;

import java.util.List;

public class OrderMyFoodException extends RuntimeException {

    private static final long serialVersionUID = -276793195697383092L;
    private transient List<OrderMyFoodError> orderMyFoodErrorList;

    public OrderMyFoodException(List<OrderMyFoodError> orderMyFoodErrors) {
        orderMyFoodErrorList = orderMyFoodErrors;
    }

    public OrderMyFoodException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderMyFoodException(String errorCode) {
        orderMyFoodErrorList = ErrorConstant.getError(errorCode);
    }

    public OrderMyFoodException(Throwable cause) {
        super(cause);
    }

    List<OrderMyFoodError> getOrderMyFoodErrorList() {
        return orderMyFoodErrorList;
    }

    public void setRestaurantErrorList(List<OrderMyFoodError> orderMyFoodErrorList) {
        this.orderMyFoodErrorList = orderMyFoodErrorList;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "OrderMyFoodException ( orderMyFoodErrors = " + getOrderMyFoodErrorList();
    }
}
