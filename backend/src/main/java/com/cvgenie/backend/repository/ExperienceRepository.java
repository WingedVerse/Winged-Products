package com.cvgenie.backend.repository;

import com.cvgenie.backend.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience,Long> {
}
