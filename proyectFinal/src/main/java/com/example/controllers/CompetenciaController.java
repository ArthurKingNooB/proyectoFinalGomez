package com.example.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AsignacionDeCategoriaACompetenciaDTO;
import com.example.models.Competencia;
import com.example.responses.ErrorResponse;
import com.example.service.CompetenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/Competencias")
@Tag(name = "Gestion de Competencias", description = "Endpoints para gestionar Competencias")
public class CompetenciaController {

	@Autowired
	private CompetenciaService svc;
	
	@Operation(summary = "Obtener la lista de Todos los Competencias")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de Competencias obtenida correctamente", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Competencia.class)))}),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping
	public ResponseEntity<?> getAllCompetencias(){
		try {
			List<Competencia> Competencias = svc.findAll();
			return ResponseEntity.ok(Competencias); // Code 200
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse("Error de Servidor", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}
	
	@Operation(summary = "Obtener un Competencia por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Competencia encontrado correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competencia.class))}),
			@ApiResponse(responseCode = "404", description = "Error al Obtener el Competencia", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/{CompetenciaId}")
	public ResponseEntity<?> getCompetenciaById(
			@Parameter(description = "Identificador del Competencia", example = "1", required = true)
			@PathVariable Long CompetenciaId) {
		try {		
			Competencia Competencia = svc.findById(CompetenciaId);
			return ResponseEntity.ok(Competencia); // 200
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404	
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse("Error de Servidor", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500	
		}
	}
	
	@Operation(summary = "Crear un Competencia")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Competencia creado correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competencia.class))}),
			@ApiResponse(responseCode = "409", description = "Error al intentar crear el Competencia - CONFLICT", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping("/create")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del Competencia a crear",
			required = true,
			content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
							name = "Competencia Backend",
							value = "{\"nombre\":\"Java Backend\"}"
					),
					schema = @Schema(implementation = Competencia.class)
			)
	)
	public ResponseEntity<?> createCompetencia(@RequestBody Competencia Competencia) {
		try {
			Competencia CompetenciaCreado = svc.save(Competencia);
			return ResponseEntity.status(HttpStatus.CREATED).body(CompetenciaCreado); // 201
		} catch (IllegalStateException e) {
			ErrorResponse error = new ErrorResponse("Conflicto", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(error);	// 409	
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse("Error de Servidor", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500	
		}
	}
	
	@Operation(summary = "Actualizar un Competencia por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Competencia actualizado correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competencia.class))}),
			@ApiResponse(responseCode = "404", description = "Error al obtener el Competencia", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Error al intentar actualizar el Competencia - CONFLICT", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PutMapping("/{CompetenciaId}")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del Competencia a actualizar",
			required = true,
			content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
							name = "Actualizacion de Competencia",
							value = "{\"nombre\":\"Java Avanzado\"}"
					),
					schema = @Schema(implementation = Competencia.class)
			)
	)
	public ResponseEntity<?> updateCompetenciaById(
			@Parameter(description = "Identificador del Competencia", example = "1", required = true)
			@PathVariable Long CompetenciaId,
			@RequestBody Competencia CompetenciaActualizado){
		try {
			Competencia Competencia = svc.update(CompetenciaId, CompetenciaActualizado);
			return ResponseEntity.ok(Competencia); // 200
		} catch (IllegalStateException e) {
			ErrorResponse error = new ErrorResponse("Conflicto", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(error);	// 409	
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404	
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse("Error de Servidor", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500	
		}
	}
	
	@Operation(summary = "Eliminar un Competencia por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Competencia eliminado correctamente", content = {
					@Content()}),
			@ApiResponse(responseCode = "404", description = "Error al obtener el Competencia", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@DeleteMapping("/{CompetenciaId}")
	public ResponseEntity<?> deleteCompetenciaById(
			@Parameter(description = "Identificador del Competencia", example = "1", required = true)
			@PathVariable Long CompetenciaId){
		try {
			svc.deleteById(CompetenciaId);
			return ResponseEntity.noContent().build(); // 204
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404	
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse("Error de Servidor", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500	
		}
	}
	
	
	@Operation(summary = "Asignar una Categoria a un Competencia")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Competencia asociado correctamente a la Categoria", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competencia.class))}),
			@ApiResponse(responseCode = "400", description = "Par\u00e1metros incompletos", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Error al obtener el Competencia o Categoria", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Error al intentar asociar al Competencia, hay un conflicto con los datos", 
				content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping("/asignar-categoria")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Identificadores necesarios para relacionar un Competencia con una categoria",
			required = true,
			content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(value = "{\"CompetenciaId\":1,\"categoriaId\":2}"),
					schema = @Schema(implementation = AsignacionDeCategoriaACompetenciaDTO.class)
			)
	)
	public ResponseEntity<?> asignarCategoriaACompetencia(@RequestBody AsignacionDeCategoriaACompetenciaDTO dto){
		if(dto.getCompetenciaId() == null || dto.getCategoriaId() == null) {
			ErrorResponse error = new ErrorResponse("Solicitud invalida", "El parametro ID no puede ser null");
			return ResponseEntity.badRequest().body(error);
		}		
		try {
			Competencia CompetenciaActualizado = svc.asignarCategoriaAUnCompetencia(
					dto.getCompetenciaId(),
					dto.getCategoriaId()
				);
			return ResponseEntity.ok(CompetenciaActualizado); // 200
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404
		} catch (IllegalStateException e) {
			ErrorResponse error = new ErrorResponse("Conflicto", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(error);	// 409			
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse("Error de Servidor", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500	
		}
	}
	
}