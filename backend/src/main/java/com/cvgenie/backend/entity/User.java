package com.cvgenie.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String profilePic; // store path or name
    @NotBlank(message = "Full Name is required")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;
    @NotNull(message = "Phone Number is required")
    @Column(unique = true)
    private long phone;
//    @NotNull(message = "Address is required")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
    @NotBlank(message = "Password is required")
    private String password;
    @Transient
    private String confirmPassword;

    private boolean isActive;
    private boolean deactivated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt = LocalDateTime.now();}
