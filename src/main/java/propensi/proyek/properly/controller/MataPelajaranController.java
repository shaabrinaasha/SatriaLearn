package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.kelas.KelasService;
import propensi.proyek.properly.service.matapelajaran.*;

@RequestMapping("/matpel")
@Controller
public class MataPelajaranController {
    @Autowired
    private MataPelajaranService mataPelajaranService;
    @Autowired
    private GuruService guruService;
    @Autowired
    private KelasService kelasService;

    @RequestMapping("")
    public String viewMatpel(Model model, Principal principal) {
        List<MataPelajaran> listMatpel = mataPelajaranService.getListMatpel();
        System.out.println(listMatpel.size());
        model.addAttribute("listMatpel", listMatpel);
        return "matpel/read-manajemen-matpel";
    }

    @RequestMapping("/siswa")
    public String viewMatpelSiswa(Model model, Principal principal) {
        return "matpel/read-matpel-siswa";
    }

    @RequestMapping("/guru")
    public String viewMatpelGuru(Model model, Principal principal) {
        return "matpel/read-matpel-guru";
    }

    @GetMapping("/add")
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

    @PostMapping(value = "/add", params = {"save"})
    public String addMatpelSubmitPage(@ModelAttribute MataPelajaran matpel, String namaMatpel, UUID kelasMatpel, UUID guruMatpel, RedirectAttributes redirectAttrs) {
        System.out.println(matpel);

        System.out.println("submit page");

        Guru guru = guruService.getGuruById(guruMatpel);
        Kelas kelas = kelasService.getKelasById(kelasMatpel);

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

    @GetMapping("/update/{id}")
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

    @PostMapping(value = "/update", params = {"save"})
    public String updateMatpelSubmitPage(@ModelAttribute MataPelajaran matpel, RedirectAttributes redirectAttrs) {        
        MataPelajaran oldMatpel = mataPelajaranService.getMatpelById(matpel.getId());
        String oldNama = oldMatpel.getNama();
        Guru oldGuru = oldMatpel.getGuru();
        Kelas oldKelas = oldMatpel.getKelas();

        String newNama = matpel.getNama();
        Guru newGuru = matpel.getGuru();
        Kelas newKelas = matpel.getKelas();

        if (!oldNama.equals(matpel.getNama())){
            oldMatpel.setNama(newNama);
        }
        if (oldGuru.getId() != newGuru.getId()) {
            Iterator<MataPelajaran> iterator = oldGuru.getMataPelajarans().iterator();
                while (iterator.hasNext()) {
                    MataPelajaran mataPelajaran = iterator.next();
                    if (mataPelajaran.getId().equals(matpel.getId())) {
                        iterator.remove(); // Menghapus elemen dari koleksi menggunakan iterator
                    }
                }
            oldMatpel.setGuru(newGuru);
            newGuru.getMataPelajarans().add(mataPelajaranService.getMatpelById(matpel.getId()));
        }
        if (oldKelas.getId() != newKelas.getId()) {
            Iterator<MataPelajaran> iterator = oldKelas.getMataPelajarans().iterator();
                while (iterator.hasNext()) {
                    MataPelajaran mataPelajaran = iterator.next();
                    if (mataPelajaran.getId().equals(matpel.getId())) {
                        iterator.remove(); // Menghapus elemen dari koleksi menggunakan iterator
                    }
                }
            oldMatpel.setKelas(newKelas);
            newKelas.getMataPelajarans().add(mataPelajaranService.getMatpelById(matpel.getId()));
        }
      
        // save matpel
        mataPelajaranService.addMataPelajaran(oldMatpel);
        
        return "redirect:/matpel";
    }

}
