package myfiles.GC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationRequest {
    @JsonProperty("username") // Ensures JSON field "username" maps to this field
    private String username;

    @JsonProperty("email") // Add email field
    private String email;

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

    public String getEmail() { // Add getter for email
        return email;
    }

    public void setEmail(String email) { // Add setter for email
        this.email = email;
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