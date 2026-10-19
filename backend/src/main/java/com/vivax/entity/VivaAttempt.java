package com.vivax.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viva_attempts")
public class VivaAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "viva_id")
    private Viva viva;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private String status; // IN_PROGRESS, SUBMITTED, AUTO_SUBMITTED
    private double score;
    private double percentage;
}
