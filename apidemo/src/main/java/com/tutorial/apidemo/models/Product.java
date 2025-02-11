package com.tutorial.apidemo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 300)
    private String name;
    private int productYear;
    private Double price;
    private String url;

    public Product(String name, int productYear, Double price, String url) {
        this.name = name;
        this.productYear = productYear;
        this.price = price;
        this.url = url;
    }
}

// ToString to using POJO - Plain Object Java Object give value for client