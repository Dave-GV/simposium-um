package com.example.simposium.repositories;

import com.example.simposium.models.Ponencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PonenciaRepository extends JpaRepository<Ponencia, Long> {
    // This interface provides CRUD methods automatically
}
