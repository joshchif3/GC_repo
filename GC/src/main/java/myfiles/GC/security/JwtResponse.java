package myfiles.GC.security;

public class JwtResponse {
    private final String token;
    private final String role;
    private final String username;
    private final Integer userId;

    public JwtResponse(String token, String role, String username, Integer userId) {
        this.token = token;
        this.role = role;
        this.username = username;
        this.userId = userId;
    }

    // Getters
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public Integer getUserId() { return userId; }
}