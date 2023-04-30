package com.shaderock.lunch.backend.feature.order.employee.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeOrderRepository extends BaseEntityRepository<EmployeeOrder> {

  List<EmployeeOrder> findByOrderDateAndOptions_Category_Menu_Supplier(LocalDate orderDate,
      Supplier supplier);

  List<EmployeeOrder> findByAppUser_OrganizationDetailsAndOrderDate(
      OrganizationDetails organizationDetails, LocalDate orderDate);

  Optional<EmployeeOrder> findByIdAndAppUser_OrganizationDetails(UUID id,
      OrganizationDetails organizationDetails);

  List<EmployeeOrder> findByIdInAndAppUser_OrganizationDetails(Collection<UUID> ids,
      OrganizationDetails organizationDetails);

  List<EmployeeOrder> findByAppUserAndOrderDate(AppUser appUser, LocalDate orderDate);

  List<EmployeeOrder> findByAppUser_UserDetailsAndStatusAndOrderDate(AppUserDetails userDetails,
      EmployeeOrderStatus status, LocalDate orderDate);

  List<EmployeeOrder> findByAppUser_UserDetails(AppUserDetails userDetails);

  List<EmployeeOrder> findByAppUser_UserDetailsAndOrderDate(AppUserDetails userDetails,
      LocalDate orderDate);

  List<EmployeeOrder> findByAppUser_UserDetailsAndStatus(AppUserDetails userDetails,
      EmployeeOrderStatus status);

  Optional<EmployeeOrder> findByIdAndAppUser_UserDetails(UUID id, AppUserDetails userDetails);

}
