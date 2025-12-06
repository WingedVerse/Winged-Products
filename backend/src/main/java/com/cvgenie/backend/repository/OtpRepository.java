package com.cvgenie.backend.repository;

import com.cvgenie.backend.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OtpEntity,String> {
}
