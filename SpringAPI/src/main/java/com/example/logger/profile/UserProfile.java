package com.example.logger.profile;

import java.util.List;

public class UserProfile {

    private String userId;
    private List<String> readOperations;
    private List<String> writeOperations;
    private List<String> searchedExpensiveProducts;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getReadOperations() {
		return readOperations;
	}
	public void setReadOperations(List<String> readOperations) {
		this.readOperations = readOperations;
	}
	public List<String> getWriteOperations() {
		return writeOperations;
	}
	public void setWriteOperations(List<String> writeOperations) {
		this.writeOperations = writeOperations;
	}
	public List<String> getSearchedExpensiveProducts() {
		return searchedExpensiveProducts;
	}
	public void setSearchedExpensiveProducts(List<String> searchedExpensiveProducts) {
		this.searchedExpensiveProducts = searchedExpensiveProducts;
	}

	@Override
    public String toString() {
        // Implement the toString method for logging purposes
        // You can customize this based on your requirements
        return "UserProfile{" +
               "userId='" + userId + '\'' +
               ", readOperations=" + readOperations +
               ", writeOperations=" + writeOperations +
               ", searchedExpensiveProducts=" + searchedExpensiveProducts +
               '}';
    }
}