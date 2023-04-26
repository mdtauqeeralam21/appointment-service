package com.pms.Service;

import java.time.LocalDate;
import java.util.List;

import com.pms.entity.Appointment;
import com.pms.exception.AppointmentServiceException;



public interface AppointmentService {

	public Appointment saveDetails(Appointment appointment)throws AppointmentServiceException;
	public boolean deleteDetails(String appointmentId)throws AppointmentServiceException;
	public Appointment updateDetails(Appointment appointment ,String appointmentId)throws AppointmentServiceException;
	public List<Appointment> getAppointment(String patientId)throws AppointmentServiceException;
	public List<Appointment> showAvailability(String physicianEmail , LocalDate date , String acceptance)throws AppointmentServiceException;
	public List<Appointment> showAppointment(String acceptance)throws AppointmentServiceException;
	public List<Appointment> showAppointmentByRejected(String physicianEmail ,String acceptance)throws AppointmentServiceException;
	public List<Appointment> showAllAppointments()throws AppointmentServiceException;
}
