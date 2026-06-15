package com.santiago.medicos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.medicos.entities.Medico;


import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
	
	 List<Medico>findByEstadoRegistro(EstadoRegistro estadoRegistro);
	 
	Optional<Medico> findByIdAndEstadoRegistrado(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);

    boolean existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(String cedulaProfesional, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
            String email, EstadoRegistro estadoRegistro, Long id);

    boolean existsByTelefonoAndEstadoRegistroAndIdNot(
            String telefono, EstadoRegistro estadoRegistro, Long id);

    boolean existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(
            String cedulaProfesional, EstadoRegistro estadoRegistro, Long id);
}