package com.msvc.employee.domain.port.in;

import java.util.List;
import com.msvc.employee.domain.models.User;

public interface UserUseCase {

    List<User> seachAllUser();

    User searchByEmail(String email);

    boolean existsByEmployeeId(Long employeeId);

    void updateActiveByEmployeeId(Long employeeId, boolean active);

    User create(User user);

    void updateActice(Long id, boolean active);

    void updatePassword(Long id, String newPassword, String oldPassword);
}
