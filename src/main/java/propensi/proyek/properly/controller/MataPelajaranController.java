package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import propensi.proyek.properly.model.MataPelajaran;
import propensi.proyek.properly.service.MataPelajaranService;

@RequestMapping("/matpel")
@Controller
public class MataPelajaranController {
    @Autowired
    private MataPelajaranService mataPelajaranService;

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

    @RequestMapping("/add")
    public String CreateMatpel(Model model, Principal principal) {
        // List<MataPelajaran> listGuru = mataPelajaranService.addMatpel();
        // System.out.println("INIIII HAHAHAHAHHAHA-------------------------------------");
        // System.out.println(listGuru.size());
        // model.addAttribute("listMatpel", listGuru);
        return "matpel/form-add-matpel";
    }

    @RequestMapping("/update")
    public String UpdateMatpel() {
        return "matpel/form-update-matpel";
    }

}
