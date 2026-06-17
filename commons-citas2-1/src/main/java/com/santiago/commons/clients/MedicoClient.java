package com.santiago.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.santiago.commons.dto.MedicoResponse;

@FeignClient(name = "msv-medicos")
public interface MedicoClient {
	
	@GetMapping("/{id}")
	MedicoResponse obtenerMedicoActivoPorId(@PathVariable Long id);
	
	@GetMapping("/id-medico/{id}")
	MedicoResponse obtenerMedicoSinEstadoPorId(@PathVariable Long id);
}
