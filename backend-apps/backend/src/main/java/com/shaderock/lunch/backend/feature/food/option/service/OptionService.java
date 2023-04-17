package com.shaderock.lunch.backend.feature.food.option.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.option.dto.OptionDto;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.mapper.OptionMapper;
import com.shaderock.lunch.backend.feature.food.option.repository.OptionRepository;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierValidationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionService {

  private final OptionRepository optionRepository;
  private final OptionMapper optionMapper;
  private final SupplierValidationService supplierValidationService;

  @Transactional
  public Option create(OptionDto optionDto, Category category) {
    Option option = optionMapper.toEntity(optionDto);
    return create(option, category);
  }

  @Transactional
  public Option create(Option option, Category category) {
    if (option.isPublic()) {
      supplierValidationService.validateCanCreatePublicOptions(category.getMenu().getSupplier());
    }
    validateVisibility(option, category);

    option.setCategory(category);
    Option persistedOption = optionRepository.save(option);
    category.getOptions().add(persistedOption);
    return persistedOption;
  }

  public Option read(UUID id) {
    return optionRepository.findById(id).orElseThrow(
        () -> new CrudValidationException(String.format("Option(id=[%s]) not found", id)));
  }

  public List<Option> read(Category category) {
    return optionRepository.findByCategory_Id(category.getId());
  }

  public Option read(UUID id, Supplier supplier) {
    return optionRepository.findByIdAndCategory_Menu_Supplier_Id(id, supplier.getId()).orElseThrow(
        () -> new CrudValidationException(
            String.format("Option(id=[%s]) for Supplier(id=[%s]) not found", id,
                supplier.getId())));
  }

  public List<Option> read(Supplier supplier) {
    return optionRepository.findByCategory_Menu_Supplier_Id(supplier.getId());
  }

  public List<Option> read(Supplier supplier, Category category) {
    return optionRepository.findByCategoryAndCategory_Menu_Supplier(category,
        supplier);
  }

  public Option update(OptionDto optionDto, Supplier supplier) {
    Option option = optionMapper.toEntity(optionDto);
    return update(option, supplier);
  }

  @Transactional
  public Option update(Option option, Supplier supplier) {
    Option persistedOption = read(option.getId(), supplier);
    if (option.isPublic()) {
      supplierValidationService.validateCanCreatePublicOptions(supplier);
    }
    validateVisibility(persistedOption, persistedOption.getCategory());
    persistedOption.setName(option.getName());
    persistedOption.setPublic(option.isPublic());
    persistedOption.setPrice(option.getPrice());
    return persistedOption;
  }

  @Transactional
  public void delete(UUID id, Supplier supplier) {
    Option persistedOption = read(id, supplier);
    optionRepository.delete(persistedOption);
  }

  private void validateVisibility(Option option, Category category) {
    if (!category.isPublic() && option.isPublic()) {
      throw new CrudValidationException(
          String.format(
              "Can not set public Option(id=[%s]) because Category(id=[%s]) is not public",
              category.getId(), category.getId()));
    }
  }
}
