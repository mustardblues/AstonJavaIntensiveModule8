package edu.aston.userservice.controller;

import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.service.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/application")
@Tag(name = "UserService API", description = "API for managing the user database")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user in the database")
    @ApiResponse(
            responseCode = "200", description = "The user has been created in the database",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(responseCode = "409", description = "Failed to create a new user in the database")
    public ResponseEntity<?> create(
            @Parameter(description = "The information about the new user", required = true)
            @RequestBody final UserRequestDTO userRequestDTO) {
        try {
            final UserResponseDTO userResponseDTO = this.userService.createUser(userRequestDTO);

            final EntityModel<UserResponseDTO> entityModel = EntityModel.of(userResponseDTO);

            entityModel.add(linkTo(methodOn(UserController.class).readById(userResponseDTO.getId())).withSelfRel());
            entityModel.add(linkTo(methodOn(UserController.class).update(userResponseDTO.getId(), null)).withRel("update()"));
            entityModel.add(linkTo(methodOn(UserController.class).delete(userResponseDTO.getId())).withRel("delete()"));

            return ResponseEntity.ok(entityModel);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get all users from the database")
    @ApiResponse(responseCode = "200", description = "The list of all users in the database")
    public ResponseEntity<?> readAll() {
        CollectionModel<UserResponseDTO> collectionModel = CollectionModel.of(this.userService.findAll());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user from the database using their ID")
    @ApiResponse(responseCode = "200", description = "The user has been found in the database")
    @ApiResponse(responseCode = "404", description = "Failed to find user in the database")
    public ResponseEntity<?> readById(
            @Parameter(description = "User ID in the database", required = true)
            @PathVariable("id") final Integer id) {
        try {
            final EntityModel<UserResponseDTO> entityModel = EntityModel.of(this.userService.findById(id));

            entityModel.add(linkTo(methodOn(UserController.class).readById(id)).withSelfRel());
            entityModel.add(linkTo(methodOn(UserController.class).update(id, null)).withRel("update()"));
            entityModel.add(linkTo(methodOn(UserController.class).delete(id)).withRel("delete()"));
            
            return ResponseEntity.ok(entityModel);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user information in the database")
    @ApiResponse(responseCode = "200", description = "The user information has been updated")
    @ApiResponse(responseCode = "404", description = "Failed to find user in the database")
    @ApiResponse(responseCode = "409", description = "Failed to updated user information in the database")
    public ResponseEntity<?> update(
            @Parameter(description = "User ID in the database", required = true)
            @PathVariable("id") final Integer id,
            @Parameter(description = "Updated user information", required = true)
            @RequestBody final UserRequestDTO userRequestDTO) {
        try {
            final UserResponseDTO userResponseDTO = this.userService.updateUser(id, userRequestDTO);

            EntityModel<UserResponseDTO> entityModel = EntityModel.of(userResponseDTO);

            entityModel.add(linkTo(methodOn(UserController.class).readById(id)).withSelfRel());
            entityModel.add(linkTo(methodOn(UserController.class).update(id, null)).withRel("update()"));
            entityModel.add(linkTo(methodOn(UserController.class).delete(id)).withRel("delete()"));

            return ResponseEntity.ok(entityModel);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user information in the database")
    @ApiResponse(responseCode = "200", description = "The user has been removed from the database")
    @ApiResponse(responseCode = "404", description = "Failed to find user in the database")
    public ResponseEntity<?> delete(
            @Parameter(description = "User ID in the database", required = true)
            @PathVariable("id") final Integer id) {
        try {
            final boolean isDeleted = this.userService.deleteById(id);

            return ResponseEntity.ok(isDeleted ? "DELETED" : "NOT FOUND");
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
