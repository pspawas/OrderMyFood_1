package com.omf.repository;

import com.omf.model.Restaurant;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "restaurant")
public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, String> {
    @RestResource(rel = "name", path = "name", description = @Description("Find by name"))
    public List<Restaurant> findByName(@Param("name") String name);

    @RestResource(rel = "location", path = "location", description = @Description("Find by location"))
    public List<Restaurant> findByLocation(@Param("location") String location);

    @RestResource(rel = "distance", path = "distance", description = @Description("Find by distance"))
    public List<Restaurant> findByDistance(@Param("distance") String distance);

    @RestResource(rel = "cuisine", path = "cuisine", description = @Description("Find by cuisine"))
    public List<Restaurant> findByCuisine(@Param("cuisine") String cuisine);

    @RestResource(rel = "budget", path = "budget", description = @Description("Find by budget"))
    public List<Restaurant> findByBudget(@Param("budget") String budget);
}
