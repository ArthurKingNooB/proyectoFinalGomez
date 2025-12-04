package com.example.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Competencia;

public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

	boolean existsByNombre(String nombre);
}