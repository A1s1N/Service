package org.service.controllers;

import org.service.models.Doctor;
import org.service.repo.DoctorRepository;
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
public class DoctorController {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctors")
    public String doctor(Model model) {
        Iterable<Doctor> doctors = doctorRepository.findAll();
        if(StreamSupport.stream(doctors.spliterator(), false).findAny().isEmpty())
            model.addAttribute("message", "На данный момент врачи отсутствуют");
        else
            model.addAttribute("doctors", doctors);
        return "doctors/doctors";
    }

    @GetMapping("/doctors/add")
    public String doctorAdd(Model model) {
        return "doctors/doctors-add";
    }

    @PostMapping("/doctors/add")
    public String doctorNewAdd(@RequestParam String fullName, Model model) {
        Doctor doctor = new Doctor(fullName);
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/doctors/{id}")
    public String doctorDetails(@PathVariable(value = "id") Long id, Model model) {
        if(!doctorRepository.existsById(id))
            return "redirect:/doctors";

        Optional<Doctor> doctor = doctorRepository.findById(id);
        ArrayList<Doctor> res = new ArrayList<>();
        doctor.ifPresent(res::add);
        model.addAttribute("doctor", res);
        return "doctors/doctors-details";
    }

    @GetMapping("/doctors/{id}/edit")
    public String doctorEdit(@PathVariable(value = "id") long id, Model model) {
        if(!doctorRepository.existsById(id))
            return "redirect:/doctors";

        Optional<Doctor> doctor = doctorRepository.findById(id);
        ArrayList<Doctor> res = new ArrayList<>();
        doctor.ifPresent(res::add);
        model.addAttribute("doctor", res);
        return "doctors/doctors-edit";
    }

    @PostMapping("/doctors/{id}/edit")
    public String doctorUpdate(@PathVariable(value = "id") long id,
                               @RequestParam String fullName,
                               Model model) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctor.setFullName(fullName);
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @PostMapping("/doctors/{id}/remove")
    public String doctorDelete(@PathVariable(value = "id") long id, Model model) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctorRepository.delete(doctor);
        return "redirect:/doctors";
    }

}
