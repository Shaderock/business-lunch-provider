package com.shaderock.backend.repository.user;

import com.shaderock.backend.model.entity.user.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository<U extends Supplier> extends UserRepository<U> {
}
