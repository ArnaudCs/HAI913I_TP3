package com.example.logger.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogParser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static List<UserProfile> parseLogs(Path logFilePath) {
        try {
            List<String> logLines = Files.readAllLines(logFilePath);
            return parseLogLines(logLines);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<UserProfile> parseLogLines(List<String> logLines) {
        List<UserProfile> userProfiles = new ArrayList<>();
        UserProfile currentUserProfile = null;

        for (String logLine : logLines) {
            String[] parts = logLine.split("\\|");

            if (parts.length >= 4 && parts.length <= 5) {
                LocalDateTime timestamp = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
                String userId = parts[1].trim();
                String type = parts[2].trim();
                String action = parts[3].trim();
                String productId = (parts.length == 5) ? parts[4].trim() : null;

                if (currentUserProfile == null || !currentUserProfile.getUserId().equals(userId)) {
                    // Nouvel utilisateur
                    currentUserProfile = new UserProfile();
                    currentUserProfile.setUserId(userId);
                    userProfiles.add(currentUserProfile);
                }

                Event event = new Event(timestamp, userId, type, action, productId);

                if ("READ".equals(type)) {
                    currentUserProfile.getReadOperations().add(event);
                } else if ("WRITE".equals(type)) {
                    currentUserProfile.getWriteOperations().add(event);
                }
            } else {
                System.err.println("Skipping invalid log entry: " + logLine);
            }
        }

        return userProfiles;
    }
    
    public static void userProfilesExporter(List<UserProfile> userProfiles) {
    	UserProfileExporter.exportReadPriority(userProfiles, "logs/read_priority.txt");
		UserProfileExporter.exportWritePriority(userProfiles, "logs/write_priority.txt");
		UserProfileExporter.exportRankedProfiles(userProfiles, "logs/ranked_profiles.txt");
    }

    public static void main(String[] args) {
        // Example usage
        Path logFilePath = Path.of("logs/application.log");
        List<UserProfile> userProfiles = parseLogs(logFilePath);

        for (UserProfile userProfile : userProfiles) {
            System.out.println(userProfile);
        }
		UserProfileExporter.exportReadPriority(userProfiles, "logs/read_priority.txt");
		UserProfileExporter.exportWritePriority(userProfiles, "logs/write_priority.txt");
		UserProfileExporter.exportRankedProfiles(userProfiles, "logs/ranked_profiles.txt");

    }
}

