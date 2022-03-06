package com.omf.service.impl;

import com.omf.constant.MenuItemMessage;
import com.omf.exception.ErrorConstant;
import com.omf.exception.OrderMyFoodException;
import com.omf.model.MenuItem;
import com.omf.repository.MenuItemRepository;
import com.omf.rest.model.MenuItemRequest;
import com.omf.rest.model.MenuItemResponse;
import com.omf.service.MenuItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItem> findAllMenusByRestaurantId(String restaurantId) {
        return menuItemRepository.findAllMenusByRestaurantId(restaurantId);
    }

    @Override
    public MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest) {
        MenuItemResponse menuItemResponse = new MenuItemResponse();
        MenuItem menuItem = new MenuItem();
        BeanUtils.copyProperties(menuItemRequest, menuItem);
        List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantIdAndName(menuItem.getRestaurantId(),
                menuItem.getName());
        MenuItem createdMenuItem;
        if (menuItems.size() == 0) createdMenuItem = menuItemRepository.save(menuItem);
        else
            throw new OrderMyFoodException(ErrorConstant.MENU90);

        BeanUtils.copyProperties(createdMenuItem, menuItemResponse);
        menuItemResponse.setTransactionId(UUID.randomUUID().toString());
        menuItemResponse.setStatus(MenuItemMessage.CREATED);
        return menuItemResponse;
    }
}
