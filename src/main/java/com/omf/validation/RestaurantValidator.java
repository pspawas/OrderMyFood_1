package com.omf.validation;

import com.omf.exception.ErrorConstant;
import com.omf.exception.OrderMyFoodError;
import com.omf.exception.OrderMyFoodException;
import com.omf.rest.model.OrderCancelRequest;
import com.omf.rest.model.OrderRequest;
import com.omf.rest.model.RestaurantRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RestaurantValidator {

    private List<OrderMyFoodError> orderMyFoodErrors = new ArrayList<>();

    public void validateRestaurant(RestaurantRequest o) throws OrderMyFoodException {
        if (StringUtils.isBlank(o.getName())) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.REST100));
        if (StringUtils.isBlank(o.getLocation())) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.REST101));
        if (o.getDistance() <= 0) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.REST102));
        if (StringUtils.isBlank(o.getCuisine())) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.REST103));
        if (o.getBudget() <= 0) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.REST104));
        if (CollectionUtils.isNotEmpty(orderMyFoodErrors)) throw new OrderMyFoodException(orderMyFoodErrors);
    }

    public void validateOrder(OrderRequest o) throws OrderMyFoodException {
        if (o.getItemDetail() != null && (StringUtils.isBlank(o.getItemDetail().getName())
                || o.getItemDetail().getPrice() <= 0
                || o.getItemDetail().getQuantity() <= 0))
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER115));
        if (StringUtils.isBlank(o.getCustomerName()))
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER109));
        if (StringUtils.isBlank(o.getCustomerCellNo()))
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER108));
        if (StringUtils.isBlank(o.getSpecialNote()))
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER112));
        if (CollectionUtils.isNotEmpty(orderMyFoodErrors)) throw new OrderMyFoodException(orderMyFoodErrors);
    }

    public void validateCancelOrder(OrderCancelRequest o) throws OrderMyFoodException {
        if (StringUtils.isBlank(o.getCustomerName()))
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER109));
        if (o.getOrderIds() == null) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER116));
        if (o.getOrderIds() != null && o.getOrderIds().length == 0)
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.ORDER117));
        if (CollectionUtils.isNotEmpty(orderMyFoodErrors)) throw new OrderMyFoodException(orderMyFoodErrors);
    }
}