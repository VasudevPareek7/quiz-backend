package com.quiz.youtube.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.quiz.youtube.model.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class FirebaseLessonRepository implements LessonRepository {

    private final Firestore firestoreDB;
    private static final String LESSONS_COLLECTION = "lessons";

    public FirebaseLessonRepository(Firestore firestoreDB) {
        this.firestoreDB = firestoreDB;
    }

    /**
     * Save or update a lesson.
     */
    @Override
    public Optional<Lesson> save(Lesson lesson) throws ExecutionException, InterruptedException {
        try {
            DocumentReference documentReference;

            if (lesson.getId() == null) {
                // Create a new lesson
                documentReference = firestoreDB.collection(LESSONS_COLLECTION).document();
                lesson.setId(documentReference.getId());
                lesson.setCreatedAt(new Date());
                log.info("Creating new lesson with ID: {}", lesson.getId());
            } else {
                // Update existing lesson
                documentReference = firestoreDB.collection(LESSONS_COLLECTION).document(lesson.getId());
                log.info("Updating lesson with ID: {}", lesson.getId());
            }

            lesson.setUpdatedAt(new Date());

            ApiFuture<WriteResult> writeResult = documentReference.set(lesson, SetOptions.merge());
            writeResult.get(); // Wait for Firestore to complete the operation
            log.info("Successfully saved lesson with ID: {}", lesson.getId());
            return Optional.of(lesson);
        } catch (Exception e) {
            log.error("Error saving lesson: {}", lesson, e);
            return Optional.empty();
        }
    }

    /**
     * Query lessons for a specific subject.
     */
    @Override
    public List<Lesson> findBySubject(String subject) throws ExecutionException, InterruptedException {
        if (subject == null || subject.trim().isEmpty()) {
            log.warn("Subject cannot be null or empty");
            return new ArrayList<>();
        }

        log.info("Fetching lessons for subject: {}", subject);
        Query query = firestoreDB.collection(LESSONS_COLLECTION)
                .whereEqualTo("subject", subject)
                .orderBy("date", Query.Direction.DESCENDING); // Sorting by date in descending order

        return executeQuery(query);
    }

    /**
     * Query lessons for a subject on a specific date.
     */
    @Override
    public List<Lesson> findBySubjectAndDate(String subject, String date) throws ExecutionException, InterruptedException {
        if (subject == null || subject.trim().isEmpty() || date == null || date.trim().isEmpty()) {
            log.warn("Subject and date cannot be null or empty");
            return new ArrayList<>();
        }

        log.info("Fetching lessons for subject={} on date={}", subject, date);
        Query query = firestoreDB.collection(LESSONS_COLLECTION)
                .whereEqualTo("subject", subject)
                .whereEqualTo("date", date); // Query by date as a string

        return executeQuery(query);
    }

    /**
     * Helper method to execute a Firestore query and convert results to a list of lessons.
     */
    private List<Lesson> executeQuery(Query query) throws ExecutionException, InterruptedException {
        log.debug("Executing Firestore query: {}", query);
        ApiFuture<QuerySnapshot> futureQuery = query.get();
        List<QueryDocumentSnapshot> documents = futureQuery.get().getDocuments();

        List<Lesson> lessons = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Lesson lesson = document.toObject(Lesson.class);
            if (lesson != null) {
                lesson.setId(document.getId());
                lessons.add(lesson);
            }
        }
        return lessons;
    }
}
