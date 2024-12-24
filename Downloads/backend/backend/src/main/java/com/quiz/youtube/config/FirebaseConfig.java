package com.quiz.youtube.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {
    @Bean
    public Firestore firestore() throws IOException {
        try {
            log.info("Starting Firebase initialization...");

            // Get Firebase config from environment variable
            String firebaseConfigPath = System.getenv("FIREBASE_CONFIG");
            log.info("Firebase config path: {}", firebaseConfigPath);

            GoogleCredentials credentials;
            if (firebaseConfigPath != null) {
                // Production: Read from mounted secret
                credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseConfigPath));
                log.info("Loaded credentials from environment variable path");
            } else {
                // Local development: Read from classpath
                ClassPathResource resource = new ClassPathResource("admin.json");
                log.info("admin.json exists: {}", resource.exists());
                credentials = GoogleCredentials.fromStream(resource.getInputStream());
                log.info("Loaded credentials from classpath");
            }

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized successfully");
            } else {
                log.info("Firebase already initialized");
            }

            Firestore db = FirestoreClient.getFirestore();
            log.info("Firestore instance created");
            return db;

        } catch (Exception e) {
            log.error("Error initializing Firebase: ", e);
            throw e;
        }
    }
}