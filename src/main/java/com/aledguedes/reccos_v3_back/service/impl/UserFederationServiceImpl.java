package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.exception.DuplicateResourceException;
import com.aledguedes.reccos_v3_back.exception.NotFoundException;
import com.aledguedes.reccos_v3_back.mapper.FederationMapper;
import com.aledguedes.reccos_v3_back.mapper.UserMapper;
import com.aledguedes.reccos_v3_back.model.Federation;
import com.aledguedes.reccos_v3_back.model.User;
import com.aledguedes.reccos_v3_back.model.UserFederation;
import com.aledguedes.reccos_v3_back.repository.FederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserFederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserRepository;
import com.aledguedes.reccos_v3_back.service.UserFederationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserFederationServiceImpl implements UserFederationService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private FederationRepository federationRepository;

        @Autowired
        private UserFederationRepository userFederationRepository;

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private FederationMapper federationMapper;

        @Override
        public void associateUserToFederation(UUID userId, UUID federationId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFoundException("User not found"));
                Federation federation = federationRepository.findById(federationId)
                                .orElseThrow(() -> new NotFoundException("Federation not found"));

                if (userFederationRepository.findByUserAndFederation(user, federation).isPresent()) {
                        throw new DuplicateResourceException("User is already associated with this federation");
                }

                UserFederation userFederation = new UserFederation();
                userFederation.setUser(user);
                userFederation.setFederation(federation);
                userFederationRepository.save(userFederation);
        }

        @Override
        public void removeUserFromFederation(UUID userId, UUID federationId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFoundException("User not found"));
                Federation federation = federationRepository.findById(federationId)
                                .orElseThrow(() -> new NotFoundException("Federation not found"));

                UserFederation userFederation = userFederationRepository.findByUserAndFederation(user, federation)
                                .orElseThrow(() -> new NotFoundException("Association not found"));
                userFederationRepository.delete(userFederation);
        }

        @Override
        public List<FederationDTO> getFederationsByUser(UUID userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFoundException("User not found"));
                return userFederationRepository.findByUser(user).stream()
                                .map(userFederation -> federationMapper.toFederationDTO(userFederation.getFederation()))
                                .collect(Collectors.toList());
        }

        @Override
        public List<UserDTO> getUsersByFederation(UUID federationId) {
                Federation federation = federationRepository.findById(federationId)
                                .orElseThrow(() -> new NotFoundException("Federation not found"));
                return userFederationRepository.findByFederation(federation).stream()
                                .map(userFederation -> userMapper.toUserDTO(userFederation.getUser()))
                                .collect(Collectors.toList());
        }
}