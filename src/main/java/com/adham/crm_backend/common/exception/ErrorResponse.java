package com.adham.crm_backend.common.exception;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(LocalDateTime localDateTime,
                            @Schema(description = "HTTP status code", example = "400")
                            int status,
                            @Schema(description = "Error message", example = "email is required")
                            String message,
                            @Schema(description = "Request path", example = "/api/v1/users")
                            String path
) {
}
