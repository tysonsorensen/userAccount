package net.tysonsorensen.userAccount.web.controllers;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.userAccount.services.UserService;
import net.tysonsorensen.userAccount.web.helpers.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/login")
    public ModelAndView login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return new ModelAndView("login", model.asMap());
    }

    @GetMapping(value = "/registration")
    public ModelAndView registration(Model model) {
        model.addAttribute("userForm", new UserForm());
        return new ModelAndView("registration", model.asMap());
    }

    @PostMapping(value = "/registration")
    public ModelAndView createUser(@Valid UserForm user, BindingResult result) {
        try {
            userService.create(user.getUserName(), user.getLastName(), user.getLastName(), user.getEmail(), user.getPassword());
        } catch (UserService.UserNameInvalid userNameInvalid) {
            result.addError(new FieldError("UserForm", "userName", "user name already taken"));
        }

        if(result.hasErrors()) {
            return new ModelAndView("registration", Collections.emptyMap());
        }

        return new ModelAndView("login", Collections.emptyMap());
    }

}
