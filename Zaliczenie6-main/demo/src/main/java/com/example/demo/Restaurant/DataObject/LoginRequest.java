package com.example.demo.Restaurant.DataObject;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
