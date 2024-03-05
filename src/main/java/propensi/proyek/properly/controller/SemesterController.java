package propensi.proyek.properly.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import propensi.proyek.properly.Dto.semester.CreateSemesterDTO;
import propensi.proyek.properly.Dto.semester.SemesterMapper;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.service.semester.SemesterService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SemesterController {
    @Autowired
    private SemesterMapper semesterMapper;

    @Autowired
    private SemesterService semesterService;

    // need mapper kalo mau pake DTO

    @GetMapping("/semester/create-test")
    public String createSemester() {
        // testing save data
        Semester semester = new Semester();

        semester.setId(UUID.randomUUID()); // Set a random UUID
        System.out.println(semester.getId());
        semester.setIsGanjil(false);
        semester.setTanggalAwal(LocalDate.of(2022, 9, 1)); // Set start date
        semester.setTanggalAkhir(LocalDate.of(2022, 12, 31)); // Set end date
        semester.setTahunAjaran("2022/2023");

        var isOverlap = semesterService.isSemestersOverlap(semester.getTanggalAwal(), semester.getTanggalAkhir());
        System.out.println(isOverlap);

        Set<Kelas> classes = new HashSet<>();
        if (isOverlap == false) {
            semesterService.save(semester);
        }
        return "home";
    }

    // Show form create semester
    @GetMapping("/semester/create")
    public String createSemesterForm(Model model) {
        var semesterDTO = new CreateSemesterDTO();
        model.addAttribute("semesterDTO", semesterDTO);
        return "form-create-semester";
    }

    // Process create semester from form
    @PostMapping("/semester/create")
    public String createSemester(@ModelAttribute CreateSemesterDTO semesterDTO) {
        // check dulu isi dto
        System.out.println(semesterDTO);

        // check if the new dates overlapped with the one in database
        var isOverlap = semesterService.isSemestersOverlap(semesterDTO.getTanggalAwal(), semesterDTO.getTanggalAkhir());

        if (!isOverlap) {
            // map semesterDTO to semester
            var semester = semesterMapper.createSemesterDTOToSemester(semesterDTO);
            semesterService.save(semester);
        }
        // var semester = semesterMapper.createSemesterDTOToSemester(semesterDTO);
        return "home";
    }

}
