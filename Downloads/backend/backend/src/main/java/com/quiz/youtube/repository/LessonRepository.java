package com.quiz.youtube.repository;

import com.quiz.youtube.model.Lesson;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Repository interface for Lesson
 */
public interface LessonRepository {

    /**
     * Create or update a lesson
     *
     * @param lesson the lesson to save
     * @return an Optional containing the saved lesson, or empty if an error occurs
     */
    Optional<Lesson> save(Lesson lesson) throws ExecutionException, InterruptedException;

    /**
     * Query all lessons for a specific subject
     *
     * @param subject the subject to filter lessons
     * @return a list of lessons for the given subject
     */
    List<Lesson> findBySubject(String subject) throws ExecutionException, InterruptedException;

    /**
     * Query lessons for a specific subject and date
     *
     * @param subject the subject to filter lessons
     * @param date the date to filter lessons
     * @return a list of lessons for the given subject and date
     */
    List<Lesson> findBySubjectAndDate(String subject, String date) throws ExecutionException, InterruptedException;
}
