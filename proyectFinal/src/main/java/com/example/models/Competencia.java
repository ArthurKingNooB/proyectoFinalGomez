package com.example.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Schema(description = "Modelo de Competencia")
@Table(name = "Competencias")
public class Competencia {

	@Schema(description = "ID del Competencia", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
	@Id // Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Schema(description = "Nombre del Competencia", requiredMode = Schema.RequiredMode.REQUIRED, example = "Java")
	@Column(name = "Nombre", nullable = false)
	private String nombre;
	
	@Schema(description = "Lista de Competidor que tienen asignado este Competencia")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Competencia_Competidor", 
		joinColumns = @JoinColumn(name = "Competencia_id"), 
		inverseJoinColumns = @JoinColumn(name = "Competidor_id"))
	@JsonIgnore
	private List<Competidor> Competidor = new ArrayList<>();
	
	@Schema(description = "Categoria del Competencia")
	@ManyToOne(fetch = FetchType.EAGER)
	private Categoria categoria;

}