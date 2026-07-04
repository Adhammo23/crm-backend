package com.adham.crm_backend.exception;
import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime localDateTime,
                            int status,
                            String message,
                            String path
) {
}
