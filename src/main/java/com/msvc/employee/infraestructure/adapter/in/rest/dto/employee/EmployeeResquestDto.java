package com.msvc.employee.infraestructure.adapter.in.rest.dto.employee;

import java.util.Set;
import com.msvc.employee.domain.models.AcademicLevel;
import com.msvc.employee.domain.models.DocumentType;
import com.msvc.employee.domain.models.WorkSpace;

public record EmployeeResquestDto(
        String name,
        String lastName,
        DocumentType documentType,
        String documentNumber,
        String email,
        boolean systemAccess,
        AcademicLevel academicLevel,
        Set<WorkSpace> workSpaceIds) {
}
