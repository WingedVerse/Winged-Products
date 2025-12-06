package com.cvgenie.backend.repository;

import com.cvgenie.backend.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
}
