package propensi.proyek.properly.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import propensi.proyek.properly.Dto.semester.CreateSemesterDTO;
import propensi.proyek.properly.Dto.semester.SemesterMapper;
import propensi.proyek.properly.Dto.semester.UpdateSemesterDTO;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.service.semester.SemesterService;
import propensi.proyek.properly.service.user.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/semester")
public class SemesterController {
    @Autowired
    private SemesterMapper semesterMapper;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private UserService userService;

    // Show form create semester
    @GetMapping("/create")
    public String formCreateSemester(Model model, @ModelAttribute CreateSemesterDTO createSemesterDTO) {
        var semesterDTO = new CreateSemesterDTO();
        model.addAttribute("semesterDTO", semesterDTO);

        return "/semester/form-create-semester";
    }

    // Process create semester from form
    @PostMapping("/create")
    public String createSemester(
            Model model,
            @ModelAttribute CreateSemesterDTO semesterDTO,
            RedirectAttributes redirectAttrs) {

        // check if the new dates overlapped with the one in database
        var isOverlap = semesterService.semestersOverlap(semesterDTO.getTanggalAwal(), semesterDTO.getTanggalAkhir());
        var sameTahunAjaran = semesterService.countTahunAjaran(semesterDTO.getTahunAjaran());
        var countTypeTahunAjaran = semesterService.countTypeTahunAjaran(semesterDTO.getIsGanjil(),
                semesterDTO.getTahunAjaran());
        var compareTanggal = semesterDTO.getTanggalAwal().compareTo(semesterDTO.getTanggalAkhir());

        if (compareTanggal >= 0) {
            redirectAttrs.addFlashAttribute("error",
                    "Tanggal awal harus sebelum tanggal akhir. Silahkan ubah tanggal awal dan tanggal akhir.");
            return "redirect:/semester/create";
        } else if (isOverlap > 0) {
            redirectAttrs.addFlashAttribute("error",
                    "Tanggal awal dan akhir semester tumpang tindih dengan semester lain di basis data. Silahkan ubah tanggal semester.");
            return "redirect:/semester/create";
        } else if (sameTahunAjaran >= 2) {
            redirectAttrs.addFlashAttribute("error",
                    "Sudah ada dua semester yang memiliki tahun ajaran yang sama. Silahkan ubah tahun ajaran.");
            return "redirect:/semester/create";
        } else if (countTypeTahunAjaran >= 1) {
            redirectAttrs.addFlashAttribute("error",
                    "Sudah ada nama semester yang sama pada tahun ajaran yang sama. Silahkan ubah nama semester.");
            return "redirect:/semester/create";
        }

        // map semesterDTO to semester
        var semester = semesterMapper.createSemesterDTOToSemester(semesterDTO);
        // save semester to db
        semesterService.save(semester);
        // redirect to view-all
        var semesterName = semesterService.semesterNameGenerator(semester.getIsGanjil(), semester.getTahunAjaran());

        redirectAttrs.addFlashAttribute("success", semesterName + " berhasil ditambahkan.");
        return "redirect:/semester/view-all";
    }

    // View all semester in table form
    @GetMapping("/view-all")
    public String viewAllSemester(
            Authentication auth,
            Principal principal,
            Model model) {
        // get list of semesters via jpa
        List<Semester> semesters = semesterService.getAllSemesters();
        // add current user into model
        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        model.addAttribute("semesters", semesters);
        return "/semester/view-all-semester";
    }

    // Show form update semester
    @GetMapping("/{id}/update")
    public String formUpdateSemester(@PathVariable(value = "id") UUID id, Model model) {
        // Get semester by id
        var semester = semesterService.getSemesterById(id);

        // Map to update DTO
        var semesterDTO = semesterMapper.semesterToUpdateSemesterDTO(semester);
        model.addAttribute("semesterDTO", semesterDTO);

        return "/semester/form-update-semester";
    }

    // Process update semester
    @PostMapping("/{id}/update")
    public String updateSemester(@PathVariable(value = "id") UUID id, @ModelAttribute UpdateSemesterDTO semesterDTO,
            Model model, RedirectAttributes redirectAttrs) {
        // TODO: validasi POST request & redirect

        // check if the new dates overlapped with the one in database
        var changedFields = semesterService.whatHasChanged(semesterDTO);
        var isOverlap = semesterService.semestersOverlap(semesterDTO.getTanggalAwal(), semesterDTO.getTanggalAkhir());
        var sameTahunAjaran = semesterService.countTahunAjaran(semesterDTO.getTahunAjaran());
        var countTypeTahunAjaran = semesterService.countTypeTahunAjaran(semesterDTO.getIsGanjil(),
                semesterDTO.getTahunAjaran());
        var compareTanggal = semesterDTO.getTanggalAwal().compareTo(semesterDTO.getTanggalAkhir());

        // validate only if the updated data has changed
        if (changedFields.contains("tanggal") && compareTanggal >= 0) {
            redirectAttrs.addFlashAttribute("error",
                    "Tanggal awal harus sebelum tanggal akhir. Silahkan ubah tanggal awal dan tanggal akhir.");
            return String.format("redirect:/semester/%s/update", semesterDTO.getId().toString());

        } else if (changedFields.contains("tanggal") && isOverlap > 1) {
            redirectAttrs.addFlashAttribute("error",
                    "Tanggal awal dan akhir semester tumpang tindih dengan semester lain di basis data. Silahkan ubah tanggal semester.");
            return String.format("redirect:/semester/%s/update", semesterDTO.getId().toString());

        } else if (changedFields.contains("tahunAjaran") && sameTahunAjaran >= 2) {
            redirectAttrs.addFlashAttribute("error",
                    "Sudah ada dua semester yang memiliki tahun ajaran yang sama. Silahkan ubah tahun ajaran.");
            return String.format("redirect:/semester/%s/update", semesterDTO.getId().toString());

        } else if (changedFields.contains("isGanjil") && countTypeTahunAjaran >= 1) {
            redirectAttrs.addFlashAttribute("error",
                    "Sudah ada nama semester yang sama pada tahun ajaran yang sama. Silahkan ubah nama semester.");
            return String.format("redirect:/semester/%s/update", semesterDTO.getId().toString());
        }
        // map DTO to object
        var semesterFromDTO = semesterMapper.updateSemesterDTOToSemester(semesterDTO);
        var semester = semesterService.updateSemester(semesterFromDTO);
        // redirect to view-all
        var semesterName = semesterService.semesterNameGenerator(semester.getIsGanjil(), semester.getTahunAjaran());
        redirectAttrs.addFlashAttribute("success", semesterName + " berhasil diubah .");
        return "redirect:/semester/view-all";
    }

    // Process delete semester
    @GetMapping("/{id}/delete")
    public String deleteSemester(@PathVariable("id") UUID id, Model model, RedirectAttributes redirectAttrs) {
        // get semester by id
        var semester = semesterService.getSemesterById(id);
        // check if semester is not related to any class
        if (semester.getClasses() == null || semester.getClasses().isEmpty()) {
            semesterService.deleteSemester(id);
        } else {
            redirectAttrs.addFlashAttribute("error",
                    "Semester tidak bisa dihapus karena masih memiliki relasi dengan kelas lain.");
            return "redirect:/semester/view-all";
        }
        return "redirect:/semester/view-all";
    }

}
