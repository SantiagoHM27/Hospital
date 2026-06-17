package com.santiago.pacientes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.pacientes.entities.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{

    List<Paciente> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Paciente> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndIdNotAndEstadoRegistro(String email, Long id, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndIdNotAndEstadoRegistro(String telefono, Long id, EstadoRegistro estadoRegistro);

}