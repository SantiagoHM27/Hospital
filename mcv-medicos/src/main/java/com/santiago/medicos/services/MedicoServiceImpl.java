package com.santiago.medicos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.santiago.commons.clients.CitaClient;
import com.santiago.commons.dto.MedicoRequest;
import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.enums.DisponibilidadMedico;
import com.santiago.commons.enums.EspecialidadMedico;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.exceptions.RecursoNoEncontradoException;
import com.santiago.medicos.entities.Medico;
import com.santiago.medicos.mappers.MedicoMapper;
import com.santiago.medicos.repositories.MedicoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Transactional
@Slf4j

public class MedicoServiceImpl implements MedicoService {
	
	private final MedicoRepository medicoRepository;
	
	private final MedicoMapper medicoMapper;
	
	private final CitaClient citaClient;

	@Override
	@Transactional(readOnly = true)
	public List<MedicoResponse> listar() {
		log.info("Listado de todos los medicos activos solicitados");
		return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(medicoMapper::entidadAResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
		
	    log.info("Buscando medico sin estado con id: {}", id);
	    
	    return medicoMapper.entidadAResponse(medicoRepository.findById(id)
	            .orElseThrow(() -> new RecursoNoEncontradoException("Medico no encontrado con id: " + id)));
	}
	
	@Override
	@Transactional(readOnly = true)
	public MedicoResponse obtenerPorId(Long id) {
		return medicoMapper.entidadAResponse(obtenerMedicoActivoOException(id));
	}

	@Override
	public MedicoResponse registrar(MedicoRequest request) {
		log.info("Registranod un nuevo medico: {} ", request.nombre());
		
		validarDatosUnicos(request);
		
		Medico medico = medicoMapper.requestAEntidad(request);
		
		medico.actualizarEspecialidad(
				EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));
		
		medicoRepository.save(medico);
		
		log.info("Nuevo medico registrado {}", medico.getNombre());
		
		return medicoMapper.entidadAResponse(medico);
	}



	@Override
	public void eliminar(Long id) {
		log.info("Eliminando medico con id: {}", id);
	    
	    Medico medico = obtenerMedicoActivoOException(id);
	    
	    medicoTieneCitasAsignadas(id);
	    
	    medico.eliminar();
	    
	    log.info("Medico eliminado con id: {}", id);

		
	}

	@Override
	public MedicoResponse actualizar(MedicoRequest request, Long id) {

		Medico medico = obtenerMedicoActivoOException(id);
		
		log.info("Actualizando Medico con id: {}",id);
		
		medicoTieneCitasAsignadas(id);
		
		validarCambiosUnicos(request, id);
		medico.actualizar(
	            request.nombre(),
	            request.apellidoPaterno(),
	            request.apellidoMaterno(),
	            request.edad(),
	            request.email(),
	            request.telefono(),
	            request.cedulaProfesional(),
	            EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));
	    
	    log.info("Medico actualizado con id: {}", id);
	    
	    return medicoMapper.entidadAResponse(medico);
	}
	
	@Override
	public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
		
		Medico medico = obtenerMedicoActivoOException(idMedico);
		log.info("Actualizando la disponibilidad del medico con id: {}", idMedico);
		
		DisponibilidadMedico nuevaDisponibilidad =  DisponibilidadMedico
				.obtenerDisponibilidadPorCodigo(idDisponibilidad);
		
		DisponibilidadMedico anteriorDisponibilidad = medico.getDisponibilidad();
		
		medico.actualizarDisponibilidad(nuevaDisponibilidad);
		
		log.info("Disponibilidad del medico con id {} cambio de {} a {}", idMedico, anteriorDisponibilidad, nuevaDisponibilidad);
	}
	
	private Medico obtenerMedicoActivoOException(Long id) {
	    log.info("Buscando medico con estado {} con id: {}", EstadoRegistro.ACTIVO, id);
	    
	    return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
	            .orElseThrow(() -> new RecursoNoEncontradoException("Medico activo no encontrado con id: " + id));
	}
	
	public void validarDatosUnicos(MedicoRequest request) {
	    
	    log.info("Validando email unico ...");
	    
	    if(medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email().trim(), EstadoRegistro.ACTIVO))
	        throw new IllegalStateException("Ya existe un medico activo con el email: " + request.email());
	    
	    log.info("Validando telefono unico ...");
	    
	    if(medicoRepository.existsByTelefonoAndEstadoRegistro(request.telefono().trim(), EstadoRegistro.ACTIVO))
	        throw new IllegalStateException("Ya existe un medico activo registrado con el telefono: " + request.telefono());
	    
	    log.info("Validando cedula profesional unica ...");
	    
	    if(medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO))
	        throw new IllegalStateException("Ya existe un medico activo registrado con la cedula: " + request.cedulaProfesional());
	}
	
	public void validarCambiosUnicos(MedicoRequest request, Long id) {
	    
	    log.info("Validando email unico ...");
	    
	    if(medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email().trim(), EstadoRegistro.ACTIVO, id))
	        throw new IllegalStateException("Ya existe un medico activo con el email: " + request.email());
	    
	    log.info("Validando telefono unico ...");
	    
	    if(medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono().trim(), EstadoRegistro.ACTIVO, id))
	        throw new IllegalStateException("Ya existe un medico activo registrado con el telefono: " + request.telefono());
	    
	    log.info("Validando cedula profesional unica ...");
	    
	    if(medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO, id))
	        throw new IllegalStateException("Ya existe un medico activo registrado con la cedula: " + request.cedulaProfesional());
	}
	
	private void medicoTieneCitasAsignadas(Long idMedico) {
		citaClient.medicoTieneCitasAsignadas(idMedico);
	}
}
