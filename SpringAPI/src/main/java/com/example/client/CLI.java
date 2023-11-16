package com.example.client;

import com.example.demo.api.model.Product;
import com.example.demo.api.model.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
			System.out.println("6. List All Products");
			System.out.println("0. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume the newline

			switch (choice) {
			case 1:
				listAllUsers();
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

	private static void listAllUsers() {
		RestTemplate restTemplate = new RestTemplate();
		User[] users = restTemplate.getForObject(BASE_URL + "/all-users", User[].class);
		Scanner scanner = new Scanner(System.in);

		if (users != null && users.length > 0) {
			System.out.println("All Users:");

			// Display a numbered list of users
			for (int i = 0; i < users.length; i++) {
				System.out.println((i + 1) + ". " + users[i]);
			}

			// Ask for an int corresponding to the user
			System.out.print("Enter user number: ");
			int userNumber = scanner.nextInt();
			scanner.nextLine(); // Consume the newline

			// Ensure the user number is within a valid range
			if (userNumber > 0 && userNumber <= users.length) {
				// Fetch the selected user
				User selectedUser = users[userNumber - 1];
				System.out.println("Selected user details: " + selectedUser);
			} else {
				System.out.println("Invalid user number.");
			}
		} else {
			System.out.println("No users found.");
		}
		
	}

	private static void getProduct() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter product ID: ");
		String productId = scanner.nextLine();

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

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/add-product", newProduct,
				String.class);
		System.out.println(response.getBody());
		
	}

	private static void deleteProduct() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter product ID to delete: ");
		String productId = scanner.nextLine();

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/delete-product/{id}",
				org.springframework.http.HttpMethod.DELETE, null, String.class, productId);

		System.out.println(response.getBody());
		
	}

	private static void updateProduct() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Select a product to update:");

		// Display all products
		List<Product> products = getAllProducts();
		for (int i = 0; i < products.size(); i++) {
			System.out.println((i + 1) + ". " + products.get(i));
		}

		System.out.print("Enter the number of the product to update: ");
		int selectedProductNumber = scanner.nextInt();
		scanner.nextLine(); // Consume the newline

		if (selectedProductNumber > 0 && selectedProductNumber <= products.size()) {
			// User selected a valid product
			Product selectedProduct = products.get(selectedProductNumber - 1);

			System.out.print("Enter updated product name (or press Enter to keep the same): ");
			String updatedProductName = scanner.nextLine();
			if (!updatedProductName.isEmpty()) {
				selectedProduct.setName(updatedProductName);
			}

			System.out.print("Enter updated product price (or enter 0 to keep the same): ");
			double updatedProductPrice = scanner.nextDouble();
			if (updatedProductPrice != 0) {
				selectedProduct.setPrice(updatedProductPrice);
			}

			RestTemplate restTemplate = new RestTemplate();

			// Use the selectedProduct as the request body
			ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/update-product/{id}",
					org.springframework.http.HttpMethod.PUT, new HttpEntity<>(selectedProduct), // Set the request body
					String.class, selectedProduct.getId());

			System.out.println(response.getBody());
		} else {
			System.out.println("Invalid selection.");
		}
		
	}

	private static void listAllProducts() {
		RestTemplate restTemplate = new RestTemplate();
		Product[] products = restTemplate.getForObject(BASE_URL + "/all-products", Product[].class);

		if (products != null && products.length > 0) {
			System.out.println("All Products:");
			Arrays.stream(products).forEach(System.out::println);
		} else {
			System.out.println("No products found.");
		}
	}

	private static List<Product> getAllProducts() {
		RestTemplate restTemplate = new RestTemplate();
		Product[] products = restTemplate.getForObject(BASE_URL + "/all-products", Product[].class);

		return (products != null) ? Arrays.asList(products) : Collections.emptyList();
	}
}
