package com.santiago.commons.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}

