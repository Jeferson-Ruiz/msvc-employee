package com.msvc.employee.domain.port.out;

import java.util.List;
import java.util.Optional;
import com.msvc.employee.domain.models.User;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(Long employeeId);

    Optional<User> findByEmployeeId(Long employeeId);

}
