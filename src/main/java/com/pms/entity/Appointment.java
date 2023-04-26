package com.pms.entity;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Use database appointment_db;	
@Entity
@Data // gives all lombok
@Table(name = "appointment")
@EnableJpaAuditing
@Component
public class Appointment {

	
	  
	@Id
	@GeneratedValue(generator = CustomIdGenerator.GENERATOR_NAME)
	@GenericGenerator(name = CustomIdGenerator.GENERATOR_NAME, strategy = "com.pms.entity.CustomIdGenerator", parameters = {
	@Parameter(name = CustomIdGenerator.PREFIX_PARAM, value = "AP00") })
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String appointmentId;
//	@Column(name ="appointmentReason")
	private String reason;
	
	@Column(nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")	
	private LocalDate appointmentDate;
	
	
//	@Column( name ="appointmentAcceptance")
	private String acceptance;
	@NotBlank
	private String patientId;
	@NotBlank
	@Email
	private String physicianEmail;
	
	@NotBlank
	private String PatientName;
	
	
	@Column(nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate submissionDate;

	

	public Appointment(String appointmentId, String reason, LocalDate appointmentDate, String acceptance,
			 String patientId,  @Email String physicianEmail,  String patientName,
			LocalDate submissionDate) {
		super();
		this.appointmentId = appointmentId;
		this.reason = reason;
		this.appointmentDate = appointmentDate;
		this.acceptance = acceptance;
		this.patientId = patientId;
		this.physicianEmail = physicianEmail;
		PatientName = patientName;
		this.submissionDate = submissionDate;
	}



	public Appointment() {
		super();
	}
	
	
	
	
	
}