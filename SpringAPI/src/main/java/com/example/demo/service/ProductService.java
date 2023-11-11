package com.example.demo.service;

import com.example.demo.api.exceptions.ProductAlreadyExistsException;
import com.example.demo.api.exceptions.ProductNotFoundException;
import com.example.demo.api.model.Product;
import com.example.demo.api.model.User;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private List<Product> productList;
    public ProductService(){
        productList = new ArrayList<>();

        Product product = new Product("Pen", 2, new Date());
        Product product2 = new Product("Keyboard", 79.99, new Date());
        productList.addAll(Arrays.asList(product, product2));
    }

    public Optional<Product> getProduct(Integer id) {
        Optional<Product> optionalProduct = productList.stream()
                .filter(product -> product.getId() == id)
                .findFirst();

        if (optionalProduct.isPresent()) {
            return optionalProduct;
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
    
    public List<Product> getAllProducts() {
    	return productList;
    }

    public void addProduct(Product newProduct) throws ProductAlreadyExistsException {
        boolean productExists = productList.stream()
                .anyMatch(product -> product.getId() == newProduct.getId());

        if (productExists) {
            throw new ProductAlreadyExistsException("Product already exists with ID: " + newProduct.getId());
        } else {
            productList.add(newProduct);
        }
    }

    public void deleteProduct(Integer id) {
        Optional<Product> productToRemove = productList.stream()
                .filter(product -> product.getId() == id)
                .findFirst();

        if (productToRemove.isPresent()) {
            Product removedProduct = productToRemove.get();
            boolean removed = productList.removeIf(product -> product.getId() == id);
            if (removed) {
                System.out.println("Product named : " + removedProduct.getName() + " has been deleted");
            } else {
                throw new ProductNotFoundException("Error occurred while deleting product with ID: " + id);
            }
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public void updateProduct(Integer id, Product updatedProduct) {
        boolean found = false;
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getId() == id) {
                productList.set(i, updatedProduct);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
}
