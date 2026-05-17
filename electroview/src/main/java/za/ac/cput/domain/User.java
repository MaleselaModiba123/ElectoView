package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import za.ac.cput.Enums.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "passwordHash")
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private int failedLoginAttempts = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Builder
    public User(String id, String name, String email, String passwordHash, Role role) {
        this.id = id != null ? id : java.util.UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = AccountStatus.PENDING;
        this.failedLoginAttempts = 0;
    }
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = AccountStatus.PENDING;
        }
    }

     public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    public boolean isLocked() {
        return this.status == AccountStatus.LOCKED;
    }

    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
            lockAccount();
        }
    }
    public void lockAccount() {
        this.status = AccountStatus.LOCKED;
    }

    public void unlockAccount() {
        this.status = AccountStatus.ACTIVE;
        this.failedLoginAttempts = 0;
    }

    public void activate(){
        this.status = AccountStatus.ACTIVE;
    }

    public void deactivate(){
        this.status = AccountStatus.INACTIVE;
    }

    public void resetPassword(String newPasswordHash) {
    if (newPasswordHash == null || newPasswordHash.isBlank())
        throw new IllegalArgumentException("Password hash cannot be empty.");
    this.passwordHash = newPasswordHash;
    this.failedLoginAttempts = 0;
}

    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
    }

}
