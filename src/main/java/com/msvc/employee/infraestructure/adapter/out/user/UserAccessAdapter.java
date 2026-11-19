package com.msvc.employee.infraestructure.adapter.out.user;

import org.springframework.stereotype.Component;
import com.msvc.employee.domain.models.Employee;
import com.msvc.employee.domain.models.User;
import com.msvc.employee.domain.port.in.UserUseCase;
import com.msvc.employee.domain.port.out.UserAccessPort;

@Component
public class UserAccessAdapter implements UserAccessPort {

    private final UserUseCase userUseCase;

    public UserAccessAdapter(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return userUseCase.existsByEmployeeId(employeeId);
    }

    @Override
    public void updateActiveByEmployeeId(Long employeeId, boolean active) {
        userUseCase.updateActiveByEmployeeId(employeeId, active);
    }

    @Override
    public void createForEmployee(Long employeeId) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        User user = new User();
        user.setEmployeeId(employee);
        userUseCase.create(user);
    }

}
