package myfiles.GC.controller;

import myfiles.GC.dto.UserLoginRequest;
import myfiles.GC.dto.UserRegistrationRequest;
import myfiles.GC.model.User;
import myfiles.GC.security.JwtResponse;
import myfiles.GC.service.UserDetailsServiceImpl;
import myfiles.GC.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Register a new user (checks if username already exists)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        Optional<User> existingUser = userDetailsService.findByUsername(request.getUsername());

        if (existingUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Username already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userDetailsService.registerNewUser(request.getUsername(), request.getPassword(), request.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            User user = userDetailsService.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(new JwtResponse(token, user.getRoles().iterator().next().getName().name(), user.getId())); // ✅ Pass role & userId
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Check if the username is available
    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        Optional<User> user = userDetailsService.findByUsername(username);

        Map<String, Boolean> response = new HashMap<>();
        response.put("available", !user.isPresent()); // true if available, false if taken
        return ResponseEntity.ok(response);
    }
}