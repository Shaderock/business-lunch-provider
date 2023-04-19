package com.shaderock.lunch.backend.feature.order.employee.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeOrderRepository extends DeletableEntityRepository<EmployeeOrder> {

  List<EmployeeOrder> findByAppUser_UserDetailsAndStatusAndOrderDate(AppUserDetails userDetails,
      EmployeeOrderStatus status, LocalDate orderDate);

  List<EmployeeOrder> findByAppUser_UserDetails(AppUserDetails userDetails);

  List<EmployeeOrder> findByAppUser_UserDetailsAndOrderDate(AppUserDetails userDetails,
      LocalDate orderDate);

  List<EmployeeOrder> findByAppUser_UserDetailsAndStatus(AppUserDetails userDetails,
      EmployeeOrderStatus status);

  Optional<EmployeeOrder> findByIdAndAppUser_UserDetails(UUID id, AppUserDetails userDetails);

}
