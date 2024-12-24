package com.quiz.youtube.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date; // Store as String for Firestore compatibility

    private Date createdAt; // Full timestamp
    private Date updatedAt; // Full timestamp
    private String subject;
    private String topic;
    private String youtubeLink;
    private List<Question> questions;
}
