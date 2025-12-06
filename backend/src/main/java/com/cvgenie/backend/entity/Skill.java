package com.cvgenie.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Skill Name is required")
    private String skill;
//    @NotNull(message = "1-5 Skill Expert Level is required")
    private byte level;
    @ManyToOne
    @JoinColumn(name="portfolio_id",nullable = false)
    @JsonIgnore
    private Portfolio portfolio;
}
