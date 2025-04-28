package com.aledguedes.reccos_v3_back.repository;

import com.aledguedes.reccos_v3_back.model.Role;
import com.aledguedes.reccos_v3_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    List<User> findByFederationIdAndRole(UUID federationId, Role role);
}