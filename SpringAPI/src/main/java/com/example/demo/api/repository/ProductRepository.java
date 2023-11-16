package com.example.demo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.api.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {


}
