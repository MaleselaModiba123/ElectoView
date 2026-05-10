package za.ac.cput.repositories.filesystem;

import za.ac.cput.Enums.AccountStatus;
import za.ac.cput.Enums.Role;
import za.ac.cput.domain.User;
import za.ac.cput.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;


public class FileSystemUserRepository implements IUserRepository {

    private final String filePath;

    public FileSystemUserRepository(String filePath) {
        if (filePath == null || filePath.isBlank())
            throw new IllegalArgumentException("File path cannot be empty.");
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public User save(User entity) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.save not yet implemented. "
              + "Add Jackson dependency and implement JSON serialisation.");
    }

    @Override
    public Optional<User> findById(String id) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.findById not yet implemented.");
    }

    @Override
    public List<User> findAll() {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.findAll not yet implemented.");
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.delete not yet implemented.");
    }

    @Override
    public boolean existsById(String id) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.existsById not yet implemented.");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.count not yet implemented.");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.findByEmail not yet implemented.");
    }

    @Override
    public List<User> findByRole(Role role) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.findByRole not yet implemented.");
    }

    @Override
    public List<User> findByStatus(AccountStatus status) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.findByStatus not yet implemented.");
    }

    @Override
    public boolean existsByEmail(String email) {
        throw new UnsupportedOperationException(
                "FileSystemUserRepository.existsByEmail not yet implemented.");
    }
}