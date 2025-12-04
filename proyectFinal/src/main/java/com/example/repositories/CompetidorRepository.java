package com.example.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Competidor;

public interface CompetidorRepository extends JpaRepository<Competidor, Long> {

	boolean existsByDni(int dni);
	
	boolean existsByLegajo(String legajo);
}