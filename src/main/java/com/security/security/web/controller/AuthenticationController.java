package com.security.security.web.controller;

import com.security.security.business.service.AuthenticationService;
import com.security.security.model.User;
import com.security.security.swagger.DescriptionVariables;
import com.security.security.swagger.HTMLResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

import java.io.Serializable;
import java.util.Map;

@Tag(name = DescriptionVariables.AUTHENTICATION)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Registers user", description = "Creates new account for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200,
                    content = @Content(examples = {@ExampleObject(value = "{\"token\": \"value\"}")},
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "400", description = HTMLResponseMessages.HTTP_400, content = @Content),
            @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500, content = @Content),
    })
    public ResponseEntity<?> register(@RequestBody User user) {
        Map<String, Serializable> response = authenticationService.register(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in user", description = "Logs in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200,
                    content = @Content(examples = {@ExampleObject(value = "{\"token\": \"value\"}")},
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403, content = @Content),
            @ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500, content = @Content),
    })
    public ResponseEntity<?> login(@RequestBody User user) {
        Map<String, Serializable> response = authenticationService.login(user);
        return ResponseEntity.ok(response);
    }

}
