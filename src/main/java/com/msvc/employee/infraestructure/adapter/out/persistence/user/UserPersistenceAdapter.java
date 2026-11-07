package com.msvc.employee.infraestructure.adapter.out.persistence.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import com.msvc.employee.domain.models.User;
import com.msvc.employee.domain.port.out.UserRepositoryPort;
import com.msvc.employee.infraestructure.adapter.out.persistence.employee.EmployeeJpaEntity;
import com.msvc.employee.domain.models.Employee;

@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setActive(user.isActive());
        entity.setRegistrationDate(user.getRegistrationDate());

        if (user.getEmployee() != null) {
            EmployeeJpaEntity employeeEntity = new EmployeeJpaEntity();
            employeeEntity.setId(user.getEmployee().getId());
            entity.setEmployee(employeeEntity);
        }

        UserJpaEntity saved = userJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return userJpaRepository.existsByEmployee_Id(employeeId);
    }

    @Override
    public Optional<User> findByEmployeeId(Long employeeId) {
        return userJpaRepository.findByEmployee_Id(employeeId).map(this::toDomain);
    }

    private User toDomain(UserJpaEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setActive(entity.isActive());
        user.setRegistrationDate(entity.getRegistrationDate());

        if (entity.getEmployee() != null) {
            Employee employee = new Employee();
            employee.setId(entity.getEmployee().getId());
            user.setEmployeeId(employee);
        }

        return user;
    }

}
