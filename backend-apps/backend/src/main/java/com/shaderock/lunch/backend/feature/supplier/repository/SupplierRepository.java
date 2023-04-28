package com.shaderock.lunch.backend.feature.supplier.repository;

import com.shaderock.lunch.backend.data.repository.VisibleEntityRepository;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.Optional;

public interface SupplierRepository extends VisibleEntityRepository<Supplier> {

  Optional<Supplier> findByOrganizationDetails_Name(String name);

  Optional<Supplier> findByOrganizationDetails(OrganizationDetails organizationDetails);

  Optional<Supplier> findByOrganizationDetails_Users_UserDetails_Email(String email);


}
