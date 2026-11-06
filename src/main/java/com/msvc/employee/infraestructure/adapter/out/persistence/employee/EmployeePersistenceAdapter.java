package com.msvc.employee.infraestructure.adapter.out.persistence.employee;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;
import com.msvc.employee.domain.DocumentNumber;
import com.msvc.employee.domain.Email;
import com.msvc.employee.domain.NormalizedText;
import com.msvc.employee.domain.models.AcademicLevel;
import com.msvc.employee.domain.models.DocumentType;
import com.msvc.employee.domain.models.Employee;
import com.msvc.employee.domain.models.WorkSpace;
import com.msvc.employee.domain.port.out.EmployeeRepositoryPort;

@Repository
public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

    private final EmployeeJpaRepository employeeJpaRepository;

    public EmployeePersistenceAdapter(EmployeeJpaRepository employeeJpaRepository) {
        this.employeeJpaRepository = employeeJpaRepository;
    }

    @Override
    public Employee save(Employee employee) {
        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        entity.setId(employee.getId());
        entity.setName(employee.getName().value());
        entity.setLastName(employee.getLastName().value());
        entity.setDocumentType(employee.getDocumentType().name());
        entity.setDocumentNumber(employee.getDocumentNumber().value());
        entity.setEmail(employee.getEmail().value());
        entity.setActive(employee.isActive());
        entity.setSystemAccess(employee.isSystemAccess());
        entity.setRegistrationDate(employee.getRegistrationDate());
        entity.setAcademicLevel(employee.getAcademicLevel().name());
        entity.setWorkSpaces(employee.getWorkSpaces());
        return toDomain(employeeJpaRepository.save(entity));
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Employee> findByIdentificationNumber(String number) {
        return employeeJpaRepository.findByDocumentNumber(number).map(this::toDomain);
    }

    @Override
    public List<Employee> findAll() {
        return employeeJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Employee> findAllByWorkSpace(WorkSpace workSpace) {
        return employeeJpaRepository.findByWorkSpaces(workSpace)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return employeeJpaRepository.existsByDocumentNumber(documentNumber);
    }

    private Employee toDomain(EmployeeJpaEntity employeeJpaEntity) {
        Set<WorkSpace> workSpaces = employeeJpaEntity.getWorkSpaces() == null
                ? Set.of()
                : employeeJpaEntity.getWorkSpaces();

        return new Employee(
                employeeJpaEntity.getId(),
                new NormalizedText(employeeJpaEntity.getName()),
                new NormalizedText(employeeJpaEntity.getLastName()),
                DocumentType.valueOf(employeeJpaEntity.getDocumentType()),
                new DocumentNumber(employeeJpaEntity.getDocumentNumber()),
                new Email(employeeJpaEntity.getEmail()),
                employeeJpaEntity.isActive(),
                employeeJpaEntity.isSystemAccess(),
                employeeJpaEntity.getRegistrationDate(),
                AcademicLevel.valueOf(employeeJpaEntity.getAcademicLevel()),
                workSpaces);
    }

}
