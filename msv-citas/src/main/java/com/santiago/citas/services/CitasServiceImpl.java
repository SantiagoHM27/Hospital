package com.santiago.citas.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.santiago.citas.MsvCitasApplication;
import com.santiago.citas.dto.CitaRequest;
import com.santiago.citas.dto.CitaResponse;
import com.santiago.citas.entities.Cita;
import com.santiago.citas.enums.EstadoCita;
import com.santiago.citas.mappers.CitaMapper;
import com.santiago.citas.repositories.CitaRepository;
import com.santiago.commons.clients.MedicoClient;
import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.enums.DisponibilidadMedico;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.exceptions.EntidadRelacionadaException;
import com.santiago.commons.exceptions.RecursoNoEncontradoException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitasServiceImpl  implements CitaService{
	
	private final MsvCitasApplication msvCitasApplication;
	
	private final CitaRepository citaRepository;
	
	private final CitaMapper citaMapper;
	
	private final MedicoClient medicoClient;
	
	private final List<EstadoCita> ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS =
			List.of(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);

	@Override
	@Transactional(readOnly = true)
	public List<CitaResponse> listar() {
		log.info("Listando todas las citas activas solicitadas");
		return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(cita -> citaMapper.entidadAResponse(
						cita,
						null,
						obtenerMedicoSinEstado(cita.getIdMedico())
						)
					).toList();
	}

	@Override
	public CitaResponse obtenerPorId(Long id) {
		
		Cita cita = obtenerCitaActivaOException(id);
        
        return citaMapper.entidadAResponse(
                cita, null, obtenerMedicoSinEstado(cita.getIdMedico()));
    }

	@Override
	public CitaResponse registrar(CitaRequest request) {
		
		log.info("Registrando nueva Cita: {}", request);
		
		MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
		
		validarDisponibilidadMedico(medico.idDisponibilidad());
		
		//Paciente Response
		
		validarPacienteTieneRegistrosAsignados(request.idPaciente());
        
        Cita cita = citaMapper.requestAEntidad(request);
        
        citaRepository.save(cita);
        
        log.info("Cita registrada exitosamente");
        
        return citaMapper.entidadAResponse(
                cita, null, obtenerMedicoSinEstado(cita.getIdMedico()));
    }

	@Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaActivaOException(id);
        
        log.info("Actualizando cita con id: {}", id);
        
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        
        validarDisponibilidadMedico(medico.idDisponibilidad()); 

        if (!cita.getIdPaciente().equals(request.idPaciente()))
            validarPacienteTieneRegistrosAsignados(request.idPaciente());
        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );
        log.info("Cita actualizada con id: {}", id);
        return citaMapper.entidadAResponse(cita, null, medico);
    }
	@Override
	public void eliminar(Long id) {
		
		Cita cita = obtenerCitaActivaOException(id);
		
		log.info("Eliminando cita con id: {}", id);
		
		cita.eliminar();
		log.info("Cita con id: {} ha sido marcada como eliminada");
		
	}
	
	private  Cita obtenerCitaActivaOException(Long id) {
		log.info("Buscando cita activa con id: {}", id);
		
		return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
				() -> new RecursoNoEncontradoException("Cita activa no encontraada con el id: " + id));
	}
	
	private MedicoResponse obtenerMedicoActivo(Long idMedico) {
		log.info("Buscando médico activo con id: {} en el servicio remoto....");
		return medicoClient.obtenerMedicoActivoPorId(idMedico);
		
	}
	
	private MedicoResponse obtenerMedicoSinEstado(Long idMedico) {
		log.info("Buscando médico sin estado con id: {} en el servicio remoto....");
		return medicoClient.obtenerMedicoSinEstadoPorId(idMedico);
		
	}
	
	private void validarDisponibilidadMedico(Long idDisponibilidad) {
		log.info("Validando si el medico se encuentra en estado: {}", DisponibilidadMedico.DISPONIBLE);
		
		if(!DisponibilidadMedico.DISPONIBLE.getCodigo().equals(idDisponibilidad))
			throw new IllegalStateException("El medico no se encunetra en estado: "
					+ DisponibilidadMedico.DISPONIBLE);
	}
	
	private void validarPacienteTieneRegistrosAsignados(Long idPaciente) {
		log.info("Validando si el paciente  con id: {} tiene una cita activa con los estados: {}",
				idPaciente, ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);
		
		if(citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
				idPaciente, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS))
			throw new EntidadRelacionadaException(
					"No se puede registrar la cita ya que el apciente solo puede tener una"
					+ "cita activa con los estados de: " + ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);
	}
	
	
	
	

}
