package edu.aston.userservice.dto;

public class UserEventDTO {
    private String action;
    private String email;

    public UserEventDTO(final String action, final String email) {
        this.action = action;
        this.email = email;
    }

    public String getAction() {
        return this.action;
    }

    public String getEmail() {
        return this.email;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
