package com.cvgenie.backend.repository;

import com.cvgenie.backend.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    Portfolio findByUserId(long userId);
    boolean existsByUserId(long userId);
}
