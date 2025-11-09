package edu.aston.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequestDTO {
    @Schema(description = "Username from request")
    private String name;

    @Schema(description = "User email from request")
    private String email;

    @Schema(description = "User age from request")
    private Integer age;

    public UserRequestDTO() {}

    public UserRequestDTO(final String name, final String email, final int age) {
        this.name = name;
        this.email = email;
        this.age = age;
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
}
