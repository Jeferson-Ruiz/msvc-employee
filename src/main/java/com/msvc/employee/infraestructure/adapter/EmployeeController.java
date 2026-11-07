package com.msvc.employee.infraestructure.adapter;

import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.msvc.employee.domain.Email;
import com.msvc.employee.domain.NormalizedText;
import com.msvc.employee.domain.models.AcademicLevel;
import com.msvc.employee.domain.models.DocumentType;
import com.msvc.employee.domain.models.Employee;
import com.msvc.employee.domain.models.WorkSpace;
import com.msvc.employee.domain.port.in.EmployeeUseCase;
import com.msvc.employee.infraestructure.adapter.in.rest.dto.employee.EmployeeResquestDto;
import com.msvc.employee.infraestructure.adapter.in.rest.mapper.EmployeeMapper;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;

    public EmployeeController(EmployeeUseCase employeeUseCase) {
        this.employeeUseCase = employeeUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EmployeeResquestDto resquest) {
        Employee employee = employeeUseCase.create(EmployeeMapper.toModel(resquest));
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toDto(employee));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Employee> employees = employeeUseCase.searchAll();
        return ResponseEntity.ok(employees.stream().map(EmployeeMapper::toDto).toList());
    }

    @GetMapping("/document")
    public ResponseEntity<?> getByDocumentNumber(@RequestParam("document") String document) {
        return ResponseEntity.ok(EmployeeMapper.toDto(employeeUseCase.searchByDocumentNumber(document)));
    }

    @GetMapping("/workspace")
    public ResponseEntity<?> getAllByWorkSpace(@RequestParam("workspace") WorkSpace workSpace) {
        return ResponseEntity.ok(employeeUseCase.searchByWorkSpace(workSpace)
                .stream().map(EmployeeMapper::toDto));
    }

    @PatchMapping("/update-name/{id}")
    public ResponseEntity<?> updateName(@PathVariable Long id, @RequestBody String name) {
        employeeUseCase.updateName(id, new NormalizedText(name));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-lastname/{id}")
    public ResponseEntity<?> updateLastName(@PathVariable Long id, @RequestBody String lastName) {
        employeeUseCase.updateLastName(id, new NormalizedText(lastName));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-documentType/{id}")
    public ResponseEntity<?> updateDocumentType(@PathVariable Long id, @RequestBody String documentType) {
        employeeUseCase.updateDocumentType(id, DocumentType.valueOf(documentType));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-email/{id}")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody String email) {
        employeeUseCase.updateEmail(id, new Email(email));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<?> updateActive(@PathVariable Long id, @RequestBody boolean status) {
        employeeUseCase.updateActive(id, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-access/{id}")
    public ResponseEntity<?> updateSystemAccess(@PathVariable Long id, @RequestBody boolean status) {
        employeeUseCase.updateSystemAccess(id, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-academic/{id}")
    public ResponseEntity<?> updateAcademicLevel(@PathVariable Long id, @RequestBody String academicLevel) {
        employeeUseCase.updateAcademicLevel(id, AcademicLevel.valueOf(academicLevel));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-workspace/{id}")
    public ResponseEntity<?> updateWorkSpace(@PathVariable Long id, @RequestBody Set<WorkSpace> workSpaces) {
        employeeUseCase.updateWorkSpace(id, workSpaces);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeUseCase.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
