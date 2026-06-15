package com.santiago.medicos.services;

import com.santiago.commons.dto.MedicoRequest;
import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.services.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
	
	MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
	
	void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);

}