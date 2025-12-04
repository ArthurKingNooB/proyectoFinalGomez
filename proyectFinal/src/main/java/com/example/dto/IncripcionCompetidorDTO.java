package com.example.dto;


import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "DTO de Inscripciones de Competidors a Competencia", requiredProperties = {"CompetidorId","CompetenciaIds"})
public class IncripcionCompetidorDTO {

	@Schema(description = "ID del Competidor", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long CompetidorId;

	@Schema(description = "IDs del Competencia disponibles", requiredMode = Schema.RequiredMode.REQUIRED)
	@ArraySchema(minItems = 1, uniqueItems = true, schema = @Schema(implementation = Long.class, example = "1"))
	private List<Long> CompetenciaIds;

	public Long[] getCompetenciaIds() {
		// TODO Auto-generated method stub
		return null;
	}
}