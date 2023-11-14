package com.example.demo.api.controller;

import com.example.demo.api.exceptions.ProductAlreadyExistsException;
import com.example.demo.api.exceptions.ProductNotFoundException;
import com.example.demo.api.model.Product;
import com.example.demo.api.model.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/product")
    public Product getProduct(@RequestParam String id){
        Optional product = productService.getProduct(id);
        if(product.isPresent()){
            return (Product) product.get();
        }
        return null;
    };
    
    @GetMapping("/all-product")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestBody Product newProduct) {
        try {
            productService.addProduct(newProduct);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (ProductAlreadyExistsException e) {
            return new ResponseEntity<>("Product already exists", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        try {
            productService.updateProduct(id, updatedProduct);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
}
