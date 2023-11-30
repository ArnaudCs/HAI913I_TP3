package com.example.client;

import com.example.demo.api.model.Product;
import com.example.demo.api.model.User;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CLI {

	private static final String BASE_URL = "http://localhost:8080"; // Remplacez par votre URL de base réelle
	private static String selectedUserId;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Sélection de l'utilisateur au démarrage
		selectUser(scanner);

		while (true) {
			System.out.println("1. Get Product");
			System.out.println("2. Add Product");
			System.out.println("3. Delete Product");
			System.out.println("4. Update Product");
			System.out.println("5. List All Products");
			System.out.println("6. Get raw logs");
			System.out.println("7. Get user with more READ op");
			System.out.println("8. Get user with more WRITE op");
			System.out.println("9. Get user ranked by op number");
			System.out.println("0. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // Consommer la nouvelle ligne

			switch (choice) {
			case 1:
				getProduct();
				break;
			case 2:
				addProduct();
				break;
			case 3:
				deleteProduct();
				break;
			case 4:
				updateProduct();
				break;
			case 5:
				listAllProducts();
				break;
			case 6:
				getLogs("all");
				break;
			case 7:
				getLogs("read");
				break;
			case 8:
				getLogs("write");
				break;
			case 9:
				getLogs("ranked");
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

	private static void selectUser(Scanner scanner) {
		RestTemplate restTemplate = new RestTemplate();
		User[] users = restTemplate.getForObject(BASE_URL + "/all-users", User[].class);

		if (users != null && users.length > 0) {
			System.out.println("Select a user:");

			// Afficher une liste numérotée des utilisateurs
			for (int i = 0; i < users.length; i++) {
				System.out.println((i + 1) + ". " + users[i]);
			}

			// Demander un entier correspondant à l'utilisateur
			System.out.print("Enter user number: ");
			int userNumber = scanner.nextInt();
			scanner.nextLine(); // Consommer la nouvelle ligne

			// Assurer que le numéro d'utilisateur est dans une plage valide
			if (userNumber > 0 && userNumber <= users.length) {
				// Récupérer l'utilisateur sélectionné
				User selectedUser = users[userNumber - 1];
				selectedUserId = selectedUser.getId();
				System.out.println("Selected user details: " + selectedUser);
			} else {
				System.out.println("Invalid user number. Exiting...");
				System.exit(0);
			}
		} else {
			System.out.println("No users found. Exiting...");
			System.exit(0);
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
		Product product = restTemplate.getForObject(BASE_URL + "/product?id=" + productId + "&userId=" + selectedUserId,
				Product.class);

		if (product != null) {
			System.out.println("Product details: " + product);
		} else {
			System.out.println("Product not found.");
		}
	}

	private static void addProduct() {
		// Utilisez selectedUserId pour inclure l'identifiant de l'utilisateur dans la
		// requête
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter product name: ");
		String productName = scanner.nextLine();
		System.out.print("Enter product price: ");
		double productPrice = scanner.nextDouble();

		RestTemplate restTemplate = new RestTemplate();
		Product newProduct = new Product();
		newProduct.setName(productName);
		newProduct.setPrice(productPrice);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/add-product?userId=" + selectedUserId,
				newProduct, String.class);
		System.out.println(response.getBody());
	}

	private static void deleteProduct() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter product ID to delete: ");
		String productId = scanner.nextLine();

		RestTemplate restTemplate = new RestTemplate();
		// Utilisez selectedUserId comme paramètre dans l'URL
		ResponseEntity<String> response = restTemplate.exchange(
				BASE_URL + "/delete-product/{id}?userId=" + selectedUserId, org.springframework.http.HttpMethod.DELETE,
				null, String.class, productId);

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

			// Utilisez selectedUserId comme paramètre dans l'URL
			ResponseEntity<String> response = restTemplate.exchange(
					BASE_URL + "/update-product/{id}?userId=" + selectedUserId, org.springframework.http.HttpMethod.PUT,
					new HttpEntity<>(selectedProduct), // Set the request body
					String.class, selectedProduct.getId());

			System.out.println(response.getBody());
		} else {
			System.out.println("Invalid selection.");
		}
	}

	private static void listAllProducts() {
		RestTemplate restTemplate = new RestTemplate();
		// Utilisez selectedUserId comme paramètre dans l'URL
		Product[] products = restTemplate.getForObject(BASE_URL + "/all-products?userId=" + selectedUserId,
				Product[].class);

		if (products != null && products.length > 0) {
			System.out.println("All Products:");
			Arrays.stream(products).forEach(System.out::println);
		} else {
			System.out.println("No products found.");
		}
	}

	private static List<Product> getAllProducts() {
		RestTemplate restTemplate = new RestTemplate();
		Product[] products = restTemplate.getForObject(BASE_URL + "/all-products?userId=" + selectedUserId,
				Product[].class);

		return (products != null) ? Arrays.asList(products) : Collections.emptyList();
	}

	private static void getLogs(String type) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Resource> response = restTemplate.getForEntity(BASE_URL + "/api/logs/" + type, Resource.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				// Lire le contenu du fichier de log depuis le Resource
				byte[] contentBytes = new byte[response.getBody().getInputStream().available()];
				response.getBody().getInputStream().read(contentBytes);

				// Convertir le contenu en chaîne et l'afficher
				String content = new String(contentBytes);
				System.out.println(content);
			} catch (IOException e) {
				System.err.println("Error reading log content: " + e.getMessage());
			}
		} else {
			System.out.println("Failed to retrieve logs. Status code: " + response.getStatusCodeValue());
		}
	}
}
