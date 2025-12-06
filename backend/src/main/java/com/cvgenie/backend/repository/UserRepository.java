package com.cvgenie.backend.repository;

import com.cvgenie.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(long phone);

    User findById(long id);
    User findByEmail(String email);

}
