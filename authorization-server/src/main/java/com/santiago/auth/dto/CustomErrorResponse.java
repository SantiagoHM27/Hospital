package com.santiago.auth.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje

) {}
