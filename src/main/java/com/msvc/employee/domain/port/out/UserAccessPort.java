package com.msvc.employee.domain.port.out;

public interface UserAccessPort {
    boolean existsByEmployeeId(Long employeeId);

    void updateActiveByEmployeeId(Long employeeId, boolean active);

    void createForEmployee(Long employeeId);
}
