package com.example.logger.utilities;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private String userId;
    private List<Event> readOperations;
    private List<Event> writeOperations;
    
    
    
	public UserProfile(String userId, List<Event> readOperations, List<Event> writeOperations) {
		super();
		this.userId = userId;
		this.readOperations = readOperations;
		this.writeOperations = writeOperations;
	}
	
	
	
	public UserProfile() {
		this.userId = "";
		this.readOperations = new ArrayList<>();
		this.writeOperations = new ArrayList<>();
	}



	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Event> getReadOperations() {
		return readOperations;
	}
	public void setReadOperations(List<Event> readOperations) {
		this.readOperations = readOperations;
	}
	public List<Event> getWriteOperations() {
		return writeOperations;
	}
	public void setWriteOperations(List<Event> writeOperations) {
		this.writeOperations = writeOperations;
	}
	
    public int getTotalRequests() {
        return readOperations.size() + writeOperations.size();
    }
	@Override
	public String toString() {
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append("UserProfile [userId=").append(userId).append("]\n");
	    
	    stringBuilder.append("Read Operations:\n");
	    for (Event readEvent : readOperations) {
	        stringBuilder.append("\t").append(readEvent).append("\n");
	    }
	    
	    stringBuilder.append("Write Operations:\n");
	    for (Event writeEvent : writeOperations) {
	        stringBuilder.append("\t").append(writeEvent).append("\n");
	    }

	    return stringBuilder.toString();
	}

	
}