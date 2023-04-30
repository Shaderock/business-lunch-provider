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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionService {

  private final OptionRepository optionRepository;
  private final OptionMapper optionMapper;
  private final SupplierValidationService supplierValidationService;
  private final OptionValidationService optionValidationService;

  @Transactional
  public Option create(OptionDto optionDto, Category category) {
    Option option = optionMapper.toEntity(optionDto);
    return create(option, category);
  }

  @Transactional
  public Option create(Option option, Category category) {

    option.setId(null);
    option.setPublic(false);
    option.setCategory(category);
    option.setCreatedAt(LocalDate.now());
    option.setPublishedAt(null);
    option.setPhoto(null);

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

  public Option update(OptionDto optionDto, Supplier supplier, Category category) {
    Option option = optionMapper.toEntity(optionDto);
    return update(option, supplier, category);
  }

  // todo don't allow setting back to non public from public
  @Transactional
  public Option update(Option option, Supplier supplier, Category category) {
    Option persistedOption = read(option.getId(), supplier);
    if (option.isPublic()) {
      supplierValidationService.validateCanCreatePublicOptions(supplier);
      optionValidationService.validateOptionCanBeMadePublic(persistedOption,
          persistedOption.getCategory());
      persistedOption.setPublic(true);
      persistedOption.setPublishedAt(LocalDate.now());
    } else {
      // todo validate option can be made private
      persistedOption.setPublic(false);
    }

    if (persistedOption.getPublishedAt() == null) {
      persistedOption.getCategory().getOptions().remove(persistedOption);
      persistedOption.setCategory(category);
      category.getOptions().add(persistedOption);

      persistedOption.setName(option.getName());
      persistedOption.setPublic(option.isPublic());
      persistedOption.setDescription(option.getDescription());
      persistedOption.setPrice(option.getPrice());
      persistedOption.setGram(option.getGram());
    }

    return optionRepository.save(persistedOption);
  }

  @Transactional
  @SneakyThrows
  @CacheEvict(cacheNames = {"supplierOptionThumbnail", "supplierOptionPhoto"}, key = "#option.id")
  public void updatePhoto(MultipartFile photo, Option option) {
    option.setPhoto(photo.getBytes());
    optionRepository.save(option);
  }

  @Transactional
  public void delete(UUID id, Supplier supplier) {
    Option persistedOption = read(id, supplier);
    optionRepository.delete(persistedOption);
  }

  @Transactional
  @CacheEvict(cacheNames = {"supplierOptionThumbnail", "supplierOptionPhoto"}, key = "#option.id")
  public void deletePhoto(Option option) {
    option.setPhoto(null);
    optionRepository.save(option);
  }
}
