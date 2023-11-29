package com.example.demo.api.controller;

import com.example.demo.api.exceptions.ProductAlreadyExistsException;
import com.example.demo.api.exceptions.ProductNotFoundException;
import com.example.demo.api.model.Product;
import com.example.demo.api.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

	private final ProductService productService;
	private ProductRepository productRepository;

	@Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

	@GetMapping("/product")
	public ResponseEntity<Product> getProduct(@RequestParam String id, @RequestParam String userId) {
		try {
			Product product = productService.getProduct(id, userId)
					.orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all-products")
	public List<Product> getAllProducts(@RequestParam String userId) {
		return productService.getAllProducts(userId);
	}

	@PostMapping("/add-product")
	public ResponseEntity<String> addProduct(@RequestBody Product newProduct, @RequestParam String userId) {
		try {

			if (newProduct.getId() == null) {
				newProduct.setId(UUID.randomUUID().toString());
			}
			// Validate the product using your service if needed
			productService.addProduct(newProduct, userId);
			

			// Save the product to the MongoDB repository
			productRepository.save(newProduct);

			return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
		} catch (ProductAlreadyExistsException e) {
			return new ResponseEntity<>("Product already exists", HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/delete-product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable String id, @RequestParam String userId) {
		try {
			productService.deleteProduct(userId, id);
			return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update-product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct, @RequestParam String userId) {
		try {
			productService.updateProduct(id, updatedProduct, userId);
			return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}
	}
}
