package com.shaderock.lunch.backend.doubles;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.repository.CompanyPreferenceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeCompanyPreferenceRepository implements CompanyPreferenceRepository {

  private final List<CompanyPreferences> preferencesList = new ArrayList<>();

  @Override
  public <S extends CompanyPreferences> S save(S entity) {
    preferencesList.add(entity);
    return entity;
  }

  @Override
  public Optional<CompanyPreferences> findById(UUID uuid) {
    return preferencesList.stream().filter(p -> p.getId().equals(uuid)).findFirst();
  }

  @Override
  public boolean existsById(UUID uuid) {
    return false;
  }


  @Override
  public <S extends CompanyPreferences> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public List<CompanyPreferences> findAll() {
    return null;
  }

  @Override
  public List<CompanyPreferences> findAllById(Iterable<UUID> uuids) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(UUID uuid) {

  }

  @Override
  public void delete(CompanyPreferences entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends UUID> uuids) {

  }

  @Override
  public void deleteAll(Iterable<? extends CompanyPreferences> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public Optional<CompanyPreferences> findByCompany_OrganizationDetails_Users_UserDetails_Id(
      UUID id) {
    return Optional.empty();
  }
}
