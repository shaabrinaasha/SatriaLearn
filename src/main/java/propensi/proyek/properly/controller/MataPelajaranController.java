package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
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

    @RequestMapping("/matpel")
    public String viewMatpel(Authentication auth, Model model, Principal principal, RedirectAttributes redirectAttrs) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        Collection<? extends GrantedAuthority> authorities2 = auth.getAuthorities();

        List<String> roles = authorities2.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userRole = roles.get(0);
        System.out.println("ROLE");
        System.out.println(userRole);

        if (userRole.contains("Admin") || userRole.contains("admin")) {
            List<MataPelajaran> listMatpel = mataPelajaranService.getListMatpel();
            System.out.println(listMatpel.size());
            userService.addCurrentUserToModel(username, authorities, model);
            model.addAttribute("listMatpel", listMatpel);
            return "matpel/read-manajemen-matpel";

        } else if (userRole.contains("Guru") || userRole.contains("guru")) {
            User user = userService.getUserByUsername(username);
            Guru guru = guruService.getGuruById(user.getId());
            List<MataPelajaran> listMatpel = new ArrayList<>(guru.getMataPelajarans());
            userService.addCurrentUserToModel(username, authorities, model);
            model.addAttribute("listMatpel", listMatpel);
            return "matpel/read-matpel-guru";
        }
        redirectAttrs.addFlashAttribute("error","Role tidak memiliki akses ke mata pelajaran");
        return "home";
    }

    @RequestMapping("/kelas/matpel/{id}")
    public String viewMatpelSiswa(@PathVariable UUID id, Authentication auth, Model model, Principal principal) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        // need to handle loop
        Kelas kelas = kelasService.getKelasById(id);
        List<MataPelajaran> listMatpel = new ArrayList<>(kelas.getMataPelajarans());
        List<Semester> semesters = new ArrayList<>(kelas.getSemesters());
        Semester semester = semesters.get(0);
        userService.addCurrentUserToModel(username, authorities, model);
        model.addAttribute("listMatpel", listMatpel);
        model.addAttribute("kelas", kelas);
        model.addAttribute("semester", semester);
        return "matpel/read-matpel-siswa";
    }

    @GetMapping("/matpel/add")
    public String addMatpelFormPage(Model model, Principal principal) {
        System.out.println("form page");
        List<Guru> listGuru = guruService.getListGuruActive();
        System.out.println("list GURU");
        System.out.println(listGuru.size());

        List<Kelas> listKelas = kelasService.getAllKelas();
        System.out.println("list KELAS");
        System.out.println(listKelas.size());

        MataPelajaran matpel = new MataPelajaran();

        model.addAttribute("listGuru", listGuru);
        model.addAttribute("listKelas", listKelas);
        model.addAttribute("matpel", matpel);
        return "matpel/form-add-matpel";
    }

    @PostMapping(value = "/matpel/add", params = { "save" })
    public String addMatpelSubmitPage(@ModelAttribute MataPelajaran matpel, String namaMatpel, UUID kelasMatpel,
            UUID guruMatpel, RedirectAttributes redirectAttrs) {
        if (namaMatpel.isEmpty()) {
            redirectAttrs.addFlashAttribute("error",
                    "Mata Pelajaran tidak dapat ditambahkan karena field nama kosong");
            return "redirect:/matpel/add";
        }

        Guru guru = guruService.getGuruById(guruMatpel);
        Kelas kelas = kelasService.getKelasById(kelasMatpel);

        for (MataPelajaran mataPelajaran : kelas.getMataPelajarans()) {
            if (mataPelajaran.getNama().equals(namaMatpel)) {
                redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + namaMatpel
                        + " tidak dapat ditambahkan karena sudah ada di kelas" + kelas.getNama());
                return "redirect:/matpel/add";
            }
        }

        matpel.setNama(namaMatpel);
        matpel.setGuru(guru);
        matpel.setKelas(kelas);
        mataPelajaranService.addMataPelajaran(matpel);
        System.out.println("MATPEL BARU");
        System.out.println(matpel);

        guru.getMataPelajarans().add(matpel);
        kelas.getMataPelajarans().add(matpel);

        return "redirect:/matpel";
    }

    @GetMapping("/matpel/update/{id}")
    public String updateMatpelFormPage(@PathVariable UUID id, Model model, Principal principal) {
        List<Guru> listGuru = guruService.getListGuruActive();
        List<Kelas> listKelas = kelasService.getAllKelas();

        MataPelajaran oldMatpel = mataPelajaranService.getMatpelById(id);
        var matpel = new MataPelajaran();
        matpel.setId(oldMatpel.getId());
        matpel.setNama(oldMatpel.getNama());
        matpel.setGuru(oldMatpel.getGuru());
        matpel.setKelas(oldMatpel.getKelas());

        model.addAttribute("listGuru", listGuru);
        model.addAttribute("listKelas", listKelas);
        model.addAttribute("matpel", matpel);
        return "matpel/form-update-matpel";
    }

    @PostMapping(value = "/matpel/update", params = { "save" })
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
            if (newNama.isEmpty()) {
                redirectAttrs.addFlashAttribute("error", "Mata Pelajaran tidak dapat diubah karena field nama kosong");
                return "redirect:/matpel/update/" + matpel.getId();
            }
            // nama matpel berubah
            // cek apakah di kelas matpel lama sudah ada nama matpel baru
            for (MataPelajaran mataPelajaran : oldKelas.getMataPelajarans()) {
                if (mataPelajaran.getNama().equals(newNama)) {
                    redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + newNama
                            + " tidak dapat ditambahkan karena sudah ada di kelas" + oldKelas.getNama());
                    return "redirect:/matpel/update/" + oldMatpel.getId();
                }
            }
            oldMatpel.setNama(newNama);
        } else if (oldKelas.getId() != newKelas.getId()) {
            // kelas matpel berubah
            // cek apakah di kelas matpel baru sudah ada nama matpel lama
            for (MataPelajaran mataPelajaran : newKelas.getMataPelajarans()) {
                if (mataPelajaran.getNama().equals(oldNama)) {
                    redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + oldNama
                            + " tidak dapat ditambahkan karena sudah ada di kelas" + newKelas.getNama());
                    model.addAttribute("matpel", oldMatpel);
                    return "redirect:/matpel/update/" + oldMatpel.getId();
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
        } else if (!oldNama.equals(matpel.getNama()) && oldKelas.getId() != newKelas.getId()) {
            // nama dan kelas matpel berubah
            // cek apakah di kelas matpel baru sudah ada nama matpel baru
            for (MataPelajaran mataPelajaran : newKelas.getMataPelajarans()) {
                if (mataPelajaran.getNama().equals(newNama)) {
                    redirectAttrs.addFlashAttribute("error", "Mata Pelajaran " + newNama
                            + " tidak dapat ditambahkan karena sudah ada di kelas" + newKelas);
                    model.addAttribute("matpel", oldMatpel);
                    return "redirect:/matpel/update/" + oldMatpel.getId();
                }
            }
            oldMatpel.setNama(newNama);
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

        return "redirect:/matpel";
    }

    @GetMapping("/matpel/delete/{id}")
    public String deleteMapelForm(@PathVariable UUID id, Model model, RedirectAttributes redirectAttrs,
            Principal principal) {
        MataPelajaran matpel = mataPelajaranService.getMatpelById(id);
        mataPelajaranService.deleteMatpel(matpel);
        redirectAttrs.addFlashAttribute("success", "Mapel " + matpel.getNama() + " berhasil dihapus");
        return "redirect:/matpel";
    }

}
