package com.santiago.auth.services;
import java.util.Set;

import com.santiago.auth.dto.UsuarioRequest;
import com.santiago.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}