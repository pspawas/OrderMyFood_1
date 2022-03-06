package com.omf.service;

import com.omf.model.MenuItem;
import com.omf.rest.model.MenuItemRequest;
import com.omf.rest.model.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> findAllMenusByRestaurantId(String rid);

    MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest);
}
