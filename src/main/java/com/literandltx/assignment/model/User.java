package com.literandltx.assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    public User() { }

    public User(
            final String email,
            final String firstname,
            final String lastname,
            final LocalDate birthdate
    ) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public User(
            String email,
            String firstname,
            String lastname,
            LocalDate birthdate,
            String address,
            String phoneNumber
    ) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
