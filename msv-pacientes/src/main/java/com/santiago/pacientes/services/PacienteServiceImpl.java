package com.santiago.pacientes.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.dto.PacienteRequest;
import com.santiago.commons.dto.PacienteResponse;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.exceptions.RecursoNoEncontradoException;
import com.santiago.pacientes.entities.Paciente;
import com.santiago.pacientes.mappers.PacienteMapper;
import com.santiago.pacientes.repositories.PacienteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        log.info("Listado de todos los pacientes activos solicitados");
        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(pacienteMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
        return pacienteMapper.entidadAResponse(obtenerPacienteActivoOException(id));
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {
        log.info("Registrando un nuevo paciente: {}", request.nombre());

        validarDatosUnicos(request);

        Paciente paciente = pacienteMapper.requestAEntidad(request);

        pacienteRepository.save(paciente);

        log.info("Nuevo paciente registrado: {}", paciente.getNombre());

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {

        Paciente paciente = obtenerPacienteActivoOException(id);

        log.info("Actualizando paciente con id: {}", id);

        validarCambiosUnicos(request, id);

        paciente.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.peso(),
                request.estatura(),
                request.email(),
                request.telefono(),
                request.direccion()
        );

        log.info("Paciente actualizado con id: {}", id);

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando paciente con id: {}", id);

        Paciente paciente = obtenerPacienteActivoOException(id);

        paciente.eliminar();

        log.info("Paciente eliminado con id: {}", id);
    }

    private Paciente obtenerPacienteActivoOException(Long id) {
        log.info("Buscando paciente con estado {} con id: {}", EstadoRegistro.ACTIVO, id);

        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente activo no encontrado con id: " + id));
    }

    private void validarDatosUnicos(PacienteRequest request) {
        log.info("Validando email unico ...");

        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un paciente activo con el email: " + request.email());

        log.info("Validando telefono unico ...");

        if (pacienteRepository.existsByTelefonoAndEstadoRegistro(request.telefono().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un paciente activo con el telefono: " + request.telefono());
    }

    private void validarCambiosUnicos(PacienteRequest request, Long id) {
        log.info("Validando email unico ...");

        if (pacienteRepository.existsByEmailIgnoreCaseAndIdNotAndEstadoRegistro(request.email().trim(), id, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un paciente activo con el email: " + request.email());

        log.info("Validando telefono unico ...");

        if (pacienteRepository.existsByTelefonoAndIdNotAndEstadoRegistro(request.telefono().trim(), id, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un paciente activo con el telefono: " + request.telefono());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorIdSinEstado(Long id) {
        log.info("Buscando paciente sin validar estado con id: {}", id);
        return pacienteMapper.entidadAResponse(
            pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + id))
        );
    }
}