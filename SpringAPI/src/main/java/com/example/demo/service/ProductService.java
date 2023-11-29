package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.api.exceptions.ProductAlreadyExistsException;
import com.example.demo.api.exceptions.ProductNotFoundException;
import com.example.demo.api.model.Product;
import com.example.demo.api.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProduct(String id, String userId) {
    	logger.info("{} | READ | PRODUCT | {}", userId, id);
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts(String userId) {
    	logger.info("{} | READ | ALL PRODUCTS", userId);
        return productRepository.findAll();
    }

    public void addProduct(Product newProduct, String userId) throws ProductAlreadyExistsException {
        Optional<Product> existingProduct = productRepository.findById(newProduct.getId());

        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException("Product already exists with ID: " + newProduct.getId());
        } else {
        	logger.info("{} | WRITE | ADD | {}", userId, newProduct.getId());
            productRepository.save(newProduct);
        }
    }

    public void deleteProduct(String userId, String id) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            productRepository.deleteById(id);
            System.out.println("Product with ID: " + id + " has been deleted");
            logger.info("{} | WRITE | DELETE | {}", userId, id);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public void updateProduct(String id, Product updatedProduct, String userId) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
        	logger.info("{} | WRITE | UPDATE | {}", userId, id);
            productRepository.save(updatedProduct);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
    
}
