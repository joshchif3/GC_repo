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
    public User registerNewUser(String username, String password, String role) {
        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // Find the role by name
        Role userRole = roleRepository.findByName(UserRole.valueOf(role))
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));

        // Assign the role to the user
        user.setRoles(Set.of(userRole));

        // Create a new cart and associate it with the user
        Cart cart = new Cart();
        cart.setUser(user); // Link the cart to the user
        user.setCart(cart); // Link the user to the cart

        // Save the user (this will also save the cart due to CascadeType.ALL)
        return userRepository.save(user);
    }


}