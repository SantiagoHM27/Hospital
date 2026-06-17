package com.santiago.citas.dto;

import java.time.LocalDateTime;

public record CitaResponse(
		Long id,
		String paciente,
		String medico,
		LocalDateTime fechaCita,
		String sintomas,
		String estadoCita		
) {}
