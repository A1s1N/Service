package org.service.controllers;

import org.service.models.Doctor;
import org.service.models.Patient;
import org.service.repo.PatientRepository;
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
public class PatientController {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/patients")
    public String patient(Model model) {
        Iterable<Patient> patients = patientRepository.findAll();
        if(StreamSupport.stream(patients.spliterator(), false).findAny().isEmpty())
            model.addAttribute("message", "На данный момент пациенты отсутствуют");
        else
            model.addAttribute("patients", patients);
        return "patients/patients";
    }

    @GetMapping("/patients/add")
    public String patientAdd(Model model) {
        return "patients/patients-add";
    }

    @PostMapping("/patients/add")
    public String patientNewAdd(@RequestParam String fullName,
                                @RequestParam String birthdate,
                                Model model) {
        Patient patient = new Patient(fullName, birthdate);
        patientRepository.save(patient);
        return "redirect:/patients";
    }

    @GetMapping("/patients/{id}")
    public String patientDetails(@PathVariable(value = "id") Long id, Model model) {
        if(!patientRepository.existsById(id))
            return "redirect:/doctors";

        Optional<Patient> patient = patientRepository.findById(id);
        ArrayList<Patient> res = new ArrayList<>();
        patient.ifPresent(res::add);
        model.addAttribute("patient", res);
        return "patients/patients-details";
    }

    @GetMapping("/patients/{id}/edit")
    public String patientEdit(@PathVariable(value = "id") Long id, Model model) {
        if(!patientRepository.existsById(id))
            return "redirect:/patients";

        Optional<Patient> patient = patientRepository.findById(id);
        ArrayList<Patient> res = new ArrayList<>();
        patient.ifPresent(res::add);
        model.addAttribute("patient", res);
        return "patients/patients-edit";
    }

    @PostMapping("/patients/{id}/edit")
    public String patientUpdate(@PathVariable(value = "id") Long id,
                               @RequestParam String fullName,
                               @RequestParam String birthdate,
                               Model model) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        patient.setFullName(fullName);
        patient.setBirthdate(birthdate);
        patientRepository.save(patient);
        return "redirect:/patients";
    }

    @PostMapping("/patients/{id}/remove")
    public String patientDelete(@PathVariable(value = "id") long id, Model model) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        patientRepository.delete(patient);
        return "redirect:/patients";
    }

}
