package com.example.simposium.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// http://localhost:8080/h2-console 
@Entity
@Table(name = "ponencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ponencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
