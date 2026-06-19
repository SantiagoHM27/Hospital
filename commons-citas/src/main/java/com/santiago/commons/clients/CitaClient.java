package com.santiago.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mcv-citas")
public interface CitaClient {
	
	@GetMapping("/api/citas/id-medico/{idMedico}/citas-asignadas")
	void medicoTieneCitasAsignadas(@PathVariable Long idMedico);
	
	@GetMapping("/api/citas/id-paciente/{idPaciente}/citas-asignadas")
    void pacienteTieneCitasAsignadas(@PathVariable Long idPaciente);

}
