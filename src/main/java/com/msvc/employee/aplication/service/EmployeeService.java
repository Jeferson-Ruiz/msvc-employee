package com.msvc.employee.aplication.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.msvc.employee.domain.Email;
import com.msvc.employee.domain.NormalizedText;
import com.msvc.employee.domain.exception.EntityNotFoundException;
import com.msvc.employee.domain.models.AcademicLevel;
import com.msvc.employee.domain.models.DocumentType;
import com.msvc.employee.domain.models.Employee;
import com.msvc.employee.domain.models.User;
import com.msvc.employee.domain.models.WorkSpace;
import com.msvc.employee.domain.port.in.EmployeeUseCase;
import com.msvc.employee.domain.port.in.UserUseCase;
import com.msvc.employee.domain.port.out.EmployeeRepositoryPort;

@Service
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepositoryPort employeeRepositoryPort;
    private final UserUseCase userUseCase;

    public EmployeeService(EmployeeRepositoryPort employeeRepositoryPort,
            UserUseCase userUseCase) {
        this.employeeRepositoryPort = employeeRepositoryPort;
        this.userUseCase = userUseCase;
    }

    @Override
    @Transactional
    public Employee create(Employee employee) {
        Set<WorkSpace> workSpaces = resolveWorkSpaces(employee.getWorkSpaces());
        employee.setWorkSpaces(workSpaces);
        validateEmail(employee.getEmail());
        validateIdentificationNumber(employee.getDocumentNumber().value());
        employee.setActive(true);
        employee.setRegistrationDate(LocalDate.now());
        Employee employeeSave = employeeRepositoryPort.save(employee);

        if (employeeSave.isSystemAccess()) {
            createUserForEmployee(employeeSave);
        }

        return employeeSave;
    }

    @Override
    public List<Employee> searchAll() {
        return employeeRepositoryPort.findAll()
                .stream()
                .sorted(Comparator.comparing(Employee::getRegistrationDate))
                .toList();
    }

    @Override
    public Employee searchById(Long id) {
        return employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public Employee searchByDocumentNumber(String number) {
        return employeeRepositoryPort.findByIdentificationNumber(number)
                .orElseThrow(() -> new EntityNotFoundException(number));
    }

    @Override
    public List<Employee> searchByWorkSpace(WorkSpace workSpace) {
        return employeeRepositoryPort.findAllByWorkSpace(workSpace)
                .stream()
                .sorted(Comparator.comparing(Employee::getRegistrationDate))
                .toList();
    }

    @Override
    public void updateName(Long id, NormalizedText name) {
        Employee employee = searchById(id);
        employee.setName(name);
        employeeRepositoryPort.save(employee);
    }

    @Override
    public void updateLastName(Long id, NormalizedText lastName) {
        Employee employee = searchById(id);
        employee.setLastName(lastName);
        employeeRepositoryPort.save(employee);
    }

    @Override
    public void updateDocumentType(Long id, DocumentType documentType) {
        Employee employee = searchById(id);
        employee.setDocumentType(documentType);
        employeeRepositoryPort.save(employee);
    }

    @Override
    public void updateEmail(Long id, Email email) {
        Employee employee = searchById(id);
        validateEmail(email);
        employee.setEmail(email);
        employeeRepositoryPort.save(employee);
    }

    @Override
    public void updateActive(Long id, boolean status) {
        Employee employee = searchById(id);
        employee.setActive(status);
        employeeRepositoryPort.save(employee);
    }

    @Override
    @Transactional
    public void updateSystemAccess(Long id, boolean access) {
        Employee employee = searchById(id);
        boolean wasAccess = employee.isSystemAccess();
        employee.setSystemAccess(access);
        employeeRepositoryPort.save(employee);

        boolean userExists = userUseCase.existsByEmployeeId(employee.getId());

        if (access != wasAccess) {
            if (access && !userExists) {
                createUserForEmployee(employee);
            } else if (userExists) {
                userUseCase.updateActiveByEmployeeId(employee.getId(), access);
            }
        }
    }

    @Override
    public void updateAcademicLevel(Long id, AcademicLevel academicLevel) {
        Employee employee = searchById(id);
        employee.setAcademicLevel(academicLevel);
        employeeRepositoryPort.save(employee);
    }

    @Override
    public void updateWorkSpace(Long id, Set<WorkSpace> workSpaces) {
        Set<WorkSpace> resolved = resolveWorkSpaces(workSpaces);
        Employee employee = searchById(id);
        employee.setWorkSpaces(resolved);
        employeeRepositoryPort.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = searchById(id);
        employee.setActive(false);
        employee.setSystemAccess(false);
        employeeRepositoryPort.save(employee);
    }

    // helpers
    private Set<WorkSpace> resolveWorkSpaces(Set<WorkSpace> workSpaces) {
        if (workSpaces == null || workSpaces.isEmpty()) {
            return Set.of();
        }
        return workSpaces;
    }

    private void validateIdentificationNumber(String number) {
        if (employeeRepositoryPort.existsByDocumentNumber(number)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmail(Email email) {
        if (employeeRepositoryPort.existsByEmail(email.value())) {
            throw new IllegalArgumentException();
        }
    }

    private void createUserForEmployee(Employee employee) {
        User user = new User();
        user.setEmail(employee.getEmail().value());
        user.setEmployeeId(employee);
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        userUseCase.create(user);
    }

}
