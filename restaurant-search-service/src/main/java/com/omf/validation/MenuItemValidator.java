package com.omf.validation;

import com.omf.exception.ErrorConstant;
import com.omf.exception.OrderMyFoodError;
import com.omf.exception.OrderMyFoodException;
import com.omf.rest.model.MenuItemRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MenuItemValidator {

    private List<OrderMyFoodError> orderMyFoodErrors = new ArrayList<>();

    public void validateMenuItem(MenuItemRequest o) throws OrderMyFoodException {
        if (StringUtils.isBlank(o.getName())) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.MENU100));
        if (StringUtils.isBlank(o.getDescription()))
            orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.MENU101));
        if (o.getPrice() <= 0) orderMyFoodErrors.add(OrderMyFoodError.build(ErrorConstant.MENU102));
        if (CollectionUtils.isNotEmpty(orderMyFoodErrors)) throw new OrderMyFoodException(orderMyFoodErrors);
    }
}
