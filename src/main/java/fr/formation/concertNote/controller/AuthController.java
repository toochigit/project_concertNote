package fr.formation.concertNote.controller;

import fr.formation.concertNote.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


import fr.formation.concertNote.model.User;
import fr.formation.concertNote.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDto") UserDto dto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        userService.register(dto.getUsername(), dto.getEmail(), dto.getPassword());
        return "redirect:/login";
    }


    @GetMapping("/login")
        public String showLoginForm() {
            return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        var user = userService.authenticate(username, password);
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return "redirect:/concerts";
        } else {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe invalide");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
