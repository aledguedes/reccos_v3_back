package com.aledguedes.reccos_v3_back.repository;

import com.aledguedes.reccos_v3_back.model.Federation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FederationRepository extends JpaRepository<Federation, UUID> {
    Optional<Federation> findByName(String name);
}