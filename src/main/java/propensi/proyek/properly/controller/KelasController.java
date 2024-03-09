package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.User;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class KelasController {
    @Autowired
    UserService userService;

    @Autowired
    SiswaService siswaService;

    @GetMapping("/kelas/siswa-view")
    public String viewKelasSiswa(Authentication auth, Principal principal, Model model,
            RedirectAttributes redirectAttrs) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();

        // get current siswa
        User user = userService.getUserByUsername(username);
        Siswa siswa = siswaService.getSiswaById(user.getId());

        // get current siswa's list of kelas
        Set<Kelas> listKelas = siswa.getClasses();

        // necessities for header sidebar fragments
        userService.addCurrentUserToModel(username, authorities, model);
        // add list of kelas to show
        if (listKelas.size() == 0) {
            model.addAttribute("error", "Siswa belum bergabung dalam kelas apapun.");
        }
        model.addAttribute("listKelas", listKelas);
        return "/kelas/read-kelas-siswa";
    }

}
