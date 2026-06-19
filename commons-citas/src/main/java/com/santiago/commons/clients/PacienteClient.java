package com.santiago.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.santiago.commons.dto.PacienteResponse;

@FeignClient(name = "mcv-pacientes")

public interface PacienteClient {

    @GetMapping("/api/pacientes/{id}")
    PacienteResponse obtenerPacienteActivoPorId(@PathVariable Long id);

    @GetMapping("/api/pacientes/id-paciente/{id}")
    PacienteResponse obtenerPacienteSinEstadoPorId(@PathVariable Long id);
}
