package com.blackpanthers.repository;

import com.blackpanthers.model.Cricketer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CricketerRepository extends JpaRepository<Cricketer, Long> {
    boolean existsByEmail(String email);
    boolean existsByJerseyNumber(Integer jerseyNumber);
    Optional<Cricketer> findByEmail(String email);
}

