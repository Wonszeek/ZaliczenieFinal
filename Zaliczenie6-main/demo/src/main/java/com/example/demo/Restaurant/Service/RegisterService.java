package com.example.demo.Restaurant.Service;


import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private RegisterRepository regRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(String username, String email, String password) {
        if (emailExists(email)) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        save(user);
        return true;
    }

    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        regRepo.save(user);
    }
    public User loadUserByUsername(String username) {
        return regRepo.findByUsername(username);
    }
    public boolean emailExists(String email) {
        return regRepo.findByEmail(email) != null;
    }
}
