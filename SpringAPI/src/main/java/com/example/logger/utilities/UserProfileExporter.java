package com.example.logger.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserProfileExporter {

    public static void exportReadPriority(List<UserProfile> userProfiles, String filePath) {
        List<UserProfile> filteredProfiles = filterUserProfilesByReadPriority(userProfiles);
        exportToFile(filteredProfiles, filePath);
    }

    public static void exportWritePriority(List<UserProfile> userProfiles, String filePath) {
        List<UserProfile> filteredProfiles = filterUserProfilesByWritePriority(userProfiles);
        exportToFile(filteredProfiles, filePath);
    }

    public static void exportRankedProfiles(List<UserProfile> userProfiles, String filePath) {
        List<UserProfile> sortedProfiles = sortUserProfilesByTotalRequests(userProfiles);
        exportToFile(sortedProfiles, filePath);
    }

    private static List<UserProfile> filterUserProfilesByReadPriority(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .filter(up -> up.getReadOperations().size() > up.getWriteOperations().size())
                .collect(java.util.stream.Collectors.toList());
    }

    private static List<UserProfile> filterUserProfilesByWritePriority(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .filter(up -> up.getWriteOperations().size() > up.getReadOperations().size())
                .collect(java.util.stream.Collectors.toList());
    }

    private static List<UserProfile> sortUserProfilesByTotalRequests(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .sorted(Comparator.comparingInt(UserProfile::getTotalRequests).reversed())
                .collect(java.util.stream.Collectors.toList());
    }

    private static void exportToFile(List<UserProfile> userProfiles, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (UserProfile userProfile : userProfiles) {
                writer.write(userProfile.toString() + "\n");
            }
            System.out.println("Exported to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
