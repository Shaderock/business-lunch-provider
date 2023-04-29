package com.shaderock.lunch.backend.feature.order.company.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyOrderRepository extends BaseEntityRepository<CompanyOrder> {

  List<CompanyOrder> findByEmployeesOrders_AppUser_OrganizationDetails(
      OrganizationDetails organizationDetails);

  Optional<CompanyOrder> findByIdAndEmployeesOrders_AppUser_OrganizationDetails(UUID id,
      OrganizationDetails organizationDetails);

}
