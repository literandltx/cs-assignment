package com.literandltx.assignment.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
}
