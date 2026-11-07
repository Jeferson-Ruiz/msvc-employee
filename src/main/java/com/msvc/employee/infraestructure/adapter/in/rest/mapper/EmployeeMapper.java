package com.msvc.employee.infraestructure.adapter.in.rest.mapper;

import java.util.Set;
import com.msvc.employee.domain.DocumentNumber;
import com.msvc.employee.domain.Email;
import com.msvc.employee.domain.NormalizedText;
import com.msvc.employee.domain.models.Employee;
import com.msvc.employee.domain.models.WorkSpace;
import com.msvc.employee.infraestructure.adapter.in.rest.dto.employee.EmployeeResponseDto;
import com.msvc.employee.infraestructure.adapter.in.rest.dto.employee.EmployeeResquestDto;

public class EmployeeMapper {

    public static Employee toModel(EmployeeResquestDto toDto) {
        Employee employee = new Employee();
        employee.setId(null);
        employee.setName(new NormalizedText(toDto.name()));
        employee.setLastName(new NormalizedText(toDto.lastName()));
        employee.setDocumentType(toDto.documentType());
        employee.setDocumentNumber(new DocumentNumber(toDto.documentNumber()));
        employee.setEmail(new Email(toDto.email()));
        employee.setActive(true);
        employee.setSystemAccess(toDto.systemAccess());
        employee.setRegistrationDate(null);
        employee.setAcademicLevel(toDto.academicLevel());

        Set<WorkSpace> workSpaces = toDto.workSpaceIds() == null
                ? Set.of()
                : toDto.workSpaceIds();
        employee.setWorkSpaces(workSpaces);

        return employee;
    }

    public static EmployeeResponseDto toDto(Employee entity) {
        Set<WorkSpace> workSpaces = entity.getWorkSpaces() == null
                ? Set.of()
                : entity.getWorkSpaces();
        return new EmployeeResponseDto(entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getDocumentType(),
                entity.getDocumentNumber(),
                entity.getEmail(),
                entity.isActive(),
                entity.isSystemAccess(),
                entity.getRegistrationDate(),
                entity.getAcademicLevel(),
                workSpaces);
    }
}
