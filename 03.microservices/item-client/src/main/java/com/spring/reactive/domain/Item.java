package com.spring.reactive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Lombok annotations
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    private String id;
    private String description;
    private Double price;

}
