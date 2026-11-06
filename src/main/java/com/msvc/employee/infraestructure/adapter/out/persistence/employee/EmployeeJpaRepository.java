package com.msvc.employee.infraestructure.adapter.out.persistence.employee;

import java.util.List;
import java.util.Optional;
import com.msvc.employee.domain.models.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeJpaEntity, Long> {

    Optional<EmployeeJpaEntity> findByDocumentNumber(String number);

    List<EmployeeJpaEntity> findByWorkSpaces(WorkSpace workSpace);

    boolean existsByEmail(String email);

    boolean existsByDocumentNumber(String number);

}
