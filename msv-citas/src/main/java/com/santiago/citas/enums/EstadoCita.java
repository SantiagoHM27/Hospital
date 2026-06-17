package com.santiago.citas.enums;

import java.util.EnumSet;
import java.util.Set;

import com.santiago.commons.exceptions.RecursoNoEncontradoException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EstadoCita {
	
	PENDIENTE(1L, "Pendiente de confirmar", true, true) {
		
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return EnumSet.of(CONFIRMADA, CANCELADA);
		}
	},
	
	CONFIRMADA(2L, "Confirmada por el paciente", true, false) {
		
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return EnumSet.of(EN_CURSO, CANCELADA);
			
		}
	},
	
	EN_CURSO(3L, "Paciente llego a su cita", false, false) {
		
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return EnumSet.of(FINALIZADA);
		}
	},
	
	FINALIZADA(4L, "Cita Finalizada", false, true) {
		
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return Set.of();
		}
	},
	
	CANCELADA(5L, "Cita cancelada", false, true) {
		
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return Set.of();
		}
	};
	
	private final Long codigo;
	
	private final String descripcion;
	
	private final boolean actualizable;
	
	private final boolean eliminable;
	
	public abstract  Set<EstadoCita> puedeCambiar();
	
	public boolean puedeCambiarA(EstadoCita nuevoEstado) {
		return this.puedeCambiar().contains(nuevoEstado);
	}
	
	public static EstadoCita obtenerEstadoCitaPorCodigo(Long codigo) {
		for(EstadoCita e: values()) {
			if(e.codigo == codigo) {
				return e;
			}
		}
		throw new RecursoNoEncontradoException("Cosdigo de cita no valido: " + codigo);
	}

}
