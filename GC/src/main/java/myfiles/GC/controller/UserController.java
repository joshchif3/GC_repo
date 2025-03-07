package myfiles.GC.controller;

import myfiles.GC.dto.UserLoginRequest;
import myfiles.GC.dto.UserRegistrationRequest;
import myfiles.GC.model.User;
import myfiles.GC.security.JwtResponse;
import myfiles.GC.service.UserDetailsImpl;
import myfiles.GC.service.UserDetailsServiceImpl;
import myfiles.GC.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        // Validate username format
        if (!request.getUsername().matches("^[a-zA-Z0-9_]{3,20}$")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Username must be 3-20 characters (letters, numbers, underscores only)")
            );
        }

        // Validate email format
        if (!request.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Invalid email format")
            );
        }

        // Validate password strength
        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Password must be 8+ chars with letter, number, and special character")
            );
        }

        // Check username availability
        if (userDetailsService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", "Username already exists")
            );
        }

        // Force USER role for all registrations
        userDetailsService.registerNewUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                "USER"
        );

        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get the authenticated user
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Generate the JWT token
            String token = jwtTokenProvider.generateToken(authentication);

            // Return all required user information
            return ResponseEntity.ok(new JwtResponse(
                    token,
                    userDetails.getRole(),
                    userDetails.getUsername(),  // Add username
                    userDetails.getUserId()     // Correct method name
            ));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(Map.of(
                "userId", userDetails.getUserId(),
                "username", userDetails.getUsername(),
                "role", userDetails.getRole()
        ));
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsernameAvailability(@PathVariable String username) {
        boolean available = !userDetailsService.findByUsername(username).isPresent();
        return ResponseEntity.ok(Map.of("available", available));
    }
}