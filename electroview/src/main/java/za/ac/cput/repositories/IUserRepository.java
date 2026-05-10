package za.ac.cput.repositories;

import za.ac.cput.domain.User;
import za.ac.cput.Enums.AccountStatus;
import za.ac.cput.Enums.Role;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends Repository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User>findByStatus(AccountStatus status);
}
