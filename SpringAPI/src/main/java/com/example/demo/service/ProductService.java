package com.example.demo.service;

import com.example.demo.api.exceptions.ProductAlreadyExistsException;
import com.example.demo.api.exceptions.ProductNotFoundException;
import com.example.demo.api.model.Product;
import com.example.demo.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(Product newProduct) throws ProductAlreadyExistsException {
        Optional<Product> existingProduct = productRepository.findById(newProduct.getId());

        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException("Product already exists with ID: " + newProduct.getId());
        } else {
            productRepository.save(newProduct);
        }
    }

    public void deleteProduct(String id) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            productRepository.deleteById(id);
            System.out.println("Product with ID: " + id + " has been deleted");
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public void updateProduct(String id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            productRepository.save(updatedProduct);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
}
