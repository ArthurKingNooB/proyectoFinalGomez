package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.IncripcionCompetidorDTO;
import com.example.interfaces.CRUDInterface;
import com.example.models.Competidor;
import com.example.models.Competencia;
import com.example.repositories.CompetidorRepository;
import com.example.repositories.CompetenciaRepository;

import jakarta.transaction.Transactional;

@Service
public class CompetidorService implements CRUDInterface<Competidor, Long> {
	
	private final String message = "Competidor no encontrado";
	
	@Autowired
	private CompetidorRepository repo;
	@Autowired
	private CompetenciaRepository CompetenciaRepo;
	
	@Override
	public List<Competidor> findAll() {
		return repo.findAll();
	}

	@Override
	public Competidor findById(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(message));
	}

	@Override
	@Transactional
	public Competidor save(Competidor nuevoCompetidor) {
		if(nuevoCompetidor.getDni() != 0 && repo.existsByDni(nuevoCompetidor.getDni())) {
			throw new IllegalStateException("Este DNI ya existe");
		}
		
		if(nuevoCompetidor.getLegajo() != null && !nuevoCompetidor.getLegajo().isEmpty()
				&& repo.existsByLegajo(nuevoCompetidor.getLegajo())) {
			throw new IllegalStateException("Este Legajo ya existe");
		}
		return repo.save(nuevoCompetidor);
	}

	@Override
	@Transactional
	public Competidor update(Long id, Competidor CompetidorActualizado) {
		Competidor Competidor = findById(id);
		
		if(CompetidorActualizado.getNombre() != null && !CompetidorActualizado.getNombre().isEmpty()) {
			Competidor.setNombre(CompetidorActualizado.getNombre());
		}
		
		if(CompetidorActualizado.getApellido() != null && !CompetidorActualizado.getApellido().isEmpty()) {
			Competidor.setApellido(CompetidorActualizado.getApellido());
		}
		
		if(CompetidorActualizado.getDni() != 0) {
			Competidor.setDni(CompetidorActualizado.getDni());
		}
		
		if(CompetidorActualizado.getEdad() != 0) {
			Competidor.setEdad(CompetidorActualizado.getEdad());
		}
		
		if(CompetidorActualizado.getLegajo() != null && !CompetidorActualizado.getLegajo().isEmpty()) {
			Competidor.setLegajo(CompetidorActualizado.getLegajo());
		}
				
		return repo.save(Competidor);
	}

	@Override
	public void deleteById(Long id) {
		if(!repo.existsById(id)) {
			throw new IllegalArgumentException(message);
		}
		repo.deleteById(id);
	}

	
	@Transactional
	public Competidor inscribirCompetidorACompetencias(IncripcionCompetidorDTO dto) {
		Competidor Competidor = findById(dto.getCompetidorId());
		
		for(Long CompetenciaId : dto.getCompetenciaIds()) {
			
			Competencia Competencia = CompetenciaRepo.findById(CompetenciaId)
					.orElseThrow(() -> new IllegalArgumentException("El Competencia con ID " + CompetenciaId + ", no existe"));
			
			// Verificar si el Competidor esta inscripto a este Competencia
			if(Competidor.getCompetencias().contains(Competencia)) {
				throw new IllegalStateException("El Competidor ya esta inscripto a este Competencia co ID: " + CompetenciaId);
			}
			
			Competidor.getCompetencias().add(Competencia);
			Competencia.getCompetidor().add(Competidor);
			
			CompetenciaRepo.save(Competencia);
		}
			
		
		return repo.save(Competidor);
	}
}