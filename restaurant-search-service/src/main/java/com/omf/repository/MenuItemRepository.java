package com.omf.repository;

import com.omf.model.MenuItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "menuitem")
public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, String> {

    @RestResource(description = @Description("Get all menu items by restaurant id"))
    public List<MenuItem> findAllByRestaurantIdAndName(@Param("restaurantId") String restaurantId, @Param("name") String name);

    @RestResource(rel = "name", description = @Description("Get menu item by name"))
    public List<MenuItem> findByName(@Param("name") String name);

    @RestResource(rel = "restaurantId", description = @Description("Get menu items by restaurantId"))
    List<MenuItem> findAllMenusByRestaurantId(@Param("restaurantId") String restaurantId);
}