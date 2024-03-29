package com.example.logger.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private LocalDateTime timestamp;
    private String userId;
    private String type;
    private String action;
    private String productId;

    // Constructeur
    public Event(LocalDateTime timestamp, String userId, String type, String action, String productId) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.type = type;
        this.action = action;
        this.productId = productId;
    }

    public Event(LocalDateTime timestamp, String userId, String type, String action) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.type = type;
        this.action = action;
    }

    // Getters et Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // Méthode pour afficher l'événement sous forme de chaîne
    @Override
    public String toString() {
        return timestamp.format(DATE_TIME_FORMATTER) + " | " + userId + " | " + type + " | " + action +
                (productId != null && !productId.isEmpty() ? " | " + productId : "");
    }
}
