package edu.aston.notificationservice.dto;

public class UserEventDTO {
    private String operation;
    private String email;

    public UserEventDTO(final String operation, final String email) {
        this.operation = operation;
        this.email = email;
    }

    public String getOperation() {
        return this.operation;
    }

    public String getEmail() {
        return this.email;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
