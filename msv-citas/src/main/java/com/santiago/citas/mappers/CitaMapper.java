package com.santiago.citas.mappers;

import com.santiago.citas.dto.CitaRequest;
import com.santiago.citas.dto.CitaResponse;
import com.santiago.citas.entities.Cita;
import com.santiago.commons.dto.DatosMedico;
import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.mappers.CommonMapper;

public class CitaMapper implements CommonMapper<CitaRequest, CitaResponse, Cita> {
	
	@Override
	public Cita requestAEntidad(CitaRequest request) {
		if(request == null) return null;
		
		return Cita.crear(
				request.idPaciente(),
				request.idMedico(),
				request.fechaCita(),
				request.sintomas());
				
	}

	@Override
	public CitaResponse entidadAResponse(Cita entidad) {
		
		if(entidad == null) return null;
		
		return new CitaResponse(
				entidad.getId(),
				null,
				null,
				entidad.getFechaCita(),
				entidad.getSintomas(),
				entidad.getEstadoCita().getDescripcion());

	}
	
	public CitaResponse entidadAResponse(Cita entidad, Object paciente, MedicoResponse medicoResponse) {
		
		if(entidad == null) return null;
		
		return new CitaResponse(
				entidad.getId(),
				null,
				null,
				entidad.getFechaCita(),
				entidad.getSintomas(),
				entidad.getEstadoCita().getDescripcion());

	}
	
	private DatosMedico medicoResponseADatosMedico (MedicoResponse medico) {
		if (medico == null) return null;
		
		return new DatosMedico(
				medico.nombre(),
				medico.cedulaProfesional(),
				medico.especialidad());
	}


}
