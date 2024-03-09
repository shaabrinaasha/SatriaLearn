package propensi.proyek.properly.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import propensi.proyek.properly.Dto.login.LoginDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("login")
    public ModelAndView login(Principal principal, Model model) {

        if (principal != null) return new ModelAndView("redirect:");

        model.addAttribute("loginInfo", new LoginDto());
        return new ModelAndView("/login/index", model.asMap());
    }
    
    
    
}
