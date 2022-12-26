package com.shaderock.backend.repository.user;

import com.shaderock.backend.model.entity.user.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends UserRepository<AppUser> {
}
