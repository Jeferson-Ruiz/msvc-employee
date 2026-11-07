package com.msvc.employee.infraestructure.adapter.out.persistence.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmployee_Id(Long employeeId);

    Optional<UserJpaEntity> findByEmployee_Id(Long employeeId);

}
