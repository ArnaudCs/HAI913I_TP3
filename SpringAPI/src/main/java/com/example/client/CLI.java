package com.example.client;

import com.example.demo.api.model.Product;
import com.example.demo.api.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class CLI {

    private static final String BASE_URL = "http://localhost:8080"; // Replace with your actual base URL

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
        	System.out.println("1. Get User");
            System.out.println("2. Get Product");
            System.out.println("3. Add Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Update Product");
            System.out.println("6. List All Users");
            System.out.println("7. List All Products");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
            case 1:
                getUser();
                break;
            case 2:
                getProduct();
                break;
            case 3:
                addProduct();
                break;
            case 4:
                deleteProduct();
                break;
            case 5:
                updateProduct();
                break;
            case 6:
                listAllUsers();
                break;
            case 7:
                listAllProducts();
                break;
            case 0:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                break;
        }
        }
    }

    private static void getUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();

        RestTemplate restTemplate = new RestTemplate();
        User user = restTemplate.getForObject(BASE_URL + "/user?id=" + userId, User.class);

        if (user != null) {
            System.out.println("User details: " + user);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void getProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();

        RestTemplate restTemplate = new RestTemplate();
        Product product = restTemplate.getForObject(BASE_URL + "/product?id=" + productId, Product.class);

        if (product != null) {
            System.out.println("Product details: " + product);
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter product price: ");
        double productPrice = scanner.nextDouble();

        RestTemplate restTemplate = new RestTemplate();
        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setPrice(productPrice);

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/add-product", newProduct, String.class);
        System.out.println(response.getBody());
    }

    private static void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/delete-product/{id}",
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class,
                productId
        );

        System.out.println(response.getBody());
    }

    private static void updateProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to update: ");
        int productId = scanner.nextInt();

        RestTemplate restTemplate = new RestTemplate();
        Product updatedProduct = new Product();
        // Set the updated fields for the product

        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/update-product/{id}",
                org.springframework.http.HttpMethod.PUT,
                null,
                String.class,
                productId,
                updatedProduct
        );

        System.out.println(response.getBody());
    }
    
    private static void listAllUsers() {
        RestTemplate restTemplate = new RestTemplate();
        User[] users = restTemplate.getForObject(BASE_URL + "/all-user", User[].class);

        if (users != null && users.length > 0) {
            System.out.println("All Users:");
            Arrays.stream(users).forEach(System.out::println);
        } else {
            System.out.println("No users found.");
        }
    }

    private static void listAllProducts() {
        RestTemplate restTemplate = new RestTemplate();
        Product[] products = restTemplate.getForObject(BASE_URL + "/all-product", Product[].class);

        if (products != null && products.length > 0) {
            System.out.println("All Products:");
            Arrays.stream(products).forEach(System.out::println);
        } else {
            System.out.println("No products found.");
        }
    }
}
