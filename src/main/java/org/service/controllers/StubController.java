package org.service.controllers;

import org.service.models.Doctor;
import org.service.models.Patient;
import org.service.models.Stub;
import org.service.repo.DoctorRepository;
import org.service.repo.PatientRepository;
import org.service.repo.StubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Controller
public class StubController {

    private final StubRepository stubRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    Doctor defaultDoctor = new Doctor("Неизвестно");
    Patient defaultPatient = new Patient("Неизвестно","");

    @Autowired
    public StubController(StubRepository stubRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.stubRepository = stubRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/stubs")
    public String stub(
                       Model model) {
        Iterable<Stub> stubs = stubRepository.findAll();
        if(StreamSupport.stream(stubs.spliterator(), false).findAny().isEmpty())
            model.addAttribute("message", "На данный момент талоны отсутствуют");
        else {
            model.addAttribute("stubs", stubs);
            model.addAttribute("defaultDoctor", defaultDoctor);
            model.addAttribute("defaultPatient", defaultPatient);
            model.addAttribute("doctorRep", doctorRepository);
            model.addAttribute("patientRep", patientRepository);
        }
        return "stubs/stubs";
    }

    @GetMapping("/stubs/add")
    public String stubsAdd(Model model) {
        Iterable<Doctor> doctors = doctorRepository.findAll();
        Iterable<Patient> patients = patientRepository.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        return "stubs/stubs-add";
    }

    @PostMapping("/stubs/add")
    public String stubsNewAdd(@RequestParam Doctor doctor,
                              @RequestParam Patient patient,
                              @RequestParam String timeAppointment,
                              Model model) {
        Stub stub = new Stub(doctor.getId(), patient.getId(), timeAppointment);
        stubRepository.save(stub);
        return "redirect:/stubs";
    }

    @GetMapping("/stubs/{id}")
    public String stubsDetails(@PathVariable(value = "id") Long id, Model model) {
        if(!stubRepository.existsById(id))
            return "redirect:/doctors";

        Optional<Stub> stub = stubRepository.findById(id);
        ArrayList<Stub> res = new ArrayList<>();
        stub.ifPresent(res::add);
        Doctor doctor = doctorRepository.findById(res.get(0).getDoctorId()).orElse(defaultDoctor);
        Patient patient = patientRepository.findById(res.get(0).getPatientId()).orElse(defaultPatient);
        model.addAttribute("stub", res);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", patient);
        return "stubs/stubs-details";
    }

    @GetMapping("/stubs/{id}/edit")
    public String stubEdit(@PathVariable(value = "id") Long id, Model model) {
        if(!stubRepository.existsById(id))
            return "redirect:/stubs";

        Optional<Stub> stub = stubRepository.findById(id);
        ArrayList<Stub> res = new ArrayList<>();
        stub.ifPresent(res::add);
        Iterable<Doctor> doctors = doctorRepository.findAll();
        Iterable<Patient> patients = patientRepository.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        model.addAttribute("stub", res);
        return "stubs/stubs-edit";
    }

    @PostMapping("/stubs/{id}/edit")
    public String stubUpdate(@PathVariable(value = "id") Long id,
                             @RequestParam Doctor doctor,
                             @RequestParam Patient patient,
                             @RequestParam String timeAppointment,
                             Model model) {
        Stub stub = stubRepository.findById(id).orElseThrow();
        stub.setDoctorId(doctor.getId());
        stub.setPatientId(patient.getId());
        stub.setTimeAppointment(timeAppointment);
        stubRepository.save(stub);
        return "redirect:/stubs";
    }

    @PostMapping("/stubs/{id}/remove")
    public String patientDelete(@PathVariable(value = "id") long id, Model model) {
        Stub stub = stubRepository.findById(id).orElseThrow();
        stubRepository.delete(stub);
        return "redirect:/stubs";
    }

}
