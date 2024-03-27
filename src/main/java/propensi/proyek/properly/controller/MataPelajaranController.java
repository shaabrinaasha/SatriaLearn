package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.MataPelajaran;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.User;
import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.kelas.KelasService;
import propensi.proyek.properly.service.matapelajaran.*;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.user.UserService;

@Controller
public class MataPelajaranController {
    @Autowired
    private MataPelajaranService mataPelajaranService;
    @Autowired
    private GuruService guruService;
    @Autowired
    private SiswaService siswaService;
    @Autowired
    private KelasService kelasService;
    @Autowired
    UserService userService;

    // admin 
    @RequestMapping("/matpel-admin")
    public String viewMatpelAdmin(Authentication auth, Model model, Principal principal, RedirectAttributes redirectAttrs) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        List<MataPelajaran> listMatpel = mataPelajaranService.getListMatpel();
        userService.addCurrentUserToModel(username, authorities, model);

        Collections.sort(listMatpel, Comparator.comparing(MataPelajaran::getNama));

        model.addAttribute("listMatpel", listMatpel);
        return "matpel/admin/read-manajemen-matpel";
    }

    @GetMapping("/matpel-admin/add")
    public String addMatpelFormPage(Model model, Principal principal) {
        List<Guru> listGuru = guruService.getListGuruActive();
        List<Kelas> listKelas = kelasService.getAllKelas();

        MataPelajaran matpel = new MataPelajaran();
        Collections.sort(listGuru, Comparator.comparing(Guru::getNama));
        Collections.sort(listKelas, Comparator.comparing(Kelas::getNama));

        model.addAttribute("listGuru", listGuru);
        model.addAttribute("listKelas", listKelas);
        model.addAttribute("matpel", matpel);
        return "matpel/admin/form-add-matpel";
    }

    @PostMapping(value = "/matpel-admin/add", params = { "save" })
    public String addMatpelSubmitPage(@ModelAttribute MataPelajaran matpel, String namaMatpel, UUID kelasMatpel,
            UUID guruMatpel, RedirectAttributes redirectAttrs) {
        if (namaMatpel.isEmpty()) {
            redirectAttrs.addFlashAttribute("error",
                    "Mata Pelajaran tidak dapat ditambahkan karena field nama kosong");
            return "redirect:/matpel-admin/add";
        }

        Guru guru = guruService.getGuruById(guruMatpel);
        Kelas kelas = kelasService.getKelasById(kelasMatpel);
        var tahunAjaran = "";  
        for (Semester s : kelas.getSemesters()) {
            tahunAjaran = s.getTahunAjaran();
        }

        for (MataPelajaran mataPelajaran : kelas.getMataPelajarans()) {
            if (mataPelajaran.getNama().equals(namaMatpel)) {
                redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + namaMatpel
                        + " tidak dapat ditambahkan karena sudah ada di kelas " + kelas.getNama() + " " + tahunAjaran);
                return "redirect:/matpel-admin/add";
            }
        }

        matpel.setNama(namaMatpel);
        matpel.setGuru(guru);
        matpel.setKelas(kelas);
        mataPelajaranService.addMataPelajaran(matpel);

        guru.getMataPelajarans().add(matpel);
        kelas.getMataPelajarans().add(matpel);

        redirectAttrs.addFlashAttribute("success", "Mata Pelajaran " + matpel.getNama() + " berhasil dibuat");
        return "redirect:/matpel-admin";
    }

    @GetMapping("/matpel-admin/update/{id}")
    public String updateMatpelFormPage(@PathVariable UUID id, Model model, Principal principal) {
        List<Guru> listGuru = guruService.getListGuruActive();
        List<Kelas> listKelas = kelasService.getAllKelas();

        MataPelajaran oldMatpel = mataPelajaranService.getMatpelById(id);
        var matpel = new MataPelajaran();
        matpel.setId(oldMatpel.getId());
        matpel.setNama(oldMatpel.getNama());
        matpel.setGuru(oldMatpel.getGuru());
        matpel.setKelas(oldMatpel.getKelas());

        Collections.sort(listGuru, Comparator.comparing(Guru::getNama));
        Collections.sort(listKelas, Comparator.comparing(Kelas::getNama));

        model.addAttribute("listGuru", listGuru);
        model.addAttribute("listKelas", listKelas);
        model.addAttribute("matpel", matpel);
        return "matpel/admin/form-update-matpel";
    }

    @PostMapping(value = "/matpel-admin/update", params = { "save" })
    public String updateMatpelSubmitPage(@ModelAttribute MataPelajaran matpel, Model model,
            RedirectAttributes redirectAttrs) {
        MataPelajaran oldMatpel = mataPelajaranService.getMatpelById(matpel.getId());
        String oldNama = oldMatpel.getNama();
        Guru oldGuru = oldMatpel.getGuru();
        Kelas oldKelas = oldMatpel.getKelas();

        String newNama = matpel.getNama();
        Guru newGuru = matpel.getGuru();
        Kelas newKelas = matpel.getKelas();

        if (!oldNama.equals(newNama)) {
            if (oldKelas.getId() != newKelas.getId()) {
                // nama dan kelas matpel berubah
                // cek apakah di kelas matpel baru sudah ada nama matpel baru
                for (MataPelajaran mataPelajaran : newKelas.getMataPelajarans()) {
                    if (mataPelajaran.getNama().equals(newNama)) {
                        var tahunAjaran = "";  
                        for (Semester s : newKelas.getSemesters()) {
                            tahunAjaran = s.getTahunAjaran();
                        }
                        redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + newNama
                                + " tidak dapat ditambahkan karena sudah ada di kelas" + newKelas.getNama() + " " + tahunAjaran);
                        model.addAttribute("matpel", oldMatpel);
                        return "redirect:/matpel-admin/update/" + oldMatpel.getId();
                    }
                }
                oldMatpel.setNama(newNama);
            } else {
                if (newNama.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error",
                            "Mata Pelajaran tidak dapat diubah karena field nama kosong");
                    return "redirect:/matpel-admin/update/" + matpel.getId();
                }
                // nama matpel berubah
                // cek apakah di kelas matpel lama sudah ada nama matpel baru
                for (MataPelajaran mataPelajaran : oldKelas.getMataPelajarans()) {
                    if (mataPelajaran.getNama().equals(newNama)) {
                        var tahunAjaran = "";  
                        for (Semester s : oldKelas.getSemesters()) {
                            tahunAjaran = s.getTahunAjaran();
                        }
                        redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + newNama
                                + " tidak dapat ditambahkan karena sudah ada di kelas" + oldKelas.getNama() + " " + tahunAjaran);
                        return "redirect:/matpel-admin/update/" + oldMatpel.getId();
                    }
                }
                oldMatpel.setNama(newNama);
            }
        }
        if (oldKelas.getId() != newKelas.getId()) {
            // kelas matpel berubah
            // cek apakah di kelas matpel baru sudah ada nama matpel lama
            for (MataPelajaran mataPelajaran : newKelas.getMataPelajarans()) {
                if (mataPelajaran.getNama().equals(oldNama)) {
                    var tahunAjaran = "";  
                    for (Semester s : newKelas.getSemesters()) {
                        tahunAjaran = s.getTahunAjaran();
                    }
                    redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + oldNama
                            + " tidak dapat ditambahkan karena sudah ada di kelas" + newKelas.getNama() + " " + tahunAjaran);
                    model.addAttribute("matpel", oldMatpel);
                    return "redirect:/matpel-admin/update/" + oldMatpel.getId();
                }
            }
            Iterator<MataPelajaran> iterator = oldKelas.getMataPelajarans().iterator();
            while (iterator.hasNext()) {
                MataPelajaran mataPelajaran = iterator.next();
                if (mataPelajaran.getId().equals(matpel.getId())) {
                    iterator.remove();
                }
            }
            oldMatpel.setKelas(newKelas);
            newKelas.getMataPelajarans().add(mataPelajaranService.getMatpelById(matpel.getId()));
        }
        if (oldGuru.getId() != newGuru.getId()) {
            Iterator<MataPelajaran> iterator = oldGuru.getMataPelajarans().iterator();
            while (iterator.hasNext()) {
                MataPelajaran mataPelajaran = iterator.next();
                if (mataPelajaran.getId().equals(matpel.getId())) {
                    iterator.remove();
                }
            }
            oldMatpel.setGuru(newGuru);
            newGuru.getMataPelajarans().add(mataPelajaranService.getMatpelById(matpel.getId()));
        }

        // save matpel
        mataPelajaranService.addMataPelajaran(oldMatpel);
        redirectAttrs.addFlashAttribute("success", "Mata Pelajaran berhasil diubah");
        return "redirect:/matpel-admin";
    }

    @GetMapping("/matpel-admin/delete/{id}")
    public String deleteMatpelForm(@PathVariable UUID id, Model model, RedirectAttributes redirectAttrs,
            Principal principal) {
        MataPelajaran matpel = mataPelajaranService.getMatpelById(id);
        mataPelajaranService.deleteMatpel(matpel);
        redirectAttrs.addFlashAttribute("success", "Mata Pelajaran " + matpel.getNama() + " berhasil dihapus");
        return "redirect:/matpel-admin";
    }

    // guru
    @RequestMapping("/matpel-guru")
    public String viewMatpel(Authentication auth, Model model, Principal principal, RedirectAttributes redirectAttrs) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        User user = userService.getUserByUsername(username);
        Guru guru = guruService.getGuruById(user.getId());
        List<MataPelajaran> listMatpel = new ArrayList<>(guru.getMataPelajarans());
        userService.addCurrentUserToModel(username, authorities, model);

        Collections.sort(listMatpel, Comparator.comparing(MataPelajaran::getNama));

        model.addAttribute("listMatpel", listMatpel);
        return "matpel/guru/read-matpel-guru";
    }

    // siswa
    @RequestMapping("/kelas/matpel/{id}")
    public String viewMatpelSiswa(@PathVariable UUID id, Authentication auth, Model model, Principal principal) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();
        Kelas kelas = kelasService.getKelasById(id);
        List<MataPelajaran> listMatpel = new ArrayList<>(kelas.getMataPelajarans());
        List<Semester> semesters = new ArrayList<>(kelas.getSemesters());
        Semester semester = semesters.get(0);

        Collections.sort(listMatpel, Comparator.comparing(MataPelajaran::getNama));

        userService.addCurrentUserToModel(username, authorities, model);
        model.addAttribute("listMatpel", listMatpel);
        model.addAttribute("kelas", kelas);
        model.addAttribute("semester", semester);
        return "matpel/siswa/read-matpel-siswa";
    }

}
