package com.shaderock.lunch.backend.feature.subscription.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends BaseEntityRepository<Subscription> {

  Optional<Subscription> findByCompanyAndSupplier(Company company, Supplier supplier);

  List<Subscription> findByCompany(Company company);

  List<Subscription> findBySupplier(Supplier supplier);

  boolean existsByCompany_IdAndSupplier_Id(UUID companyId, UUID supplierId);

}
