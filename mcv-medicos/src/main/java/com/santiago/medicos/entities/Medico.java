package com.santiago.medicos.entities;

import com.santiago.commons.enums.DisponibilidadMedico;
import com.santiago.commons.enums.EspecialidadMedico;
import com.santiago.commons.enums.EstadoRegistro;
import com.santiago.commons.utils.StringCustomUtils;
import com.santiago.commons.utils.ValoresNumericosUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MEDICOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@ToString

public class Medico {
	 @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "ID_MEDICO")
	  private Long id;

	  @Column(name = "NOMBRE", nullable = false, length = 50)
	  private String nombre;

	  @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
	  private String apellidoPaterno;

	  @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
	  private String apellidoMaterno;

	  @NotNull
	  @Min(0)
	  @Max(100)
	  @Column(name = "EDAD", nullable = false, precision = 3)
	  private Short edad;

	  @Email
	  @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
	  private String email;
	  
	  @NotBlank
	  @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener exactamente 10 dígitos numéricos")
	  @Column(name = "TELEFONO", nullable = false, unique = true, length = 10)
	  private String telefono;

	  @Column(name = "CEDULA_PROFESIONAL", nullable = false, unique = true, length = 12)
	  private String cedulaProfesional;

	  @Enumerated(EnumType.STRING)
	  @Column(name = "ESPECIALIDAD", nullable = false)
	  private EspecialidadMedico especialidad;

	  @Enumerated(EnumType.STRING)
	  @Column(name = "DISPONIBILIDAD", nullable = false)
	  private DisponibilidadMedico disponibilidad;

	  @Enumerated(EnumType.STRING)
	  @Column(name = "ESTADO_REGISTRO", nullable = false)
	  private EstadoRegistro estadoRegistro;

	  public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno,
			Short edad, String email, String telefono,
			String cedulaProfesional, EspecialidadMedico especialidad) {
		  
		  validarNoEliminado();
		  
		  validarDatos(
				  nombre, apellidoPaterno, apellidoMaterno,edad, email, telefono, cedulaProfesional,especialidad);
		
		actualizarEspecialidad(especialidad);
			
		this.nombre = nombre.trim();
		this.apellidoPaterno = apellidoPaterno.trim();
		this.apellidoMaterno = apellidoMaterno.trim();
		this.edad = edad;
		this.email = email.toLowerCase();
		this.telefono = telefono.trim();
		this.cedulaProfesional = cedulaProfesional.trim();
	  }
	  
	  public void actualizarDisponibilidad(DisponibilidadMedico nuevaDisponibilidad) {
		  if(nuevaDisponibilidad == null)
			  throw new IllegalArgumentException("La disponibilidad es requerida");
		  
		  this.disponibilidad = nuevaDisponibilidad;
	  }
	  
	  private void validarDatos(
				String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email,
				String telefono, String cedulaProfesional, EspecialidadMedico especialidad) {
		  
		  
		  StringCustomUtils.validarTamanio(nombre, 1, 50, "El nombre es requerido y debe tener entre 1 y 50 caracteres");
		  
		  StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50, "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");
		  
		  StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50, "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");
		  
		  StringCustomUtils.validarTamanio(email, 1, 100, "El email es requerido y debe tener entre 1 y 50 caracteres");
		  
		  StringCustomUtils.validarTamanio(telefono, 10, 10, "El telefono es requerido y debe tener exacatamente 10 digitos(0-9)");
		  
		  StringCustomUtils.validarTamanio(cedulaProfesional, 12, 12, "La cedula profesional es requerido y debe tener exactamente 12 caracteres");
		  
		  ValoresNumericosUtils.validarRangoShort(edad, (short)18, (short)100, "La edad es requerida y debe tener entre 18 y 100 años");
		  
		  if(especialidad == null)
			  throw new IllegalArgumentException("La especialidad es requerida");
	  }
	  
	  public void actualizarEspecialidad(EspecialidadMedico nuevaEspecialidad) {
		  if(nuevaEspecialidad == null)
			  throw new IllegalArgumentException("La especialidad es requerida");
		  
		  this.especialidad = nuevaEspecialidad;
	  }
	  
	  public void eliminar() {
		  
		  validarNoEliminado();
		  
		  this.estadoRegistro = EstadoRegistro.ELIMINADO;
	  }
	  
	  private void validarNoEliminado() {
		  if(this.estadoRegistro == EstadoRegistro.ELIMINADO)
			  throw new IllegalArgumentException("El medico ya esta eliminado");
	  }
}