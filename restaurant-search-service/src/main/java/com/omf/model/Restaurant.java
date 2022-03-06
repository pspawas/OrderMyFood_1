package com.omf.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Document(value = "restaurant")
@Builder
@Getter
@AllArgsConstructor
public class Restaurant {
    int distance;
    String cuisine;
    int budget;
    @Id
    private String id;
    private String restaurantId = UUID.randomUUID().toString();
    @Indexed(unique = true)
    private String name;
    private String location;
}
