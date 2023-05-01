package com.shaderock.lunch.backend.feature.food.price.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.supplier.repository.SupplierPreferencesRepository;
import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPrice;
import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPriceDto;
import com.shaderock.lunch.backend.feature.food.price.mapper.CategoriesPriceMapper;
import com.shaderock.lunch.backend.feature.food.price.repository.CategoriesPriceRepository;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriesPriceService {

  private final SupplierPreferencesRepository supplierPreferencesRepository;

  private final CategoriesPriceRepository categoriesPriceRepository;
  private final CategoriesPriceMapper categoriesPriceMapper;

  @Transactional
  public CategoriesPrice create(CategoriesPriceDto categoriesPriceDto, Supplier supplier) {
    CategoriesPrice categoriesPrice = categoriesPriceMapper.toEntity(categoriesPriceDto);
    return create(categoriesPrice, supplier);
  }

  @Transactional
  public CategoriesPrice create(CategoriesPrice categoriesPrice, Supplier supplier) {
    Set<CategoriesPrice> categoriesPrices = supplier.getPreferences().getCategoriesPrices();
    if (categoriesPrices.isEmpty()) {
      categoriesPrices = new HashSet<>();
    }

    categoriesPrice.setId(null);
    categoriesPrice.setSupplierPreferences(supplier.getPreferences());
    categoriesPrice.setAmount(categoriesPrices.size() + 1);

    CategoriesPrice saved = categoriesPriceRepository.save(categoriesPrice);
    categoriesPrices.add(saved);
    supplierPreferencesRepository.save(supplier.getPreferences());
    return saved;
  }

  public CategoriesPrice read(UUID id, Supplier supplier) {
    return categoriesPriceRepository.findByIdAndSupplierPreferences_Supplier(id, supplier)
        .orElseThrow(() -> new CrudValidationException("Categories price not found"));
  }

  public CategoriesPrice read(UUID id) {
    return categoriesPriceRepository.findById(id)
        .orElseThrow(() -> new CrudValidationException("Categories price not found"));
  }

  @Transactional
  public CategoriesPrice update(CategoriesPriceDto categoriesPriceDto, Supplier supplier) {
    CategoriesPrice categoriesPrice = read(categoriesPriceDto.id(), supplier);
    categoriesPrice.setPrice(categoriesPriceDto.price());
    return categoriesPriceRepository.save(categoriesPrice);
  }

  @Transactional
  public void delete(CategoriesPrice categoriesPrice) {
    categoriesPriceRepository.delete(categoriesPrice);
  }
}
