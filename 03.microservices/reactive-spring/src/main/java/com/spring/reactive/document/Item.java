package com.spring.reactive.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // MongoDB annotation equal to @Entity for RDBMS.
@Data // Lombok annotation
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    @Id
    private String id;
    private String description;
    private Double price;

}
