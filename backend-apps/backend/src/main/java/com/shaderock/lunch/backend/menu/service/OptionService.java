package com.shaderock.lunch.backend.menu.service;

import com.shaderock.lunch.backend.menu.mapper.OptionMapper;
import com.shaderock.lunch.backend.menu.model.dto.OptionDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.menu.repository.OptionRepository;
import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
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

  @Transactional
  public Option create(OptionDto optionDto, Category category) {
    Option option = optionMapper.toEntity(optionDto);
    return create(option, category);
  }

  @Transactional
  public Option create(Option option, Category category) {
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

  public Option readPublic(UUID id) {
    return optionRepository.findByIdAndIsPublicTrue(id).orElseThrow(
        () -> new CrudValidationException(String.format("Public Option(id=[%s]) not found", id)));
  }

  public List<Option> readPublic(Supplier supplier) {
    return optionRepository.findByIsPublicTrueAndCategory_Menu_Supplier_Id(supplier.getId());
  }

  public List<Option> readPublic(Supplier supplier, Category category) {
    return optionRepository.findByIsPublicTrueAndCategoryAndCategory_Menu_Supplier(category,
        supplier);
  }

  public List<Option> readAll(Supplier supplier) {
    return optionRepository.findByCategory_Menu_Supplier_Id(supplier.getId());
  }

  public Option update(OptionDto optionDto, Supplier supplier) {
    Option option = optionMapper.toEntity(optionDto);
    return update(option, supplier);
  }

  @Transactional
  public Option update(Option option, Supplier supplier) {
    Option persistedOption = read(option.getId(), supplier);
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
