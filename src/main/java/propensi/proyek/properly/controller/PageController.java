package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.user.UserService;

import org.springframework.ui.Model;

@Controller
public class PageController {

        @Autowired
        SiswaService siswaService;

        @Autowired
        UserService userService;

        @RequestMapping("/")
        public String home(
                        Authentication auth, // Untuk authorities
                        Principal principal, // Untuk username
                        Model model) {
                var username = principal.getName();
                var authorities = auth.getAuthorities();

                userService.addCurrentUserToModel(username, authorities, model);

                return "home";
        }

        @RequestMapping("/kelas-semester")
        public String kelasSemester(
                        Authentication auth, // Untuk authorities
                        Principal principal, // Untuk username
                        Model model) {
                var username = principal.getName();
                var authorities = auth.getAuthorities();

                userService.addCurrentUserToModel(username, authorities, model);

                return "kelas-semester";
        }

}
