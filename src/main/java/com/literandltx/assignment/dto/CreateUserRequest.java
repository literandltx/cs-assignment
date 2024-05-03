package com.literandltx.assignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder
@Data
public class CreateUserRequest {
    @NotBlank(message = "email cannot be null or blank")
    @Email(
            message = "Invalid email input, should be real email",
            regexp = "^(.+)@(\\S+)$"
    )
    private String email;

    @NotBlank(message = "firstname cannot be null or blank")
    private String firstname;

    @NotBlank(message = "lastname cannot be null or blank")
    private String lastname;

    @Past(message = "birthdate cannot be in future")
    private LocalDate birthdate;

    private String address;

    private String phoneNumber;
}
