package com.santiago.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.santiago.commons.dto.MedicoResponse;

@FeignClient(name = "msv-medicos")
public interface MedicoClient {
	
	 @GetMapping("/api/medicos/{id}") 
	MedicoResponse obtenerMedicoActivoPorId(@PathVariable Long id);
	

	@GetMapping("/api/medicos/id-medico/{id}")
	MedicoResponse obtenerMedicoSinEstadoPorId(@PathVariable Long id);
	
	@PutMapping("/api/medicos/{idMedico}/disponibilidad/{idDisponibilidad}")
    void actualizarDisponibilidadMedico(
    		@PathVariable Long idMedico,
    		@PathVariable Long idDisponibilidad);
}
