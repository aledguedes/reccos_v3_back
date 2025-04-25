package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.LoginRequestDTO;
import com.aledguedes.reccos_v3_back.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}