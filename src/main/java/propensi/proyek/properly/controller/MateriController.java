package propensi.proyek.properly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.kelas.KelasService;
import propensi.proyek.properly.service.matapelajaran.MataPelajaranService;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.user.UserService;

@Controller
public class MateriController {
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

    // siswa
    
    
    // guru

}
