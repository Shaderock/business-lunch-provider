package com.shaderock.backend.repository.user;

import com.shaderock.backend.model.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
  Optional<Company> findByName(String name);

  Optional<Company> findByEmail(String email);
}
