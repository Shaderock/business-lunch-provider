package com.shaderock.backend.service.user;

import com.shaderock.backend.model.entity.user.Employee;
import com.shaderock.backend.repository.user.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
  private final EmployeeRepository employeeRepository;

  @Transactional
  public Employee save(Employee employee) {
    return employeeRepository.save(employee);
  }

}
