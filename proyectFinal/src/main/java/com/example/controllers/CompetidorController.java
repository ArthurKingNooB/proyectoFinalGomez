package com.example.controllers;





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

import com.example.dto.IncripcionCompetidorDTO;
import com.example.models.Competidor;
import com.example.responses.ErrorResponse;
import com.example.service.CompetidorService;

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
@RequestMapping("/api/Competidor")
@Tag(name = "Gestion de Competidor", description = "Endpoints para gestionar Competidor")
public class CompetidorController {

	@Autowired
	private CompetidorService CompetidorService;

	@Operation(summary = "Obtener la lista de Todos los Competidor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de Competidor obtenida correctamente", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Competidor.class)))}),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping
	public ResponseEntity<?> getAllCompetidor() {
		try {
			java.util.List<Competidor> Competidor = CompetidorService.findAll();
			return ResponseEntity.ok(Competidor); // Code 200
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(e.getMessage(),"Error Interno del Servido   r");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}

	@Operation(summary = "Obtener un Competidor por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Competidor encontrado correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competidor.class))}),
			@ApiResponse(responseCode = "404", description = "Error al Obtener el Competidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/{CompetidorId}")
	public ResponseEntity<?> getCompetidorById(
			@Parameter(description = "Identificador del Competidor", example = "5", required = true)
			@PathVariable Long CompetidorId) {
		try {
			Competidor Competidor = CompetidorService.findById(CompetidorId);
			return ResponseEntity.ok(Competidor); // 200
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(e.getMessage(),"Error Interno del Servidor");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}

	@Operation(summary = "Crear un Competidor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Competidor creado correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competidor.class))}),
			@ApiResponse(responseCode = "409", description = "Error al intentar crear el Competidor - CONFLICT", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping("/create")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del Competidor a crear",
			required = true,
			content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
							name = "Competidor inicial",
							value = "{\"nombre\":\"ana\",\"apellido\":\"perez\",\"dni\":3344323455,\"legajo\":\"L333424555\"}"
					),
					schema = @Schema(implementation = Competidor.class)
			)
	)
	public ResponseEntity<?> createCompetidor(@RequestBody Competidor Competidor) {
		try {
			Competidor CompetidorCreado = CompetidorService.save(Competidor);
			return ResponseEntity.status(HttpStatus.CREATED).body(CompetidorCreado); // 201
		} catch (IllegalStateException e) {
			ErrorResponse error = new ErrorResponse("Conflicto", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(error);	// 409	
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(e.getMessage(),"Error Interno del Servidor");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}

	@Operation(summary = "Actualizar un Competidor por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Competidor actualizado correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competidor.class))}),
			@ApiResponse(responseCode = "404", description = "Error al actualizar el Competidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Error al actualizar el Competidor, conflicto de Datos", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PutMapping("/{CompetidorId}")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del Competidor a actualizar",
			required = true,
			content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
							name = "Actualizaci\u00f3n de Competidor",
							value = "{\"nombre\":\"Ana\",\"apellido\":\"Perez\",\"dni\":11222333}"
					),
					schema = @Schema(implementation = Competidor.class)
			)
	)
	public ResponseEntity<?> updateCompetidorById(
			@Parameter(description = "Identificador del Competidor", example = "5", required = true)
			@PathVariable Long CompetidorId,
			@RequestBody Competidor CompetidorActualizado) {
		try {
			Competidor Competidor = CompetidorService.update(CompetidorId, CompetidorActualizado);
			return ResponseEntity.ok(Competidor); // 200
		} catch (IllegalStateException e) {
			ErrorResponse error = new ErrorResponse("Conflicto", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(error);	// 409
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(e.getMessage(),"Error Interno del Servidor");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}

	@Operation(summary = "Eliminar un Competidor por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Competidor eliminado correctamente", content = {
					@Content()}),
			@ApiResponse(responseCode = "404", description = "Error al obtener el Competidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@DeleteMapping("/{CompetidorId}")
	public ResponseEntity<?> deleteCompetidorById(
			@Parameter(description = "Identificador del Competidor", example = "5", required = true)
			@PathVariable Long CompetidorId) {
		try {
			CompetidorService.deleteById(CompetidorId);
			return ResponseEntity.noContent().build(); // 204
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(e.getMessage(),"Error Interno del Servidor");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}

	@Operation(summary = "Inscribir un Competidor a uno o varias Comptencias")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Competidor inscripto correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Competidor.class))}),
			@ApiResponse(responseCode = "404", description = "Error al obtener el Competidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "Error al intentar inscribir al Competidor, hay un conflicto con los datos", 
				content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error Interno de Servidor", content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping("/inscribir")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Identificador del Competidor y Comptencias a inscribir",
			required = true,
			content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
							name = "Inscripci\u00f3n m\u00faltiple",
							value = "{\"CompetidorId\":5,\"ComptenciaIds\":[1,2,3,4]}"
					),
					schema = @Schema(implementation = IncripcionCompetidorDTO.class)
			)
	)
	public ResponseEntity<?> inscribirCompetidorAComptencias(@RequestBody IncripcionCompetidorDTO dto) {
		try {
			Competidor Competidor = CompetidorService.inscribirCompetidorACompetencias(dto);
			return ResponseEntity.ok(Competidor);
		} catch (IllegalStateException e) {
			ErrorResponse error = new ErrorResponse("Conflicto", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(error);	// 409
		} catch (IllegalArgumentException e) {
			ErrorResponse error = new ErrorResponse("Error 404", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	// Error 404
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(e.getMessage(),"Error Interno del Servidor");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Error 500
		}
	}

}