package com.example.demo.Restaurant.DataObject;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String message;
}
