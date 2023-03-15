package com.shaderock.lunch.backend.menu.service;


import com.shaderock.lunch.backend.menu.mapper.CategoryMapper;
import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.type.DefaultCategory;
import com.shaderock.lunch.backend.menu.repository.CategoryRepository;
import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.utils.FilterManager;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
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

  private final FilterManager filterManager;

  @Transactional
  public Category create(@NonNull CategoryDto categoryDto, @NonNull Supplier supplier)
      throws TransferableApplicationException {
    LOGGER.info("Converting [{}] to Category", categoryDto);

    Category newCategory = categoryMapper.toEntity(categoryDto);

    LOGGER.info("Converted [{}]", newCategory);

    return create(newCategory, supplier);
  }

  @Transactional
  public Category create(@NonNull Category newCategory, @NonNull Supplier supplier) {
    LOGGER.info("Attempting to create [{}]", newCategory);

    validateCreate(newCategory, supplier);

    newCategory.setMenu(supplier.getMenu());

    Category persistedCategory = categoryRepository.save(newCategory);
    supplier.getMenu().getCategories().add(persistedCategory);

    LOGGER.info("Created [{}]", persistedCategory);
    return persistedCategory;
  }

  private void validateCreate(Category category, Supplier supplier) {
    categoryRepository.findByNameAndMenu_Supplier_Id(
        category.getName(), supplier.getId()).ifPresent(c -> {
      throw new CrudValidationException(String.format(
          "Category (name=[%s]) already exists and should be updated instead of created",
          c.getName()));
    });

    validateVisibility(category, supplier);
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

  public Category readPublic(@NonNull UUID id) {
    return categoryRepository.findByIsPublicTrueAndId(id).orElseThrow(
        () -> new CrudValidationException(String.format("Category(id=[%s]) not found", id)));
  }

  public List<Category> readPublic() {
    return categoryRepository.findByIsPublicTrue();
  }

  public List<Category> readPublic(Supplier supplier) {
    return categoryRepository.findByMenu_Supplier_IdAndIsPublicTrue(supplier.getId());
  }

  public List<CategoryDto> readAllDefault() {
    return Stream.of(DefaultCategory.values()).map(defaultCategory -> new CategoryDto(null,
        defaultCategory.getName(), Collections.emptySet(), true, null)).toList();
  }

  public List<Category> readAllDeleted() {
    filterManager.enableDeleteFilter();
    List<Category> all = read();
    filterManager.disableDeleteFilter();
    return all;
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
    validateVisibility(persistedCategory, persistedCategory.getMenu().getSupplier());

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

  private void validateVisibility(Category category, Supplier supplier) {
    if (!supplier.isPublic() && category.isPublic()) {
      throw new CrudValidationException(
          String.format(
              "Can not set public Category(id=[%s]) because Supplier(id=[%s]) is not public",
              category.getId(), supplier.getId()));
    }
  }
}
