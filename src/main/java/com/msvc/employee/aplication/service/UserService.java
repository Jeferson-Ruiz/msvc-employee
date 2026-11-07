package com.msvc.employee.aplication.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.msvc.employee.domain.exception.EntityNotFoundException;
import com.msvc.employee.domain.models.Employee;
import com.msvc.employee.domain.models.User;
import com.msvc.employee.domain.port.in.UserUseCase;
import com.msvc.employee.domain.port.out.EmployeeRepositoryPort;
import com.msvc.employee.domain.port.out.PasswordEncoderPort;
import com.msvc.employee.domain.port.out.UserRepositoryPort;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final EmployeeRepositoryPort employeeRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UserService(UserRepositoryPort userRepositoryPort,
            EmployeeRepositoryPort employeeRepositoryPort,
            PasswordEncoderPort passwordEncoderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.employeeRepositoryPort = employeeRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public List<User> seachAllUser() {
        return userRepositoryPort.findAll().stream()
                .sorted(Comparator.comparing(User::getRegistrationDate))
                .toList();
    }

    @Override
    public User searchByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(email));
    }

    @Override
    public User create(User user) {
        Employee employee = findEmployeeById(user.getEmployee().getId());
        if (!employee.isSystemAccess()) {
            employee.setSystemAccess(true);
            employeeRepositoryPort.save(employee);
        }

        user.setEmail(employee.getEmail().value());
        String password = buildPassword(employee.getName().value(), employee.getDocumentNumber().value());
        user.setPassword(passwordEncoderPort.encode(password));
        user.setRegistrationDate(LocalDateTime.now());
        user.setActive(true);

        return userRepositoryPort.save(user);
    }

    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return userRepositoryPort.existsByEmployeeId(employeeId);
    }

    @Override
    public void updateActiveByEmployeeId(Long employeeId, boolean active) {
        Optional<User> user = userRepositoryPort.findByEmployeeId(employeeId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(employeeId);
        }
        user.get().setActive(active);
        userRepositoryPort.save(user.get());
    }

    @Override
    public void updatePassword(Long id, String newPassword, String oldPassword) {
        User user = findUserById(id);
        if (!passwordEncoderPort.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException();
        }
        user.setPassword(passwordEncoderPort.encode(newPassword));
        userRepositoryPort.save(user);
    }

    @Override
    public void updateActice(Long id, boolean active) {
        User user = findUserById(id);
        user.setActive(active);
        userRepositoryPort.save(user);
    }

    // helper
    private Employee findEmployeeById(Long id) {
        return employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    private User findUserById(Long id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    private String buildPassword(String name, String documentNumber) {
        StringBuilder builder = new StringBuilder();
        builder.append(name.substring(0, 2).toLowerCase());
        builder.append(documentNumber);
        return builder.toString();
    }

}
