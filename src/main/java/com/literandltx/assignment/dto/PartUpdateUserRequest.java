package com.literandltx.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartUpdateUserRequest {
    @Email(
            message = "Invalid email input, should be real email",
            regexp = "^(.+)@(\\S+)$"
    )
    private String email;

    private String firstname;

    private String lastname;

    @Past(message = "birthdate cannot be in future")
    private LocalDate birthdate;

    private String address;

    private String phoneNumber;
}
