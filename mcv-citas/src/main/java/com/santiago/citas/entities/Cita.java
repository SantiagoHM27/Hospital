package com.santiago.citas.entities;

import com.santiago.citas.enums.EstadoCita;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.utils.StringCustomUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "CITAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder @Getter
@ToString
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long id;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "ID_MEDICO", nullable = false)
    private Long idMedico;

    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "SINTOMAS", nullable = false, length = 500)
    private String sintomas;

    @Column(name = "ESTADO_CITA", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estadoCita;

    @Column(name = "ESTADO_REGISTRO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro;
    
    public static Cita crear(Long idPaciente, Long idMedico,
    		LocalDateTime fechaCita, String sintomas) {
    	
    	validarId(idPaciente, "paciente");
    	
    	validarId(idMedico, "médico");
    	
    	validarFechaCita(fechaCita);
    	
    	StringCustomUtils.validarNoVacio(sintomas, "Los síntomas son requeridos");
    	
    	return Cita.builder()
    			.idPaciente(idPaciente)
    			.idMedico(idMedico)
    			.fechaCita(fechaCita)
    			.sintomas(sintomas)
    			.estadoCita(EstadoCita.PENDIENTE)
    			.estadoRegistro(EstadoRegistro.ACTIVO)
    			.build();
    }
    
    public void actualizar(Long idPaciente, Long idMedico, LocalDateTime fechaCita, String sintomas) {
    	
    	validarActualizacionPermitida();
    	
    	validarId(idPaciente, "paciente");
    	
    	validarId(idMedico, "medico");
    	
    	validarFechaCita(fechaCita);
    	
    	StringCustomUtils.validarNoVacio(sintomas, "Lo sintomas son requeridos");
    	
    	this.idPaciente = idPaciente;
    	this.idMedico = idMedico;
    	this.fechaCita = fechaCita;
    	this.sintomas = sintomas;
    }
    
    public void actualizarEstadoCita(EstadoCita nuevoEstado) {
    	
    	validarNoEliminado();
    	
    	if(nuevoEstado == null)
    		throw new IllegalStateException("El nuevo estado de la cita es requerido");
    	
    	if(!this.estadoCita.puedeCambiarA(nuevoEstado))
    		throw new IllegalStateException("La cita con estado " + this.estadoCita + 
    				" solo puede camiar a : " + this.estadoCita.puedeCambiar());
    	
    	this.estadoCita = nuevoEstado;
    }
    
    public void eliminar() {
    	
    	validarEliminacionPermitida();
    	this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
    
    
    private void validarNoEliminado() {
    	if(this.estadoRegistro == EstadoRegistro.ELIMINADO)
    		throw new IllegalStateException("La cita ya esta eliminda");
    }
    
    private static void validarId(Long id, String campo) {
    	if(id == null || id <= 0)
    		throw new IllegalStateException("El id del " + campo + " es requerido y debe ser positivo");
    }
    
    private static void validarFechaCita(LocalDateTime fechaCita) {
    	if(fechaCita == null || !fechaCita.isAfter(LocalDateTime.now()))
    		throw new IllegalStateException("La fecha de la cita es requerida y debe ser futura");
    }
    
    private void validarEliminacionPermitida() {
    	
    	validarNoEliminado();
    	
    	if(!this.estadoCita.isEliminable())
    		throw new IllegalStateException("La cita con estado " + this.estadoCita + " no puede elminiarse");
    }
    private void validarActualizacionPermitida() {
    	
    	validarNoEliminado();
    	
    	if(!this.estadoCita.isActualizable())
    		throw new IllegalStateException("La cita con estado " + this.estadoCita + " no puede actualizarse");
    }
    
    
}