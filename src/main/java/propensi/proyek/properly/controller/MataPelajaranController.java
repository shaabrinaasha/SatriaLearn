package propensi.proyek.properly.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/matpel")
@Controller
public class MataPelajaranController {
    @GetMapping("")

    @RequestMapping("")
    public String viewMapel(Model model, Principal principal) {
        return "read-manajemen-matpel";
    }

    @RequestMapping("/siswa")
    public String viewMapelSiswa(Model model, Principal principal) {
        return "read-matpel-siswa";
    }

    @RequestMapping("/guru")
    public String viewMapelGuru(Model model, Principal principal) {
        return "read-matpel-guru";
    }

    @RequestMapping("/add")
    public String CreateMatpel() {
        return "form-add-matpel";
    }

    @RequestMapping("/update")
    public String UpdateMatpel() {
        return "form-update-matpel";
    }

}
