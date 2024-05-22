package com.security.security.web.controller;

import com.security.security.business.service.JwtService;
import com.security.security.swagger.HTMLResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class JwtController {

    @Autowired
    JwtService jwtService;

    @PostMapping("/extract-username")
    @Operation(summary = "Get username", description = "Extracts users username from token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200,
                    content = @Content(examples = {@ExampleObject(value = "{\"username\": \"value\"}")},
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403, content = @Content),
            @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500, content = @Content),
    })
    public ResponseEntity<?> extractUsername(@RequestBody String token) {
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(Map.of("username", username));
    }

    @PostMapping("/is-admin")
    @Operation(summary = "User administrator check", description = "Check if user is an administrator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200,
                    content = @Content(examples = {@ExampleObject(value = "{\"is_admin\": \"value\"}")},
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403, content = @Content),
            @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500, content = @Content),
    })
    public ResponseEntity<?> checkIfAdmin(@RequestBody String token) {
        boolean isAdmin = jwtService.isAdmin(token);
        return ResponseEntity.ok(Map.of("is_admin", isAdmin));
    }
}
