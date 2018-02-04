package net.tysonsorensen.userAccount.rest;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.userAccount.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class UserController extends WebMvcConfigurerAdapter {
    private final UserService userService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/welcome").setViewName("welcome");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
    }

    @GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @PostMapping(value = "/createUser")
    public String createUser(@Valid UserForm user, BindingResult result) {

        if(result.hasErrors()) {
            return "registration";
        }

        userService.create(user.getUserName(), user.getLastName(), user.getLastName(), user.getEmail(), user.getPassword());

        return "forward:/login";
    }

    @GetMapping("/registration")
    public String showForm(UserForm userForm) {
        return "registration";
    }


}
