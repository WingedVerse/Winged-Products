package com.cvgenie.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Education
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Institute Name is required")
    private String institute;
    @NotBlank(message = "Degree Name is required")
    private String degree;
    @NotBlank(message = "Branch Name is required")
    private String branch;
    @NotBlank(message = "Location is required")
    private String location;
    @NotBlank(message = "Description is required")
    @Column(length = 10000)
    private String description;
    @NotNull(message = "Percentage is required")
    private int percentage;
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;
//    @NotNull(message = "End Date is required")
    private LocalDate endDate;
    private boolean stillPursuing;
    @ManyToOne
    @JoinColumn(name="portfolio_id",nullable = false)
    @JsonIgnore
    private Portfolio portfolio;

}
