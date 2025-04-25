package com.aledguedes.reccos_v3_back.repository;

import com.aledguedes.reccos_v3_back.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    Optional<Owner> findByEmail(String email);
    Optional<Owner> findByUsername(String username);
    long count();
}