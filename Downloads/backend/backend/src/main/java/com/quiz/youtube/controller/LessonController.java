package com.quiz.youtube.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import com.quiz.youtube.model.Lesson;
import com.quiz.youtube.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LessonController {

    private final LessonService lessonService;

    /**
     * Create or update a lesson
     */
    @PostMapping
    @CacheEvict(value = {"lessonsBySubject", "lessonsBySubjectAndDate"}, allEntries = true)
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody Lesson lesson) {
        log.info("Creating new lesson: subject={}, topic={}", lesson.getSubject(), lesson.getTopic());
        try {
            Lesson createdLesson = lessonService.createOrUpdateLesson(lesson);
            log.info("Successfully created lesson with ID: {}", createdLesson.getId());
            return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create lesson: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all lessons by subject
     */
    @GetMapping("/subject/{subject}")
    @Cacheable(value = "lessonsBySubject", key = "#subject")
    public ResponseEntity<List<Lesson>> getLessonsBySubject(@PathVariable String subject) {
        log.info("Fetching lessons for subject: {}", subject);
        try {
            List<Lesson> lessons = lessonService.getLessonsBySubject(subject);
            log.info("Found {} lessons for subject: {}", lessons.size(), subject);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            log.error("Error fetching lessons for subject: {}", subject, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get lessons by subject and date
     */
    @GetMapping("/subject/{subject}/date")
    @Cacheable(value = "lessonsBySubjectAndDate", key = "#subject + '-' + #date")
    public ResponseEntity<List<Lesson>> getLessonsBySubjectAndDate(
            @PathVariable String subject,
            @RequestParam String date) { // Expecting date as a String in "YYYY-MM-DD" format
        log.info("Fetching lessons for subject={} on date={}", subject, date);
        try {
            List<Lesson> lessons = lessonService.getLessonsBySubjectAndDate(subject, date);
            log.info("Found {} lessons for subject={} on date={}", lessons.size(), subject, date);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            log.error("Error fetching lessons for subject={} on date={}", subject, date, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
