package com.shaderock.lunch.backend.feature.food.category.service;


import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.mapper.CategoryMapper;
import com.shaderock.lunch.backend.feature.food.category.repository.CategoryRepository;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierValidationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;
  private final SupplierValidationService supplierValidationService;
  private final CategoryValidationService categoryValidationService;

  public Category create(@NonNull CategoryDto categoryDto, @NonNull Supplier supplier) {
    LOGGER.info("Converting [{}] to Category", categoryDto);

    Category newCategory = categoryMapper.toEntity(categoryDto);

    LOGGER.info("Converted [{}]", newCategory);

    return create(newCategory, supplier);
  }

  @Transactional
  public Category create(@NonNull Category newCategory, @NonNull Supplier supplier) {
    LOGGER.info("Attempting to create [{}]", newCategory);

    categoryValidationService.validateCreate(newCategory, supplier);

    newCategory.setMenu(supplier.getMenu());

    Category persistedCategory = categoryRepository.save(newCategory);
    supplier.getMenu().getCategories().add(persistedCategory);

    LOGGER.info("Created [{}]", persistedCategory);
    return persistedCategory;
  }


  public Category read(@NonNull UUID id) {
    LOGGER.info("Attempting to read Category by id=[{}]", id);

    return categoryRepository.findById(id)
        .orElseThrow(() -> new CrudValidationException(
            String.format("Category(id=[%s]) not found", id)));
  }

  public Category read(@NonNull UUID id, @NonNull Supplier supplier) {
    return categoryRepository.findByIdAndMenu_Supplier_Id(id, supplier.getId()).orElseThrow(
        () -> new CrudValidationException(
            String.format("Category(id=[%s]) not found for Supplier(id=[%s])", id,
                supplier.getOrganizationDetails().getId())));
  }

  public List<Category> read() {
    return categoryRepository.findAll();
  }

  public List<Category> read(@NonNull Supplier supplier) {
    return categoryRepository.findByMenu_Supplier_Id(supplier.getId());
  }
  @Transactional
  public Category update(@NonNull CategoryDto categoryDto, @NonNull Supplier supplier) {
    LOGGER.info("Converting [{}] to Category", categoryDto);

    Category categoryToUpdate = categoryMapper.toEntity(categoryDto);

    LOGGER.info("Converted [{}]", categoryToUpdate);

    return update(categoryToUpdate, supplier);
  }

  @Transactional
  public Category update(@NonNull Category categoryToUpdate, @NonNull Supplier supplier) {
    LOGGER.info("Attempting to update [{}]", categoryToUpdate);

    Category persistedCategory = read(categoryToUpdate.getId(), supplier);
    if (categoryToUpdate.isPublic()) {
      supplierValidationService.validateCanCreatePublicCategories(supplier);
      categoryValidationService.validateCategoryCanBeMadePublic(persistedCategory,
          persistedCategory.getMenu().getSupplier());
    }

    persistedCategory.setName(categoryToUpdate.getName());
    persistedCategory.setPublic(categoryToUpdate.isPublic());
    persistedCategory.setOptions(categoryToUpdate.getOptions());

    persistedCategory = categoryRepository.save(persistedCategory);

    LOGGER.info("Updated [{}]", persistedCategory);
    return persistedCategory;
  }

  @Transactional
  public void delete(@NonNull UUID id, @NonNull Supplier supplier) {
    Category persistedCategory = read(id, supplier);
    categoryRepository.delete(persistedCategory);
  }
}
