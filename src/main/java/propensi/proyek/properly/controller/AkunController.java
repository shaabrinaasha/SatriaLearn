package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import propensi.proyek.properly.Dto.akuns.NewUserRequestDto;
import propensi.proyek.properly.Dto.akuns.UserDto;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.User;
import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.orangtua.OrangTuaService;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.user.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class AkunController {

    @Autowired
    UserService userService;

    @Autowired
    SiswaService siswaService;

    @Autowired
    GuruService guruService;

    @Autowired
    OrangTuaService orangTuaService;

    @GetMapping("akuns")
    public ModelAndView manajemenUser(Principal principal, Authentication auth, Model model) {
        userService.addCurrentUserToModel(auth.getName(), auth.getAuthorities(), model);

        var usersInDb = userService.getAllUser();
        var users  = new ArrayList<UserDto>();

        for (User user : usersInDb) {
            users.add(UserDto.fromUser(user));
        }
        model.addAttribute("users", users);
        return new ModelAndView("/akuns/index", model.asMap());
    }

    @GetMapping("akuns/create")
    public ModelAndView createNewAkunPage(Model model) {
        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        model.addAttribute("selected", "siswa");
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    @PostMapping("akuns/create")
    public ModelAndView createNewAkun(@ModelAttribute NewUserRequestDto user, Model model) {
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        model.addAttribute("akun", user);
        
        switch (user.getPeran()) {
            case "siswa":
                return createNewSiswa(user, model);
            case "guru":
                return createNewGuru(user, model);

            default:
                return createNewOrangTua(user, model);
        }
    }

    private ModelAndView createNewSiswa(NewUserRequestDto user, Model model) {
        model.addAttribute("selected", "siswa");
        if (inputSiswaNotCorrect(user, model)) return new ModelAndView("/akuns/create-akun", model.asMap());

        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();
        var siswa = user.toSiswa();
        siswa.setUsername(username);
        siswa.setPasswordAwal(password);

        siswaService.addSiswa(siswa);

        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Siswa");
        model.addAttribute("nama", siswa.getNama());
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private Boolean inputSiswaNotCorrect(NewUserRequestDto user, Model model) {
        if (user.getNama().length() > 255) {
            model.addAttribute("error", "Nama terlalu panjang");
            return true;
        }
        if (user.getNipd().length() != 9) {
            model.addAttribute("error", "NIPD harus merupakan 9 digit");
            return true;
        }

        if (user.getNisn().length() != 10) {
            model.addAttribute("error", "NISN harus merupakan 10 digit");
            return true;
        }
        return false;
    }

    private ModelAndView createNewGuru(NewUserRequestDto user, Model model) {
        model.addAttribute("selected", "guru");
        if (inputGuruNotCorrect(user, model)) return new ModelAndView("/akuns/create-akun", model.asMap());

        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();
        var guru = user.toGuru();
        guru.setUsername(username);
        guru.setPasswordAwal(password);

        guruService.addGuru(guru);


        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Guru");
        model.addAttribute("nama", guru.getNama());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private Boolean inputGuruNotCorrect(NewUserRequestDto user, Model model) {
        if (user.getNama().length() > 255) {
            model.addAttribute("error", "Nama terlalu panjang");
            return true;
        }
        if (user.getNuptk().length() != 16) {
            model.addAttribute("error", "NUPTK harus merupakan 16 digit");
            return true;
        }
        return false;
    }



    private ModelAndView createNewOrangTua(NewUserRequestDto user, Model model) {
        model.addAttribute("selected", "orang tua");
        if (inputOrangTuaNotCorrect(user, model)) return new ModelAndView("/akuns/create-akun", model.asMap());

        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();

        var siswas = new ArrayList<Siswa>();
        for (UUID id : user.getOrangTuaOf()) {
            siswas.add(siswaService.getSiswaById(id));
        }

        var orangTua = user.toOrangTua(siswas);
        orangTua.setUsername(username);
        orangTua.setPasswordAwal(password);

        orangTuaService.addOrangTua(orangTua);


        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Orang Tua");
        model.addAttribute("nama", orangTua.getNama());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private Boolean inputOrangTuaNotCorrect(NewUserRequestDto user, Model model) {
        if (user.getNama().length() > 255) {
            model.addAttribute("error", "Nama terlalu panjang");
            return true;
        }

        for (UUID id: user.getOrangTuaOf()) {
            try {
                var siswa = siswaService.getSiswaById(id);
                if (siswa.getOrangTua() != null) {
                    model.addAttribute("error", String.format("Orang Tua %s sudah terdaftar di sistem", siswa.getNama()));
                    return true;
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Mohon isi setiap field setiap anak atau hapus");
                return true;
            }
        }
        return false;
    }



}