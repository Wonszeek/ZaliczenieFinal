package com.example.demo.Restaurant.Service;

import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {

    @Autowired
    private LoginRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public boolean login(String email, String password, HttpSession session) {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return true;
        }
        return false;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}





