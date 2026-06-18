package com.santiago.auth.services;

import com.santiago.auth.dto.LoginRequest;
import com.santiago.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}

