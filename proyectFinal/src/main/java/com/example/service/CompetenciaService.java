package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.interfaces.CRUDInterface;
import com.example.models.Categoria;
import com.example.models.Competencia;
import com.example.repositories.CategoriaRepository;
import com.example.repositories.CompetenciaRepository;

import jakarta.transaction.Transactional;

@Service
public class CompetenciaService implements CRUDInterface<Competencia, Long> {

	private final String message = "Competencia no encontrado";
	private final String messageCat = "Categoria no encontrado";
	@Autowired
	private CompetenciaRepository repo;
	
	@Autowired
	private CategoriaRepository repoCategoria;
	
	@Override
	public List<Competencia> findAll() {
		return repo.findAll();
	}

	@Override
	public Competencia findById(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(message));
	}

	@Override
	@Transactional
	public Competencia save(Competencia CompetenciaNuevo) {
		if(CompetenciaNuevo.getNombre() != null && repo.existsByNombre(CompetenciaNuevo.getNombre())) {
			throw new IllegalStateException("El Competencia con este Nombre ya existe");
		}
		return repo.save(CompetenciaNuevo);
	}

	@Override
	@Transactional
	public Competencia update(Long id, Competencia CompetenciaActualizado) {
		Competencia Competencia = findById(id);
		
		if(CompetenciaActualizado.getNombre() != null && !CompetenciaActualizado.getNombre().isEmpty()) {
			Competencia.setNombre(CompetenciaActualizado.getNombre());
		}
		
		return repo.save(Competencia);		
	}

	@Override
	public void deleteById(Long id) {
		if(!repo.existsById(id)) {
			throw new IllegalArgumentException(message);
		}
		repo.deleteById(id);
	}
	
	
	@Transactional
	public Competencia asignarCategoriaAUnCompetencia(Long CompetenciaId, Long categoriaId) {
		
		Competencia Competencia = repo.findById(CompetenciaId)
				.orElseThrow(() -> new IllegalArgumentException(message));
		
		Categoria categoria = repoCategoria.findById(categoriaId)
				.orElseThrow(() -> new IllegalArgumentException(messageCat));
		
		if(Competencia.getCategoria() != null && Competencia.getCategoria().getId().equals(categoriaId)) {
			throw new IllegalStateException("El Competencia ya tiene esta categoria asignada");
		}
		
		Competencia.setCategoria(categoria);
		
		return repo.save(Competencia);
	}

}