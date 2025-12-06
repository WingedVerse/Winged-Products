package com.cvgenie.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Country is required")
    private String country;
    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "Street is required")
    private String street;
    private String doorNo;
    @NotNull(message = "ZipCode is required")
    private int zipCode;

}
