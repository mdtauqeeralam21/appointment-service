package com.pms.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.Controller.AppointmentController;
import com.pms.Service.Impl.AppointmentServiceImpl;
import com.pms.entity.Appointment;



@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private AppointmentServiceImpl appointmentService;
	
	private Appointment testAppointment;
	
	@Autowired
    ObjectMapper mapper;
	
	@Test
	public void testSaveDetails() throws Exception {
		testAppointment = new Appointment();
		testAppointment.setReason("Checkup");
		testAppointment.setAppointmentDate(LocalDate.of(2023, 5, 1));
		testAppointment.setAcceptance("Pending");
		testAppointment.setPatientId("P001");
		testAppointment.setPhysicianEmail("testphysician@test.com");
		testAppointment.setPatientName("Ravi");
		testAppointment.setSubmissionDate(LocalDate.now());
		when(appointmentService.saveDetails(any(Appointment.class))).thenReturn(testAppointment);
		
		mockMvc.perform(post("/api/v1/appointment")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testAppointment)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.reason", is(testAppointment.getReason())))
			.andExpect(jsonPath("$.appointmentDate", is(testAppointment.getAppointmentDate().toString())))
			.andExpect(jsonPath("$.acceptance", is(testAppointment.getAcceptance())))
			.andExpect(jsonPath("$.patientId", is(testAppointment.getPatientId())))
			.andExpect(jsonPath("$.physicianEmail", is(testAppointment.getPhysicianEmail())))
			.andExpect(jsonPath("$.submissionDate", is(testAppointment.getSubmissionDate().toString())));
		
		verify(appointmentService).saveDetails(argThat(appointment -> 
			appointment.getReason().equals(testAppointment.getReason())
			&& appointment.getAppointmentDate().equals(testAppointment.getAppointmentDate())
			&& appointment.getAcceptance().equals(testAppointment.getAcceptance())
			&& appointment.getPatientId().equals(testAppointment.getPatientId())
			&& appointment.getPhysicianEmail().equals(testAppointment.getPhysicianEmail())
			&& appointment.getPatientName().equals(testAppointment.getPatientName())
			&& appointment.getSubmissionDate().equals(testAppointment.getSubmissionDate())));
	}
	
//	//test to update appointment  that throws exception
//	  @Test
//	    public void update_appointment_on_id_exception() throws Exception {
//	        
//		  when(appointmentService.updateDetails(testAppointment, "AP001")).thenReturn(null);
//	        
//	        mockMvc.perform(MockMvcRequestBuilders
//	                .put("/api/v1/appointment/AP001")
//	                .contentType(MediaType.APPLICATION_JSON))
//	                .andExpect(status().isUnauthorized());
//	    }
	
	@Test
	public void testDeleteDetails() throws Exception {
	    String appointmentId = "AP001";

	    Mockito.when(appointmentService.deleteDetails(appointmentId)).thenReturn(true);

	    mockMvc.perform(MockMvcRequestBuilders
	    		.delete("/api/v1/appointment/{appointmentId}", appointmentId))
	            .andExpect(status().isAccepted());
	}
	
	 //test to delete appointment  that throws exception
	  @Test
	    public void delete_appointment_on_id_exception() throws Exception {
	        
	        Mockito.when(appointmentService.deleteDetails("AP001")).thenReturn(false);
	        
	        mockMvc.perform(MockMvcRequestBuilders
	                .delete("/api/v1/appointment/AP001")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isInternalServerError());
	    }
	
	@Test
	public void testUpdateDetails() throws Exception {
	    String appointmentId = "AP001";

	    Appointment updatedAppointment = new Appointment();
	    updatedAppointment.setReason("Updated Reason");

	    Mockito.when(appointmentService.updateDetails(updatedAppointment, appointmentId)).thenReturn(updatedAppointment);

	    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/appointment/{appointmentId}", appointmentId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(updatedAppointment)))
	            .andExpect(status().isAccepted())
	            .andExpect(jsonPath("$.reason", is("Updated Reason")));
	}
	@Test
	public void testShowAvailability() throws Exception {
	    // Arrange
	    String patientId = "P01";
	    List<Appointment> appointments = new ArrayList<>();
	    appointments.add(new Appointment());
	    Mockito.when(appointmentService.getAppointment(patientId)).thenReturn(appointments);

	    // Act
	    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/" + patientId))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$[0]", notNullValue()));
	}
	
	
	
	 //test to get appointment on patient id that throws exception
	  @Test
	    public void get_appointment_on_id_exception() throws Exception {
	        
	        Mockito.when(appointmentService.getAppointment("P101")).thenReturn(null);
	        
	        mockMvc.perform(MockMvcRequestBuilders
	                .get("/api/v1/patient/P101")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isUnauthorized());
	    }

	  @Test
	    public void test_Show_appointment_on_acceptance() throws Exception {
	       
	        List<Appointment> expectedAppointments = Arrays.asList(
	            new Appointment("1A", "Fever",LocalDate.now(), "Pending", "P101","phy1@gmail.com","Ravi",LocalDate.now()),
	            new Appointment("2A", "Knee pain",LocalDate.now(), "Pending", "P201","phy1@gmail.com","Raju",LocalDate.now())
	           
	        );
	        Mockito.when(appointmentService.showAppointment("Pending")).thenReturn(expectedAppointments);
	        mockMvc.perform(MockMvcRequestBuilders
	        		.get("/api/v1/appointment?acceptance=Pending")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].reason").value("Fever"))
	                .andExpect(jsonPath("$[0].acceptance").value("Pending"))
	                .andExpect(jsonPath("$[0].patientName").value("Ravi"))
	                .andExpect(jsonPath("$[1].reason").value("Knee pain"))
	                .andExpect(jsonPath("$[1].acceptance").value("Pending"))
	                .andExpect(jsonPath("$[1].patientName").value("Raju"));
	               
	    }
	  //test to show appointments on acceptance that throws exception
	  @Test
	    public void test_Show_appointment_on_acceptance_exception() throws Exception {
	        
	        Mockito.when(appointmentService.showAppointment("Pending")).thenReturn(null);
	        
	        mockMvc.perform(MockMvcRequestBuilders
	                .get("/api/v1/appointment?acceptance=Pending")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isUnauthorized());
	    }
	    
	  //GET method for all appointment details
	  @Test
	  public void get_all_appointments_success() throws Exception {
		  
		  Appointment t1 = new Appointment();
		  Appointment t2 = new Appointment();
		  Appointment t3 = new Appointment();
		  
	      List<Appointment> records = Arrays.asList(t1,t2,t3);
	      
	      Mockito.when(appointmentService.showAllAppointments()).thenReturn(records);
	      
	      mockMvc.perform(MockMvcRequestBuilders
	              .get("/api/v1/appointments")
	              .contentType(MediaType.APPLICATION_JSON))
	              .andExpect(status().isOk())
	              .andExpect(jsonPath("$", notNullValue()));
	  }

}
