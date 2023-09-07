package org.service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Stub {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long doctorId;

    private Long patientId;

    private String timeAppointment;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getTimeAppointment() {
        return timeAppointment;
    }

    public void setTimeAppointment(String timeAppointment) {
        this.timeAppointment = timeAppointment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stub() {}

    public Stub(Long doctorId, Long patientId, String timeAppointment) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.timeAppointment = timeAppointment;
    }
}
