package com.vivax.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "viva_questions")
public class VivaQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "viva_id")
    private Viva viva;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
