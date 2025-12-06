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
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Language is required")
    private String language;
//    @NotNull(message = "1-5 Expert Level is required")
    private short level;
    @ManyToOne
    @JoinColumn(name="portfolio_id",nullable = false)
    @JsonIgnore
    private Portfolio portfolio;
}
