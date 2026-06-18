package com.santiago.pacientes.services;

import com.santiago.commons.dto.PacienteRequest;
import com.santiago.commons.dto.PacienteResponse;
import com.santiago.commons.services.CrudService;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {
    PacienteResponse obtenerPorIdSinEstado(Long id);
}