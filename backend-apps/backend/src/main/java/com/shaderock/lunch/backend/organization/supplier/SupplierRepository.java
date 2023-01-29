package com.shaderock.lunch.backend.organization.supplier;

import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository<S extends Supplier> extends OrganizationRepository<S> {
}
