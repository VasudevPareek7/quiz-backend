package com.quiz.youtube.model;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String id;
    private String text;
    private QuestionType type;
    private List<String> options;      // For MCQ
    private Integer correctOptionIndex; // Index of correct option for MCQ (0-based)
    private String correctAnswer;      // For non-MCQ questions
    private Integer points;
    private String hint;
    private String explanation;
}

enum QuestionType {
    MULTIPLE_CHOICE,
    OPEN_ENDED,
    TRUE_FALSE,
    SHORT_ANSWER
}