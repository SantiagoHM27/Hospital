package com.santiago.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msv-citas")
public interface CitaClient {
	
	   @GetMapping("/id-medico/{idMedico}/citas-activas-criticas")
	    Boolean medicoTieneCitasConfirmadasOEnCurso(@PathVariable Long idMedico);

}
