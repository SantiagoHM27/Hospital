package com.santiago.medicos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.santiago.commons.dto.MedicoRequest;
import com.santiago.commons.dto.MedicoResponse;
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
@Slf4j
public class MedicoServiceImpl implements MedicoService {
	
	private final MedicoRepository medicoRepository;
	
	private final MedicoMapper medicoMapper;

	@Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        log.info("Listado de todos los médicos activos solicitado");
        return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(medicoMapper::entidadAResponse).toList();
    }
	
	@Override
	@Transactional(readOnly = true)
	public MedicoResponse obtenerPorId(Long id) {
		
		return medicoMapper.entidadAResponse(obtenerMedicoActivoOException(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
log.info("Buscando médico sin estado con id: {}", id);
        
        return medicoMapper.entidadAResponse(medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado con id: " + id)));
	}

	@Override
	public MedicoResponse registrar(MedicoRequest request) {
		log.info("Registrando un nuevo Medico: {} ", request.nombre());
		
		validarDatosUnicos(request);
		
		Medico medico = medicoMapper.requestAEntidad(request);
		
		medico.actualizarEspecialidad(
				EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));
		
		medicoRepository.save(medico);
		
		log.info("Nuevo Medico Registrado {}", medico.getNombre());
		
		return medicoMapper.entidadAResponse(medico);
	}

	@Override
	public MedicoResponse actualizar(MedicoRequest request, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
		// TODO Auto-generated method stub
		
	}
	
	private Medico obtenerMedicoActivoOException(Long id) {
        log.info("Buscando médico con estado {} con id: {}", EstadoRegistro.ACTIVO, id);
        
        return medicoRepository.findByIdAndEstadoRegistrado(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico activo no encontrado con id: " + id));
    }
	
	private void validarDatosUnicos(MedicoRequest request) {
        
        log.info("Validando email único...");
        
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un médico activo con el email: " + request.email());
        }
        
        log.info("Validando teléfono único...");
        
        if (medicoRepository.existsByTelefonoAndEstadoRegistro(request.telefono().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un médico activo con el teléfono: " + request.telefono());
        }
        
        log.info("Validando cédula profesional única...");
        
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un médico activo con la cédula: " + request.cedulaProfesional()); 
           
    }
        
}
	private void validarCambiosUnicos(MedicoRequest request, Long id) {
        
        log.info("Validando email único...");
        
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un médico activo con el email: " + request.email());
        }
        
        log.info("Validando teléfono único...");
        
        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un médico activo con el teléfono: " + request.telefono());
        }
        
        log.info("Validando cédula profesional única...");
        
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un médico activo con la cédula: " + request.cedulaProfesional()); 
           
    }



	}
	
}
