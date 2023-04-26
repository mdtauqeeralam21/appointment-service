package com.pms.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.Repository.AppointmentRepository;
import com.pms.Service.AppointmentService;
import com.pms.entity.Appointment;

import com.pms.exception.AppointmentServiceException;


@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentrepository;

	

	@Override
	public Appointment saveDetails(Appointment appointment) throws AppointmentServiceException {
		appointment.setAcceptance("Pending");
		Appointment result = appointmentrepository.save(appointment);

		if (result == null) {
			throw new AppointmentServiceException("Appointment didn't booked");
		}
		return result;
	}

	// for delete
	@Override
	public boolean deleteDetails(String appointmentId) throws AppointmentServiceException {
		boolean bool = false;
		bool = appointmentrepository.existsById(appointmentId);

		if (bool == false) {
			throw new AppointmentServiceException("Given appointment Id is not valid " + appointmentId);
		}

		appointmentrepository.deleteById(appointmentId);
		;
		System.out.println("item deleted");
		return bool;

	}

	// for updating
	@Override
	public Appointment updateDetails(Appointment appointment, String appointmentId) throws AppointmentServiceException {
		Appointment app = null;
		app = appointmentrepository.findByAppointmentId(appointmentId);

		if (app == null) {
			throw new AppointmentServiceException("Given appointment Id is not valid " + appointmentId);
		}

		app.setAcceptance(appointment.getAcceptance());
//			app.setAppointmentDate(appointment.getAppointmentDate());
//			app.setPhysicianEmail(appointment.getPhysicianEmail());
//			app.setReason(appointment.getReason());
//			app.setSubmissionDate(appointment.getSubmissionDate());
		return appointmentrepository.saveAndFlush(app);

	}

	// for fetching with patiendId , only for patient
	@Override
	public List<Appointment> getAppointment(String patientId) throws AppointmentServiceException {
		List<Appointment> appointmentList = null;
		appointmentList = (List<Appointment>) this.appointmentrepository.findByPatientId(patientId);

		if (appointmentList == null) {
			throw new AppointmentServiceException("Given patient Id is not valid " + patientId);
		}
		return appointmentList;

	}

	// for fetching with physicianEmail and acceptance and date , only for physician
	@Override
	public List<Appointment> showAvailability(String physicianEmail, LocalDate date, String acceptance)
			throws AppointmentServiceException {
		List<Appointment> appointmentList = null;

		appointmentList = (List<Appointment>) this.appointmentrepository.findAppointment(physicianEmail, date,
				acceptance);
		if (appointmentList == null) {
			throw new AppointmentServiceException("Given physician email or date or acceptance are not valid " + physicianEmail+" "+date+" "+acceptance);
		}

		return appointmentList;
	}

	// for fetching with acceptance , only for nurse
	@Override
	public List<Appointment> showAppointment(String acceptance) throws AppointmentServiceException {
		List<Appointment> appointmentList = null;

		appointmentList = (List<Appointment>) this.appointmentrepository.findByAcceptance(acceptance);

		if (appointmentList == null) {
			throw new AppointmentServiceException("Given appointment acceptance is not exists  " + acceptance);
		}

		return appointmentList;
	}

	// for fetching with physicianEmail and acceptance , only for physician
	@Override
	public List<Appointment> showAppointmentByRejected(String physicianEmail, String acceptance) throws AppointmentServiceException {
		List<Appointment> appointmentList = null;

		appointmentList = (List<Appointment>) this.appointmentrepository.findByPending(physicianEmail, acceptance);
		
		if(appointmentList == null) {
			throw new AppointmentServiceException("Given Physician email or acceptence are not valid "+acceptance+" "+physicianEmail);
		}
		
		return appointmentList;

	}
	
	
	
	@Override
	public List<Appointment> showAllAppointments() throws AppointmentServiceException {
		List<Appointment> list = (List<Appointment>) this.appointmentrepository.findAll();
		
		if(list == null)
			throw new AppointmentServiceException("Appointments does not exist ");
		
		return list;

}
}