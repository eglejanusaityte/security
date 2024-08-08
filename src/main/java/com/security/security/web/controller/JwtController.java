package com.security.security.web.controller;

import com.security.security.business.service.JwtService;
import com.security.security.handler.CustomErrorResponse;
import com.security.security.swagger.DescriptionVariables;
import com.security.security.swagger.HTMLResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
@Tag(name = DescriptionVariables.JWT)
@ApiResponses(value = {@ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500, content = @Content), @ApiResponse(responseCode = "400", description = HTMLResponseMessages.HTTP_400, content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))), @ApiResponse(responseCode = "404", description = HTMLResponseMessages.HTTP_404, content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))), @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403, content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))})
public class JwtController {

    @Autowired
    JwtService jwtService;

    @PostMapping("/extract-username")
    @Operation(summary = "Get username", description = "Extracts users username from token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200, content = @Content(examples = {@ExampleObject(value = "username")}, mediaType = MediaType.APPLICATION_JSON_VALUE)),})
    public ResponseEntity<?> extractUsername(@RequestBody String token) {
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(Map.of("username", username));
    }

    @PostMapping("/is-admin")
    @Operation(summary = "User administrator check", description = "Check if user is an administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200, content = @Content(examples = {@ExampleObject(value = "{\"is_admin\": \"value\"}")}, mediaType = MediaType.APPLICATION_JSON_VALUE)),})
    public ResponseEntity<?> checkIfAdmin(@RequestBody String token) {
        boolean isAdmin = jwtService.isAdmin(token);
        return ResponseEntity.ok(Map.of("is_admin", isAdmin));
    }
}
