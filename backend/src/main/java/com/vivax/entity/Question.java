package com.vivax.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String questionText;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    private String questionType; // TEXT, MULTIPLE_CHOICE
    private String difficulty;
    private int marks;
    
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    
    @Column(columnDefinition = "TEXT")
    private String explanation;
}
