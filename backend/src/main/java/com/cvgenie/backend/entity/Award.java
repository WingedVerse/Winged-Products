package com.cvgenie.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Organization is required")
    private String organization;
    @NotBlank(message = "Award is required")
    @Column( length = 10000)
    private String award;
    @ManyToOne
    @JoinColumn(name="portfolio_id",nullable = false)
    @JsonIgnore
    private Portfolio portfolio;
}
