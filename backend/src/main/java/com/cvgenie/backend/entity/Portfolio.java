package com.cvgenie.backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "First Name is required")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotBlank(message = "Designation is required")
    private String designation;
    @NotBlank(message = "Email is required")
    private String email;
    @NotNull(message = "Phone number is required")
    private String phone;
    private String image; // store path or name
    private String linkedIn;
    @NotNull(message = "Address is required")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
    @NotBlank(message = "summary is required")
    @Column(length = 10000)
    private String summary;
    
    @NotNull(message = "Skills required")
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experience;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> education;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Award> awards;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


}
