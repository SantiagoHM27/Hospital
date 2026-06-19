package com.santiago.citas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.santiago.commons.dto.DatosMedico;
import com.santiago.commons.dto.DatosPaciente;

public record CitaResponse(
		Long id,
		DatosPaciente paciente,
		DatosMedico medico,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime fechaCita,
		String sintomas,
		String estadoCita
		) {}