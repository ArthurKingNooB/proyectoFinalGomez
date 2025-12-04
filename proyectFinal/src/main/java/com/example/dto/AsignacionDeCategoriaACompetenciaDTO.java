package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO de Asignaci\u00f3n de Categoria a Competencias", requiredProperties = {"CompetenciaId","categoriaId"})
public class AsignacionDeCategoriaACompetenciaDTO {
	@Schema(description = "ID del Competencia", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long CompetenciaId;
	@Schema(description = "ID del Categoria", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long categoriaId;
}