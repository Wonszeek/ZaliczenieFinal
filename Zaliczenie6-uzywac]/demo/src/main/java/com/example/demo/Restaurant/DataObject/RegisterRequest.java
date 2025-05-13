package com.example.demo.Restaurant.DataObject;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    @Transient
    private String confirmPassword;

}
