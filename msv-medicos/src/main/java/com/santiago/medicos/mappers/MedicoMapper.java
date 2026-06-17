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
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad())
                .email(request.email().trim())
                .telefono(request.telefono().trim())
                .cedulaProfesional(request.cedulaProfesional().trim())
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
                entidad.getCedulaProfesional(),
                entidad.getEspecialidad().getDescripcion(),
                entidad.getDisponibilidad().getDescripcion(),
                
        );
    }
}