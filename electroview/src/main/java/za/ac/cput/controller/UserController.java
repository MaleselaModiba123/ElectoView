package za.ac.cput.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "User account management operations")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user",
               description = "Registers a new user account with the specified role. "
                           + "Fails if the email is already registered.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Email already registered")
    })

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String passwordHash,
            @RequestParam Role role) {
        User user = userService.createUser(name, email, passwordHash, role);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

     @Operation(summary = "Get user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users returned")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }
    @Operation(summary = "Get users by role")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }

    @Operation(summary = "Activate user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account activated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<User> activate(@PathVariable String id) {
        return ResponseEntity.ok(userService.activate(id));
    }

    @Operation(summary = "Deactivate user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account deactivated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(userService.deactivate(id));
    }

    @Operation(summary = "Lock user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account locked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/unlock")
    public ResponseEntity<User> unlock(@PathVariable String id) {
        return ResponseEntity.ok(userService.unlock(id));
    }

    @Operation(summary = "Update user role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> updateRole(
            @PathVariable String id,
            @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    @Operation(summary = "Reset user password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<User> resetPassword(
            @PathVariable String id,
            @RequestParam String newPasswordHash) {
        return ResponseEntity.ok(userService.resetPassword(id, newPasswordHash));
    }
}