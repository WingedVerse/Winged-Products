package com.cvgenie.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OtpEntity {
    @Id
    private String mobile;

    private String otp;
    private LocalDateTime expiryTime;
}
