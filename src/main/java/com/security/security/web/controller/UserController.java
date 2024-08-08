package com.security.security.web.controller;

import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.JwtService;
import com.security.security.business.service.UserService;
import com.security.security.dto.UserDTO;
import com.security.security.handler.CustomErrorResponse;
import com.security.security.model.User;
import com.security.security.swagger.DescriptionVariables;
import com.security.security.swagger.HTMLResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;

@Controller
@Validated
@RequestMapping("/api/v1/users")
@Tag(name = DescriptionVariables.USER)
@SecurityRequirement(name = "Authorization")
@ApiResponses(value = {@ApiResponse(responseCode = "500", description = HTMLResponseMessages.HTTP_500, content = @Content), @ApiResponse(responseCode = "400", description = HTMLResponseMessages.HTTP_400, content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))), @ApiResponse(responseCode = "404", description = HTMLResponseMessages.HTTP_404, content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))), @ApiResponse(responseCode = "403", description = HTMLResponseMessages.HTTP_403, content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))})
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns list of existing users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200, content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))}),})
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        Page<UserDTO> users = userService.getUsers(page);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user", description = "Returns information about specific user by username")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200, content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),})
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        UserDTO user = userService.getUser(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes user by given user id")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = HTMLResponseMessages.HTTP_204_WITHOUT_DATA, content = @Content),})
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) throws AccessDeniedException {
        String token = request.getHeader("Authorization");
        String username = jwtService.extractUsername(token);
        boolean role = jwtService.isAdmin(token);
        userService.deleteUser(id, username, role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates user information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = HTMLResponseMessages.HTTP_200_WITHOUT_DATA, content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}), @ApiResponse(responseCode = "400", description = HTMLResponseMessages.HTTP_400, content = @Content),})
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserDAO userDAO, HttpServletRequest request) throws AccessDeniedException {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        boolean role = jwtService.isAdmin(token);
        userService.updateUser(userDAO, username, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
