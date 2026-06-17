package com.santiago.citas.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.citas.entities.Cita;
import com.santiago.citas.enums.EstadoCita;
import com.santiago.commons.enums.EstadoRegistro;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
	
	List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	 boolean existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
			 Long idPaciente, EstadoRegistro estadoRegistro, List<EstadoCita> estadosCita);
	 
	 boolean existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
			 Long idMedico, EstadoRegistro estadoRegistro, List<EstadoCita> estadosCita);

}
