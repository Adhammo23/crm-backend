package com.adham.crm_backend.common.documentation.annotation;

import com.adham.crm_backend.common.exception.ErrorResponse;
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
        @ApiResponse(responseCode = "200", description = "OK"), // schema من الـ return type
        @ApiResponse(responseCode = "404", description = "Not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface GetApiResponses {
}