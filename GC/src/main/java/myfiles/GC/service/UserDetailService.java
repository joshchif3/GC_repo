package myfiles.GC.service;

import myfiles.GC.model.User;
import java.util.Optional;

public interface UserDetailService {
    Optional<User> findByUsername(String username);
    User registerNewUser(String username, String password, String role);
}
