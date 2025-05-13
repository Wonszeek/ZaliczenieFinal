package com.example.demo.Restaurant.Controllers;

import com.example.demo.Restaurant.DataObject.AuthResponse;
import com.example.demo.Restaurant.DataObject.LoginRequest;
import com.example.demo.Restaurant.DataObject.RegisterRequest;
import com.example.demo.Restaurant.DataObject.RegisterResponse;
import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Service.LoginService;
import com.example.demo.Restaurant.Service.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private LoginService loginService;
    private RegisterService registerService;

    @Autowired
    public AuthController(RegisterService registerService, LoginService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        boolean isAuthenticated = loginService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                session
        );

        if (isAuthenticated) {
            User user = (User) session.getAttribute("user");
            return ResponseEntity.ok(new AuthResponse(
                    true,
                    "Login successful",
                    user.getEmail(),
                    user.getUsername()
            ));
        } else {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(false, "Invalid email or password", null, null)
            );
        }
    }
@PostMapping("/register")
public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    new RegisterResponse(false, "Hasła nie są identyczne")
            );
        }
        boolean success = registerService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );
        return success ? ResponseEntity.ok(new RegisterResponse(true, "Rejestracja powiodła się!"))
                : ResponseEntity.badRequest().body(new RegisterResponse(false, "Rejestracja nie powiodła się"));
}


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        loginService.logout(session);
        return ResponseEntity.ok(new AuthResponse(true, "Logout successful", null, null));
    }


}