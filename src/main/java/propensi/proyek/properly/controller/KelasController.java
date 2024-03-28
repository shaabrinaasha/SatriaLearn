package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.kelas.KelasService;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.semester.SemesterService;
import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.repository.SemesterDb;
import propensi.proyek.properly.repository.GuruDb;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import propensi.proyek.properly.service.user.UserService;
import org.springframework.security.core.Authentication;
import propensi.proyek.properly.model.User;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class KelasController {

    @Autowired
    KelasService kelasService;

    @Autowired
    SemesterService semesterService;

    @Autowired
    GuruService guruService;

    @Autowired
    SiswaService siswaService;

    @Autowired
    UserService userService;

    @Autowired
    SemesterDb semesterDb;

    @Autowired
    GuruDb guruDb;

    @GetMapping("/kelas")
    public String kelas(Model model, Principal principal, Authentication auth) {
        List<Kelas> listKelas = kelasService.getAllKelas();
        model.addAttribute("listKelas", listKelas);

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "kelas/kelas-viewall";
    }

    @GetMapping("/kelas/tambah")
    public String kelasAdd(Model model, Principal principal, Authentication auth) {
        Kelas kelas = new Kelas();
        List<Semester> listSemester = semesterService.getAllSemester();
        model.addAttribute("kelas", kelas);
        List<String> listTahunAjaran = new ArrayList<>();
        for (Semester semester : listSemester) {
            if (!listTahunAjaran.contains(semester.getTahunAjaran())) {
                listTahunAjaran.add(semester.getTahunAjaran());
            }
        }
        listTahunAjaran.sort((a, b) -> a.compareTo(b));
        model.addAttribute("listTahunAjaran", listTahunAjaran);
        List<Guru> listAvailableGuru = guruService.getAllGuru().stream().filter(guru -> guru.getWaliOf() == null)
                .collect(Collectors.toList());
        model.addAttribute("listGuru", listAvailableGuru);

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "kelas/kelas-add-form";
    }

    @PostMapping("/kelas/tambah")
    public String addKelasSubmit(@ModelAttribute Kelas kelas,
            @ModelAttribute("tahunAjaran") String tahunAjaran,
            BindingResult bindingResult, Model model, Principal principal, Authentication auth) {
        // Associate the tahunAjaran with exactly two semesters
        List<Semester> semesters = semesterService.getAllSemesterByTahunAjaran(tahunAjaran);

        // Associate the kelas with the semesters
        kelasService.addSemestersToKelas(kelas, new HashSet<>(semesters));

        for (Semester semester : semesters) {
            Set<Kelas> kelasSet = semester.getClasses();
            kelasSet.add(kelas);
            semesterService.addKelasToSemester(semester, kelas);
        }

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "redirect:/kelas";

    }

    @GetMapping("/kelas/ubah/{id}")
    public String changeKelasFormPage(@PathVariable UUID id, Model model, Principal principal, Authentication auth) {
        Kelas kelas = kelasService.getKelasById(id);
        model.addAttribute("kelas", kelas);

        List<Semester> listSemester = semesterService.getAllSemester();
        List<String> listTahunAjaran = new ArrayList<>();
        for (Semester semester : listSemester) {
            if (!listTahunAjaran.contains(semester.getTahunAjaran())) {
                listTahunAjaran.add(semester.getTahunAjaran());
            }
        }
        listTahunAjaran.sort((a, b) -> a.compareTo(b));
        var currentTahunAjaran = kelas.getSemesters().stream().findFirst().get().getTahunAjaran();
        model.addAttribute("currentTahunAjaran", currentTahunAjaran);
        model.addAttribute("listTahunAjaran", listTahunAjaran);
        List<Guru> listAvailableGuru = guruService.getAllGuru().stream().filter(guru -> guru.getWaliOf() == null)
                .collect(Collectors.toList());
        listAvailableGuru.add(kelas.getWali());
        model.addAttribute("listGuru", listAvailableGuru);
        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "kelas/kelas-update-form";
    }

    @PostMapping("/kelas/ubah")
    public String changeKelasFormSubmit(@ModelAttribute @Valid Kelas kelas,
            @ModelAttribute("tahunAjaran") String tahunAjaran, Model model, Principal principal, Authentication auth) {

        // Associate the tahunAjaran with exactly two semesters
        List<Semester> newSemesters = semesterService.getAllSemesterByTahunAjaran(tahunAjaran);
        List<Semester> allSemesters = semesterService.getAllSemester();

        for (Semester semester : allSemesters) {
            if (newSemesters.contains(semester)) {
                semesterService.addKelasToSemester(semester, kelas);
            } else {
                semesterService.removeKelasFromSemester(semester, kelas);
            }
        }

        // Associate the kelas with the semesters
        kelasService.updateKelasWithSemesters(kelas, newSemesters);

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "redirect:/kelas";
    }

    @GetMapping("/kelas/hapus/{id}")
    public String deleteKelas(@PathVariable UUID id, RedirectAttributes redirectAttributes, Model model,
            Principal principal, Authentication auth) {
        Kelas kelas = kelasService.getKelasById(id);
        String message = kelasService.deleteKelas(kelas);
        if (message != null) {
            redirectAttributes.addFlashAttribute("error", message);
        } else {
            redirectAttributes.addFlashAttribute("success", "Kelas berhasil dihapus");
        }

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "redirect:/kelas";
    }

    @GetMapping("/kelas/{id}/edit-siswa")
    public String editSiswaKelas(@PathVariable UUID id, Model model, Principal principal, Authentication auth) {
        Kelas kelas = kelasService.getKelasById(id);
        model.addAttribute("kelas", kelas);
        model.addAttribute("tahunAjaran", kelas.getSemesters().stream().findFirst().get().getTahunAjaran());
        model.addAttribute("listSiswa", siswaService.getAllSiswa());

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "kelas/kelas-edit-siswa";
    }

    @PostMapping("/kelas/{id}/edit-siswa")
    public String updateKelasSiswa(@PathVariable UUID id,
            @RequestParam(value = "siswaIds", required = false) List<UUID> siswaIds, Model model, Principal principal,
            Authentication auth) {
        List<Siswa> newSiswas;
        if (siswaIds == null) {
            newSiswas = new ArrayList<>();
        } else {
            newSiswas = siswaService.getSiswasByIds(siswaIds);
        }

        Kelas kelas = kelasService.getKelasById(id);
        List<Siswa> allSiswas = siswaService.getAllSiswa();
        for (Siswa siswa : allSiswas) {
            if (newSiswas.contains(siswa)) {
                siswaService.addKelasToSiswa(siswa, kelas);
            } else {
                siswaService.removeKelasFromSiswa(siswa, kelas);
            }
        }
        kelasService.updateSiswaKelas(kelas, new HashSet<>(newSiswas));

        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);
        return "redirect:/kelas/detail/" + id;
    }

    @GetMapping("/kelas-semester/kelas/siswa-view")
    public String viewKelasSiswa(Authentication auth, Principal principal, Model model,
            RedirectAttributes redirectAttrs) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        // get current siswa
        User user = userService.getUserByUsername(username);
        Siswa siswa = siswaService.getSiswaById(user.getId());

        // get current siswa's list of kelas
        Set<Kelas> listKelas = siswa.getClasses();

        // necessities for header sidebar fragments
        userService.addCurrentUserToModel(username, authorities, model);
        // add list of kelas to show
        if (listKelas.size() == 0) {
            model.addAttribute("error", "Siswa belum bergabung dalam kelas apapun.");
        }
        model.addAttribute("listKelas", listKelas);
        return "kelas/read-kelas-siswa";
    }

    @GetMapping("/kelas-semester/kelas/detail/{id}")
    public String detailKelasSeeSiswa(@PathVariable("id") UUID id, Authentication auth, Principal principal,
            Model model) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        // get model kelas from id
        var kelas = kelasService.getKelasById(id);
        model.addAttribute("kelas", kelas);

        // if kelas has no siswa sends redirect error
        if (kelas.getSiswas() == null || kelas.getSiswas().isEmpty()) {
            model.addAttribute("error", "Belum ada siswa yang didaftarkan.");
        } else {
            model.addAttribute("success", "Berikut adalah daftar siswa yang terdaftar dalam kelas ini.");
        }

        // necessities for header sidebar fragments
        userService.addCurrentUserToModel(username, authorities, model);
        return "kelas/detail-kelas-siswa.html";
    }

    @GetMapping("/kelas-semester/kelas/detail/{id}/view-matpel")
    public String detailKelasSeeMatpel(@PathVariable("id") UUID id, Authentication auth, Principal principal,
            Model model) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        // get model kelas from id
        var kelas = kelasService.getKelasById(id);
        model.addAttribute("kelas", kelas);

        // if kelas has no matpel sends redirect error
        if (kelas.getMataPelajarans() == null || kelas.getMataPelajarans().isEmpty()) {
            model.addAttribute("error", "Belum ada mata pelajaran yang didaftarkan.");
        } else {
            model.addAttribute("success", "");
        }

        // necessities for header sidebar fragments
        userService.addCurrentUserToModel(username, authorities, model);
        return "kelas/detail-kelas-matpel";
    }

}
