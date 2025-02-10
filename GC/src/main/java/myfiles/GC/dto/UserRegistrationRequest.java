package myfiles.GC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationRequest {
    @JsonProperty("username") // Ensures JSON field "username" maps to this field
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private String role; // 'USER' or 'ADMIN'

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
