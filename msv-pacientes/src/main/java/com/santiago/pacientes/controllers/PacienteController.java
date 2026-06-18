package com.santiago.pacientes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.commons.controllers.CommonController;
import com.santiago.commons.dto.PacienteRequest;
import com.santiago.commons.dto.PacienteResponse;
import com.santiago.pacientes.services.PacienteService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/pacientes")
@Validated
public class PacienteController extends CommonController<PacienteRequest, PacienteResponse, PacienteService> {

	public PacienteController(PacienteService service) {
		super(service);
		
	}
	
	@GetMapping("/id-paciente/{id}")
    public ResponseEntity<PacienteResponse> obtenerPacientePorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerPorIdSinEstado(id));
    }
	
}