package com.example.itemam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemRequest {

    private int id;
    private String title;
    private String description;
    private double price;
    private String currency;
    private int categoryId;
    private int userId;

}
