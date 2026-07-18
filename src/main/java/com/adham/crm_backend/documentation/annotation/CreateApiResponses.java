package com.adham.crm_backend.documentation.annotation;

import com.adham.crm_backend.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DefaultApiResponses
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"), // schema من الـ return type
        @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflict - resource already exists",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface CreateApiResponses {
}