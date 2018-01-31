package net.tysonsorensen.userAccount.rest;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.userAccount.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping(value = {"/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }

    @GetMapping(value = {"/", "/home"})
    public String home(Model model) {
        return "home";
    }
}
