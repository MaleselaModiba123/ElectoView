package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.Enums.*;
import za.ac.cput.domain.*;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String name, String email,
                           String passwordHash, Role role) {
        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("Email already registered: " + email);

        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordHash)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User activate(String id) {
        User user = findById(id);
        user.activate();
        return userRepository.save(user);
    }

    public User deactivate(String id) {
        User user = findById(id);
        user.deactivate();
        return userRepository.save(user);
    }

    public User unlock(String id) {
        User user = findById(id);
        user.unlockAccount();
        return userRepository.save(user);
    }

    public User recordFailedLogin(String email) {
        User user = findByEmail(email);
        user.incrementFailedAttempts();
        return userRepository.save(user);
    }

    public User recordSuccessfulLogin(String email) {
        User user = findByEmail(email);
        user.recordLogin();
        return userRepository.save(user);
    }

    public User resetPassword(String id, String newPasswordHash) {
        User user = findById(id);
        user.resetPassword(newPasswordHash);
        return userRepository.save(user);
    }

    public User updateRole(String id, Role newRole) {
        User user = findById(id);
        user.setRole(newRole);
        return userRepository.save(user);
    }
}