package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import propensi.proyek.properly.Dto.akuns.NewUserRequestDto;
import propensi.proyek.properly.Dto.akuns.UserDto;
import propensi.proyek.properly.model.User;
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

    @GetMapping("akuns")
    public ModelAndView manajemenUser(Principal principal, Model model) {
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
        model.addAttribute("siswas", siswaService.getAllSiswa());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    @PostMapping("akuns/create")
    public ModelAndView createNewAkun(@ModelAttribute NewUserRequestDto user, Model model) {
        
        switch (user.getPeran()) {
            case "siswa":
                return createNewSiswa(user, model);
            case "guru":
                createNewGuru();                
                break;
        
            default:
                createNewOrangTua();
                break;
        }

        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private ModelAndView createNewSiswa(NewUserRequestDto user, Model model) {
        // TODO: null check
        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();
        var siswa = user.toSiswa();
        siswa.setUsername(username);
        siswa.setPasswordAwal(password);

        siswaService.addSiswa(siswa);

        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Siswa");
        model.addAttribute("nama", siswa.getNama());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private void createNewGuru() {

    }

    private void createNewOrangTua() {

    }
    
    
    
}