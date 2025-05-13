package com.example.demo.Restaurant.Controllers;

//import com.example.demo.Logins.Register.Register;
//import ch.qos.logback.core.model.Model;
import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String processRegistration(
                      @Valid @ModelAttribute("user") User user,
                           BindingResult result, Model model) {
        if(!user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null,"Hasła nie są identyczne");
        }
        if (registerService.emailExists(user.getEmail())) {
            result.rejectValue("email", null, "Email jest już zarejestrowany");
        }
      if (result.hasErrors()) {
          return "register";
      }
      registerService.save(user);
      return "redirect:/login";


    }



}
