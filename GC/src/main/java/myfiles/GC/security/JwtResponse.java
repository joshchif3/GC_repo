package myfiles.GC.security;

public class JwtResponse {
    private String token;
    private String role;
    private Integer userId;

    public JwtResponse(String token, String role, Integer userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}