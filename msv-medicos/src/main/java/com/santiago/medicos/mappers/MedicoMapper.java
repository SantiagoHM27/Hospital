package com.santiago.medicos.mappers; 

import org.springframework.stereotype.Component;

import com.santiago.commons.dto.MedicoRequest;
import com.santiago.commons.dto.MedicoResponse;
import com.santiago.commons.enums.DisponibilidadMedico;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.mappers.CommonMapper;
import com.santiago.medicos.entities.Medico;

@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico> {

    @Override
    public Medico requestAEntidad(MedicoRequest request) {
        if (request == null) return null;

        return Medico.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .edad(request.edad())
                .email(request.email())
                .telefono(request.telefono())
                .cedulaProfesional(request.cedulaProfesional())
                .disponibilidad(DisponibilidadMedico.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public MedicoResponse entidadAResponse(Medico entidad) {
        if (entidad == null) return null;

        return new MedicoResponse(
                entidad.getId(),
                String.join(" ", entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCedulaProfesional(),
                entidad.getEspecialidad().getDescripcion(),
                entidad.getDisponibilidad().getDescripcion(),
                entidad.getDisponibilidad().getCodigo()
        );
    }
}