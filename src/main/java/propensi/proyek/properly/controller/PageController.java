package propensi.proyek.properly.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import propensi.proyek.properly.service.siswa.SiswaService;

import org.springframework.ui.Model;

@Controller
public class PageController {

    @Autowired
    SiswaService siswaService;

    @RequestMapping("/")
    public String home(Principal principal, Model model) {
        model.addAttribute("siswa", siswaService.getAllSiswa());
        model.addAttribute("name", principal.getName());
        return "home";
    }

}
