package com.aledguedes.reccos_v3_back.repository;

import com.aledguedes.reccos_v3_back.model.Federation;
import com.aledguedes.reccos_v3_back.model.User;
import com.aledguedes.reccos_v3_back.model.UserFederation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFederationRepository extends JpaRepository<UserFederation, UUID> {
    List<UserFederation> findByUser(User user);

    List<UserFederation> findByFederation(Federation federation);

    Optional<UserFederation> findByUserAndFederation(User user, Federation federation);

    List<UserFederation> findByFederationId(UUID federationId);
}