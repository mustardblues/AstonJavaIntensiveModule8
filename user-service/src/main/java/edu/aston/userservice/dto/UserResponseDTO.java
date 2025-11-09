package edu.aston.userservice.dto;

import edu.aston.userservice.entity.User;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDTO {
    @Schema(description = "User ID in the database")
    private final Integer id;

    @Schema(description = "Username in the database")
    private final String name;

    @Schema(description = "User email in the database")
    private final String email;

    @Schema(description = "User age in the database")
    private final Integer age;

    @Schema(description = "Date of user creation in the database")
    private final LocalDateTime createdAt;

    public UserResponseDTO(final int id, final String name, final String email, final int age, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public UserResponseDTO(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.createdAt = user.getCreatedAt();
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getAge() {
        return this.age;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + this.id +
                ", name=" + this.name +
                ", email=" + this.email +
                ", age=" + this.age +
                ", createdAt=" + this.createdAt +
                "}";
    }
}
