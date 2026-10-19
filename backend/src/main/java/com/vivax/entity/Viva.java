package com.vivax.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vivas")
public class Viva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private int questionCount;
    private String difficulty;
    private String instructions;
    private String status; // DRAFT, PUBLISHED, CANCELLED
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
