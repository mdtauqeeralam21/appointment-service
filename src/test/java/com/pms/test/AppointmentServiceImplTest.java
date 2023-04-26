package com.pms.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pms.Repository.AppointmentRepository;
import com.pms.Service.Impl.AppointmentServiceImpl;
import com.pms.entity.Appointment;
import com.pms.exception.AppointmentServiceException;


@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {
	@InjectMocks
	AppointmentServiceImpl appointmentService;
	@Mock
	AppointmentRepository appointmentRepository;
	   
	@Test
		public void testDeleteDetails() throws AppointmentServiceException {
		    Appointment appointment = new Appointment();
		    appointment.setAppointmentId("AP001");
		    appointment.setReason("Check-up");
		    appointment.setAppointmentDate(LocalDate.now());
		    appointment.setAcceptance("Pending");
		    appointment.setPatientId("P001");
		    appointment.setPhysicianEmail("physician@example.com");
		    appointment.setSubmissionDate(LocalDate.now());

		    Mockito.when(appointmentRepository.existsById("AP001")).thenReturn(true);

		    boolean result = appointmentService.deleteDetails("AP001");

		    assertTrue(result);
		}
		
	//test for delete appointment that throws exception
	@Test
	public void delete_appointment_Exception() throws AppointmentServiceException {
		when(appointmentRepository.existsById("AP001")).thenReturn(false);
		assertThrows(AppointmentServiceException.class, () -> {
			appointmentService.deleteDetails("AP001");
	    });
		}
	
	
		@Test
		public void testShowAvailability() throws AppointmentServiceException {
		    
		    Appointment appointment1 = new Appointment("AP001", "Reason 1", LocalDate.of(2023, 4, 15), "Accepted", "P001", "physician1@example.com","Rahul", LocalDate.of(2023, 4, 14));
		    //Appointment appointment2 = new Appointment("AP002", "Reason 2", LocalDate.of(2023, 4, 16), "Pending", "P002", "physician1@example.com","Ravi", LocalDate.of(2023, 4, 14));
		    Appointment appointment3 = new Appointment("AP003", "Reason 3", LocalDate.of(2023, 4, 15), "Accepted", "P003", "physician2@example.com","Raju", LocalDate.of(2023, 4, 14));
		  //  List<Appointment> appointmentList = Arrays.asList(appointment1, appointment2, appointment3);

		    
		    Mockito.when(appointmentRepository.findAppointment(Mockito.eq("physician1@example.com"), Mockito.eq(LocalDate.of(2023, 4, 15)), Mockito.eq("Accepted")))
		            .thenReturn(Arrays.asList(appointment1,appointment3));


		    List<Appointment> result = appointmentService.showAvailability("physician1@example.com", LocalDate.of(2023, 4, 15), "Accepted");


		    assertNotNull(result);
		    assertEquals(2, result.size());
		    assertEquals("AP001", result.get(0).getAppointmentId());
		    assertEquals("Reason 1", result.get(0).getReason());
		    assertEquals(LocalDate.of(2023, 4, 15), result.get(0).getAppointmentDate());
		    assertEquals("Accepted", result.get(0).getAcceptance());
		    assertEquals("P001", result.get(0).getPatientId());
		    assertEquals("Rahul", result.get(0).getPatientName());
		    assertEquals("physician1@example.com", result.get(0).getPhysicianEmail());
		    assertEquals(LocalDate.of(2023, 4, 14), result.get(0).getSubmissionDate());

		    
		    Mockito.verify(appointmentRepository).findAppointment("physician1@example.com", LocalDate.of(2023, 4, 15), "Accepted");
		}
		
		//test for show availability that throws exception
		@Test
		public void show_appointment_availability_Exception() throws AppointmentServiceException {
			Mockito.when(appointmentRepository.findAppointment(Mockito.eq("physician1@example.com"), Mockito.eq(LocalDate.of(2023, 4, 15)), Mockito.eq("Accepted")))
            .thenReturn(null);
			assertThrows(AppointmentServiceException.class, () -> {
				appointmentService.showAvailability("physician1@example.com", LocalDate.of(2023, 4, 15), "Accepted");
		    });
			}

		
	    @Test
	    public void testSaveDetails() throws AppointmentServiceException {
	    	 Appointment appointment = new Appointment();
		        appointment.setAppointmentId("AP001");
		        appointment.setReason("Headache");
		        appointment.setAppointmentDate(LocalDate.of(2023, 5, 1));
		        appointment.setAcceptance("Pending");
		        appointment.setPatientId("P001");
		        appointment.setPhysicianEmail("doctor@example.com");
		        appointment.setSubmissionDate(LocalDate.now());
		        
		        when(appointmentRepository.save(appointment)).thenReturn(appointment);
	   
	        Appointment result = appointmentService.saveDetails(appointment);
	        
	        assertEquals("AP001", result.getAppointmentId());
	        assertEquals("Pending", result.getAcceptance());
	        verify(appointmentRepository, times(1)).save(appointment);
	    }
	    
	  //test for save appointment that throws exception
		@Test
		public void save_appointment_Exception() throws AppointmentServiceException {
			 Appointment appointment = new Appointment("AP001", "Reason 1", LocalDate.of(2023, 4, 15), "Accepted", "P001", "physician1@example.com","Rahul", LocalDate.of(2023, 4, 14));
			when(appointmentRepository.save(appointment)).thenReturn(null);
			assertThrows(AppointmentServiceException.class, () -> {
				appointmentService.saveDetails(appointment);
		    });
			}
	    
		@Test
		public void testShowAppointment() throws AppointmentServiceException {
		    List<Appointment> appointmentList = new ArrayList<>();
		    appointmentList.add(new Appointment("AP001", "Test Reason 1", LocalDate.now(), "ACCEPTED","Rahul", "PAT001", "test1@example.com", LocalDate.now()));
		    appointmentList.add(new Appointment("AP002", "Test Reason 2", LocalDate.now(), "ACCEPTED","Ravi", "PAT002", "test2@example.com", LocalDate.now()));

		    Mockito.when(appointmentRepository.findByAcceptance("ACCEPTED")).thenReturn(appointmentList);

		    List<Appointment> result = appointmentService.showAppointment("ACCEPTED");

		    assertEquals(appointmentList.size(), result.size());
		    assertEquals(appointmentList.get(0).getAppointmentId(), result.get(0).getAppointmentId());
		    assertEquals(appointmentList.get(1).getAppointmentId(), result.get(1).getAppointmentId());
		}
		
		//test to show appointment that throws exception
		@Test
		public void show_appointments_Exception() throws AppointmentServiceException {
			
		    Mockito.when(appointmentRepository.findByAcceptance("ACCEPTED")).thenReturn(null);
		    
			assertThrows(AppointmentServiceException.class, () -> {
				appointmentService.showAppointment("ACCEPTED");
		    });
			}
		
		
		@Test
		void testShowAppointmentByRejected() throws AppointmentServiceException {

			
			String physicianEmail = "testemail@test.com";
			String acceptance = "Rejected";
			
			
			Appointment appointment1 = new Appointment("AP001", "Reason 1", LocalDate.now(), "Rejected","Rahul", "PID001", "physician1@test.com", LocalDate.now());
			Appointment appointment2 = new Appointment("AP002", "Reason 2", LocalDate.now(), "Rejected","Ravi", "PID002", "physician2@test.com", LocalDate.now());
			List<Appointment> appointmentList = new ArrayList<>();
			appointmentList.add(appointment1);
			appointmentList.add(appointment2);

			
			when(appointmentRepository.findByPending(physicianEmail, acceptance)).thenReturn(appointmentList);

		
			List<Appointment> result = appointmentService.showAppointmentByRejected(physicianEmail, acceptance);

			
			assertEquals(appointmentList.size(), result.size());
			assertEquals(appointmentList.get(0).getAppointmentId(), result.get(0).getAppointmentId());
			assertEquals(appointmentList.get(1).getAppointmentId(), result.get(1).getAppointmentId());
		}
		
		//test to reject appointment that throws exception
		@Test
		public void reject_appointment_Exception() throws AppointmentServiceException {
			when(appointmentRepository.findByPending("patient@gmail.com", "rejected")).thenReturn(null);
			assertThrows(AppointmentServiceException.class, () -> {
				appointmentService.showAppointmentByRejected("patient@gmail.com", "rejected");
		    });
			}
		
		
		@Test
		public void testShowAllAppointments() throws AppointmentServiceException {
			
			Appointment appointment1 = new Appointment("AP001", "reason1", LocalDate.of(2023, 4, 15), "accepted","Ravi", "patient1@gmail.com", "physician1@gmail.com", LocalDate.of(2023, 4, 10));
			Appointment appointment2 = new Appointment("AP002", "reason2", LocalDate.of(2023, 4, 16), "accepted","Rahul", "patient2@gmail.com", "physician2@gmail.com", LocalDate.of(2023, 4, 11));
			List<Appointment> appointmentList = new ArrayList<>();
			appointmentList.add(appointment1);
			appointmentList.add(appointment2);
			
	
			when(appointmentRepository.findAll()).thenReturn(appointmentList);
			
			List<Appointment> result = appointmentService.showAllAppointments();
			
		
		assertEquals(appointmentList.size(), result.size());
		assertEquals(appointmentList.get(0).getAppointmentId(), result.get(0).getAppointmentId());
		assertEquals(appointmentList.get(1).getAppointmentId(), result.get(1).getAppointmentId());
		}
		
		//test to show all appointments that throws exception
		@Test
		public void show_all_appointments_Exception() throws AppointmentServiceException {
			when(appointmentRepository.findAll()).thenReturn(null);
				assertThrows(AppointmentServiceException.class, () -> {
				appointmentService.showAllAppointments();				    
				});
				}
		
		@Test
		public void testGetAppointment() throws AppointmentServiceException {
		   
		    String patientId = "P01";
		    List<Appointment> expectedAppointments = new ArrayList<>();
		    expectedAppointments.add(new Appointment("AP001", "Reason 1", LocalDate.now(), "Pending","Rahul", "P01", "doctor@example.com", LocalDate.now()));
		    expectedAppointments.add(new Appointment("AP002", "Reason 2", LocalDate.now().plusDays(1),"Ravi", "Accepted", "P01", "doctor@example.com", LocalDate.now()));
		    Mockito.when(appointmentRepository.findByPatientId(patientId)).thenReturn(expectedAppointments);
		    
		  
		    List<Appointment> actualAppointments = appointmentService.getAppointment(patientId);
		    
		
		    assertNotNull(actualAppointments);
		    assertEquals(expectedAppointments.size(), actualAppointments.size());
		    for (int i = 0; i < expectedAppointments.size(); i++) {
		        Appointment expected = expectedAppointments.get(i);
		        Appointment actual = actualAppointments.get(i);
		        assertEquals(expected.getAppointmentId(), actual.getAppointmentId());
		        assertEquals(expected.getReason(), actual.getReason());
		        assertEquals(expected.getAppointmentDate(), actual.getAppointmentDate());
		        assertEquals(expected.getAcceptance(), actual.getAcceptance());
		        assertEquals(expected.getPatientId(), actual.getPatientId());
		        assertEquals(expected.getPhysicianEmail(), actual.getPhysicianEmail());
		        assertEquals(expected.getSubmissionDate(), actual.getSubmissionDate());
		    }
		}
		
		//test to get one appointment that throws exception
				@Test
				public void get_one_appointment_Exception() throws AppointmentServiceException {
					Mockito.when(appointmentRepository.findByPatientId("AP001")).thenReturn(null);
						assertThrows(AppointmentServiceException.class, () -> {
							appointmentService.getAppointment("AP001");				    
						});
							}

		@Test
		public void testUpdateDetails() throws AppointmentServiceException {
			Appointment appointment = new Appointment("AP001", "reason1", LocalDate.of(2023, 4, 15), "accepted", "patient1@gmail.com", "physician1@gmail.com", "Ravi",LocalDate.of(2023, 4, 10));
			when(appointmentRepository.findByAppointmentId("AP001")).thenReturn(appointment);
			when(appointmentRepository.saveAndFlush(any(Appointment.class))).thenReturn(appointment);

			appointment.setAcceptance("Approved");
			Appointment updatedAppointment = appointmentService.updateDetails(appointment, "AP001");

			assertEquals("Approved", updatedAppointment.getAcceptance());
		}
		
		//test to update appointment that throws exception
		@Test
		public void update_appointment_Exception() throws AppointmentServiceException {
			
			Appointment appointment = new Appointment("AP001", "reason1", LocalDate.of(2023, 4, 15), "accepted", "patient1@gmail.com", "physician1@gmail.com", "Ravi", LocalDate.of(2023, 4, 10));
			when(appointmentRepository.findByAppointmentId("AP001")).thenReturn(null);
			

				assertThrows(AppointmentServiceException.class, () -> {
					appointmentService.updateDetails(appointment, "AP001");				    
				});
					}

}
