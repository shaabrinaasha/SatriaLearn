package propensi.proyek.properly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import propensi.proyek.properly.repository.SiswaDb;
import propensi.proyek.properly.service.SiswaService;

import org.springframework.ui.Model;

@Controller
public class PageController {

    @Autowired
    SiswaService siswaService;
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("siswa", siswaService.getAllSiswa());
        return "home";
    }
    
}
