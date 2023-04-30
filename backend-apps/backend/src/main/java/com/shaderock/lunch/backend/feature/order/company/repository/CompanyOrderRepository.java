package com.shaderock.lunch.backend.feature.order.company.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyOrderRepository extends BaseEntityRepository<CompanyOrder> {

  Optional<CompanyOrder> findByIdAndEmployeesOrders_Options_Category_Menu_Supplier(UUID id,
      Supplier supplier);

  List<CompanyOrder> findByEmployeesOrders_Options_Category_Menu_Supplier(Supplier supplier);

  List<CompanyOrder> findByEmployeesOrders_AppUser_OrganizationDetails(
      OrganizationDetails organizationDetails);

  Optional<CompanyOrder> findByIdAndEmployeesOrders_AppUser_OrganizationDetails(UUID id,
      OrganizationDetails organizationDetails);

}
