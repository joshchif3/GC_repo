package myfiles.GC.config;

import myfiles.GC.model.Role;
import myfiles.GC.model.UserRole;
import myfiles.GC.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadRoles(RoleRepository roleRepository) {
        return args -> {
            // Check if roles already exist
            if (roleRepository.findByName(UserRole.USER).isEmpty()) {
                Role userRole = new Role();
                userRole.setName(UserRole.USER);
                roleRepository.save(userRole);
            }

            if (roleRepository.findByName(UserRole.ADMIN).isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName(UserRole.ADMIN);
                roleRepository.save(adminRole);
            }
        };
    }
}