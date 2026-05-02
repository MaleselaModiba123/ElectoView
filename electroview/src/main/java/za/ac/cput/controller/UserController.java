package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.Enums.*;
import za.ac.cput.domain.User;
import za.ac.cput.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String passwordHash,
            @RequestParam Role role) {
        User user = userService.createUser(name, email, passwordHash, role);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<User> activate(@PathVariable String id) {
        return ResponseEntity.ok(userService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(userService.deactivate(id));
    }

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<User> unlock(@PathVariable String id) {
        return ResponseEntity.ok(userService.unlock(id));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<User> updateRole(
            @PathVariable String id,
            @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<User> resetPassword(
            @PathVariable String id,
            @RequestParam String newPasswordHash) {
        return ResponseEntity.ok(userService.resetPassword(id, newPasswordHash));
    }
}