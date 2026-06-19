package com.santiago.citas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.santiago.citas.dto.CitaRequest;
import com.santiago.citas.dto.CitaResponse;
import com.santiago.citas.entities.Cita;
import com.santiago.citas.enums.EstadoCita;
import com.santiago.citas.mappers.CitaMapper;
import com.santiago.citas.repositories.CitaRepository;
import com.santiago.commons.clients.MedicoClient;
import com.santiago.commons.clients.PacienteClient;
import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.dto.PacienteResponse;
import com.santiago.commons.enums.DisponibilidadMedico;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.exceptions.EntidadRelacionadaException;
import com.santiago.commons.exceptions.RecursoNoEncontradoException;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;
    private final List<EstadoCita> ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS =
            List.of(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA,EstadoCita.EN_CURSO);
    private final PacienteClient pacienteClient;

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listado de todas las citas");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(cita -> citaMapper.entidadAResponse(
                        cita,
                        obtenerPacienteSinEstado(cita.getIdPaciente()),
                        obtenerMedicoSinEstado(cita.getIdMedico())
                )).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaActivaOException(id);
        return citaMapper.entidadAResponse(cita, 
        		obtenerPacienteSinEstado(cita.getIdPaciente()),
        		obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita: {}", request);
        
        pacienteClient.obtenerPacienteActivoPorId(request.idPaciente());
        
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        validarDisponibilidadMedico(medico.idDisponibilidad());

        // PacienteResponse
        validarPacienteTieneRegistrosAsignados(request.idPaciente());
        Cita cita = citaMapper.requestAEntidad(request);
        citaRepository.save(cita);
        
        cambiarDisponibilidadMedicoSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());

        log.info("Cita registrada exitosamente");
        return citaMapper.entidadAResponse(
                cita, obtenerPacienteSinEstado(cita.getIdPaciente()), 
                obtenerMedicoSinEstado(cita.getIdMedico())
        );
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaActivaOException(id);
        log.info("Actualizando cita con id: {}", id);
        pacienteClient.obtenerPacienteActivoPorId(request.idPaciente());
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        validarDisponibilidadMedico(medico.idDisponibilidad());
        if (!cita.getIdMedico().equals(request.idMedico())) {
            cambiarDisponibilidadMedico(cita.getIdMedico(), DisponibilidadMedico.DISPONIBLE.getCodigo());
        }
        if (!cita.getIdPaciente().equals(request.idPaciente()))
            validarPacienteTieneRegistrosAsignados(request.idPaciente());
        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );
        log.info("Cita actualizada con id: {}", id);
        return citaMapper.entidadAResponse(cita, 
        		obtenerPacienteSinEstado(cita.getIdPaciente()),
        		medico);
    }
    
    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
    	Cita cita = obtenerCitaActivaOException(idCita);
    	
    	log.info("Actualizando estado de la cita con id: {}",idCita);
    	
    	EstadoCita nuevoEstado = EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita);
    	
    	cita.actualizarEstadoCita(nuevoEstado);
    	
    	citaRepository.saveAndFlush(cita);
    	
    	cambiarDisponibilidadMedicoSegunEstadoCita(cita.getIdMedico(), nuevoEstado);
    	
    	log.info("Estado de la cita {} actualizado correctamente", cita.getId());
    }

    @Override
    public void medicoTieneCitasAsignadas(Long idMedico) {

        log.info("Validando si el médico tiene una cita activa con los estados: {}", 
                ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);

        boolean tieneCitas = citaRepository
                .existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
                        idMedico, 
                        EstadoRegistro.ACTIVO, 
                        ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);

        if (tieneCitas) {
            throw new EntidadRelacionadaException(
                    "No se puede modificar el médico ya que tiene citas con estados: "
                    + ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);
        }
    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaActivaOException(id);

        log.info("Eliminando cita con id: {}", id);
        cita.eliminar();
        citaRepository.saveAndFlush(cita);
        cambiarDisponibilidadMedicoSegunEstadoCita(cita.getIdMedico(), EstadoCita.CANCELADA);
        log.info("Cita con id {} ha sido marcada como eliminada");
    }

    private Cita obtenerCitaActivaOException(Long id) {
        log.info("Buscando cita activa con id: {}", id);
        return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita activa no encontrada con el id: " + id));
    }

    private MedicoResponse obtenerMedicoActivo(Long idMedico) {
        log.info("Buscando medico activo con id: {} en el servicio remoto...", idMedico);
        return medicoClient.obtenerMedicoActivoPorId(idMedico);
    }

    private MedicoResponse obtenerMedicoSinEstado(Long idMedico) {
        log.info("Buscando medico sin estado con id: {} en el servicio remoto...", idMedico);
        return medicoClient.obtenerMedicoSinEstadoPorId(idMedico);
    }
    private void validarDisponibilidadMedico(Long idDisponibilidad) {
        log.info("Validando si el medico se encuentra en estado: {}", DisponibilidadMedico.DISPONIBLE);
        if (!DisponibilidadMedico.DISPONIBLE.getCodigo().equals(idDisponibilidad))
            throw new IllegalStateException("El medico no se encuentra en estado: " + DisponibilidadMedico.DISPONIBLE);
    }

    private void validarPacienteTieneRegistrosAsignados(Long idPaciente){
        log.info("Validando si el paciente con id {} tiene una cita activa con los estados: {},",
                idPaciente, ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);
        if (citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
                idPaciente, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS
        ))
            throw  new EntidadRelacionadaException(
                    "No se puede registrar la citaya que el paciente solo puede tener una" +
                            "cita activa con los estados de: " + ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS
            );
    }
    
    @Override
	public void pacienteTieneCitasAsignadas(Long idPaciente) {
		log.info("Validando si el paciente con id {} tiene citas activas", idPaciente);
		
		boolean tieneCitas = citaRepository
	            .existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
	                    idPaciente,
	                    EstadoRegistro.ACTIVO,
	                    ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);
		
		if (tieneCitas) {
			throw new EntidadRelacionadaException(
					"No se puede modificar el paciente que ya tiene citas con estado: "
					+ ESTADOS_INVALIDOS_REGISTROS_ASIGNADOS);
		}
		
	}
    
    private PacienteResponse obtenerPacienteSinEstado(Long idPaciente) {
        log.info("Buscando paciente sin estado con id: {} en el servicio remoto...", idPaciente);
        return pacienteClient.obtenerPacienteSinEstadoPorId(idPaciente);
    }

    
    private void cambiarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
		log.info("Actualizando disponibilidad de medico con id: {} a {}",
				idMedico, DisponibilidadMedico.obtenerDisponibilidadPorCodigo(idDisponibilidad));
		
		medicoClient.actualizarDisponibilidadMedico(idMedico, idDisponibilidad);
	}
    
    private void cambiarDisponibilidadMedicoSegunEstadoCita(Long idMedico, EstadoCita estadoCita) {
		switch (estadoCita) {
			
		case PENDIENTE, CONFIRMADA ->
		cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.NO_DISPONIBLE.getCodigo());
		
		case EN_CURSO ->
		cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.EN_CONSULTA.getCodigo());
		
		case FINALIZADA, CANCELADA ->
		cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.DISPONIBLE.getCodigo());
		
		}
	}

}
