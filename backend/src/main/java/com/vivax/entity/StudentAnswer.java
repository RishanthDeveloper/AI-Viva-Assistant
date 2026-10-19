package com.vivax.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student_answers")
public class StudentAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private VivaAttempt attempt;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    @Column(columnDefinition = "TEXT")
    private String answerText;
    private String selectedOption;
    
    private boolean isCorrect;
    private double marksObtained;
    
    private LocalDateTime answeredAt = LocalDateTime.now();
}
