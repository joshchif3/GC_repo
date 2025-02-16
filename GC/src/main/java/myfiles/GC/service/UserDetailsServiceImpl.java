package myfiles.GC.service;

import myfiles.GC.model.Cart;
import myfiles.GC.model.Role;
import myfiles.GC.model.User;
import myfiles.GC.model.UserRole;
import myfiles.GC.repository.UserRepository;
import myfiles.GC.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService, UserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserDetailsImpl(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User registerNewUser(String username, String email, String password, String role) {
        // Always force the role to "USER"
        role = "USER";

        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email); // Set email field
        user.setPassword(passwordEncoder.encode(password));

        // Find the "USER" role in the database
        Role userRole = roleRepository.findByName(UserRole.USER)
                .orElseThrow(() -> new RuntimeException("Role 'USER' not found in database"));

        // Assign only the "USER" role
        user.setRoles(Set.of(userRole));

        // Create and assign a new cart for the user
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        // Save and return the user
        return userRepository.save(user);
    }
}