package com.example.demo.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.logger.utilities.LogParser;
import com.example.logger.utilities.UserProfile;

@Service
public class LogService {
	
	private static final String PATH = "logs";

    public String getReadPriorityLogs() {
        return getLogs(PATH + "/read_priority.txt");
    }

    public String getWritePriorityLogs() {
        return getLogs(PATH + "/write_priority.txt");
    }

    public String getRankedProfilesLogs() {
        return getLogs(PATH + "/ranked_profiles.txt");
    }

    public String getAllLogs() {
        return getLogs(PATH + "/application.log");
    }

    private String getLogs(String filename) {
        try {
            Resource logFile = new FileSystemResource(filename);

            if (!logFile.exists() || isLogFileStale(logFile)) {
                // Le fichier n'existe pas ou est obsolète, le recréer et refaire le parsing
                List<UserProfile> userProfiles = LogParser.parseLogs(Path.of(PATH + "/application.log"));
                LogParser.userProfilesExporter(userProfiles);
            }

            // Récupérer directement le contenu du fichier
            return FileCopyUtils.copyToString(new InputStreamReader(logFile.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading logs.";
        }
    }





    private boolean isLogFileStale(Resource logFile) {
        try {
            Instant lastModifiedInstant = Instant.ofEpochMilli(logFile.lastModified());
            LocalDateTime lastModified = LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
            LocalDateTime now = LocalDateTime.now();
            return Duration.between(lastModified, now).toMinutes() > 2;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }
}
