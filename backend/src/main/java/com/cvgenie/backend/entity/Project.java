package com.cvgenie.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Project Name is required")
    private String projectName;
    private String projectType;
    @NotBlank(message = "Description is required")
    @Column(length = 10000)
    private String description;
    private List<String> skillSet;
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;
//    @NotNull(message = "End Date is required")
    private LocalDate endDate;
    private boolean stillWorking;

    @ManyToOne
    @JoinColumn(name="portfolio_id",nullable = false)
    @JsonIgnore
    private Portfolio portfolio;
}
