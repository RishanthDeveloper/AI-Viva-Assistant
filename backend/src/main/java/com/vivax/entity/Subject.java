package com.vivax.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subjects")
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
}
