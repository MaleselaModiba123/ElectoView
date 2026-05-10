package za.ac.cput.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean delete(ID id);
    long count();
    boolean existsById(ID id);
    
}
