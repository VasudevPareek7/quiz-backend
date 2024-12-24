package com.quiz.youtube.service;

import com.quiz.youtube.model.Lesson;
import com.quiz.youtube.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    /**
     * Create or update a lesson
     */
    public Lesson createOrUpdateLesson(Lesson lesson) throws ExecutionException, InterruptedException {
        return lessonRepository.save(lesson)
                .orElseThrow(() -> new RuntimeException("Failed to save lesson"));
    }

    /**
     * Get all lessons for a subject
     */
    public List<Lesson> getLessonsBySubject(String subject) throws ExecutionException, InterruptedException {
        return lessonRepository.findBySubject(subject);
    }

    /**
     * Get lessons for a specific subject and date
     */
    public List<Lesson> getLessonsBySubjectAndDate(String subject, String date) throws ExecutionException, InterruptedException {
        return lessonRepository.findBySubjectAndDate(subject, date);
    }
}
