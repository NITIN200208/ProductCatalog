package com.example.model;

import lombok.*;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;



@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;


    private double price;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @Min(0)
    private int stock;

    @Min(1) @Max(5)
    private Double ratings; 

    private Date createdAt;
    private Date updatedAt;
}
