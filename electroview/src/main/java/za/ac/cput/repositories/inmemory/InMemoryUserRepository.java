package za.ac.cput.repositories.inmemory;

import za.ac.cput.Enums.*;
import za.ac.cput.domain.*;
import za.ac.cput.repositories.IUserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryUserRepository implements IUserRepository {
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

     @Override
    public User save(User entity) {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("User and ID cannot be null.");
        userMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public void deleteById(String id) {
        userMap.remove(id);
    }

    public void delete(User entity) {
        if (entity != null && entity.getId() != null)
            userMap.remove(entity.getId());
    }

    @Override
    public boolean existsById(String id) {
        return userMap.containsKey(id);
    }

    @Override
    public long count() {
        return userMap.size();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMap.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<User> findByRole(Role role) {
        return userMap.values().stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByStatus(AccountStatus status) {
        return userMap.values().stream()
                .filter(u -> u.getStatus() == status)
                .collect(Collectors.toList());
    }
    @Override
    public boolean existsByEmail(String email) {
        return userMap.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean delete(String id) {
        return userMap.remove(id) != null;
    }
}
