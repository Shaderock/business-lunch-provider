package com.shaderock.lunch.backend.order.service;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.order.model.dto.CategoryDto;
import com.shaderock.lunch.backend.order.model.entity.Category;
import com.shaderock.lunch.backend.order.model.entity.Menu;
import com.shaderock.lunch.backend.order.model.repository.CategoryRepository;
import com.shaderock.lunch.backend.order.repository.MenuRepository;
import com.shaderock.lunch.backend.organization.supplier.SupplierRepository;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;
  private final SupplierRepository<Supplier> supplierRepository;

  public CategoryDto readAndMapToDto(String name) {
    Category category = read(name);
    return new CategoryDto(category.getId(), category.getName());
  }

  public Category read(String name) {
    log.info("Attempting to read Category by name=[{}]", name);
    return categoryRepository.findByName(name)
        .orElseThrow(() -> new TransferableApplicationException(
            String.format("Category(name=[%s]) not found", name)));
  }

  public CategoryDto createAndMapToDto(CategoryDto categoryDto) {
    Category newCategory = create(categoryDto);
    return new CategoryDto(newCategory.getId(), newCategory.getName());
  }

  public Category create(CategoryDto categoryDto) throws TransferableApplicationException {
    log.info("Converting [{}] to Category", categoryDto);

    Category newCategory = new Category();
    newCategory.setName(categoryDto.name());

    log.info("Created category [{}]", newCategory);

    return create(newCategory);
  }

  public Category create(Category newCategory) {
    log.info("Attempting to create [{}]", newCategory);

    categoryRepository.findByName(newCategory.getName()).ifPresent(c -> {
      throw new TransferableApplicationException(String.format(
          "Category (name=[%s]) already exists and should be updated instead of created",
          c.getName()));
    });

    Supplier supplier = getSupplierForCrud();
    Menu menu = menuRepository.findBySupplier(supplier).orElseThrow(() -> {
      throw new IllegalStateException(
          String.format("[%s] does not have a Menu initialized", supplier));
    });
    newCategory.setMenu(menu);

    Category persistedCategory = categoryRepository.save(newCategory);

    log.info("Created [{}]", persistedCategory);
    return persistedCategory;
  }

  public CategoryDto updateAndMapToDto(CategoryDto categoryDto) {
    Category updatedCategory = update(categoryDto);
    return new CategoryDto(updatedCategory.getId(), updatedCategory.getName());
  }

  public Category update(CategoryDto categoryDto) {
    log.info("Converting [{}] to Category", categoryDto);
    Category categoryToUpdate = new Category();
    categoryToUpdate.setName(categoryDto.name());
    categoryToUpdate.setId(categoryDto.id());
    log.info("Converted [{}]", categoryToUpdate);

    return update(categoryToUpdate);
  }

  public Category update(Category categoryToUpdate) {
    log.info("Attempting to update [{}]", categoryToUpdate);

    if (categoryToUpdate.getId() == null) {
      throw new TransferableApplicationException("Category id not provided");
    }

    Category persistedCategory = categoryRepository.findById(categoryToUpdate.getId())
        .orElseThrow(() -> new TransferableApplicationException(
            String.format(
                "Category(id=[%s]) doesn't exist and should be created instead of updated",
                categoryToUpdate.getId())
        ));

    persistedCategory.setName(categoryToUpdate.getName());
    persistedCategory = categoryRepository.save(persistedCategory);
    log.info("Updated [{}]", persistedCategory);
    return persistedCategory;
  }

  public void delete(long id) {
    Category persistedCategory = categoryRepository.findById(id)
        .orElseThrow(() -> {
          throw new TransferableApplicationException(
              String.format("Category (id=[%s]) doesn't exist and can't be deleted", id));
        });

    persistedCategory.setDeleted(true);
    categoryRepository.save(persistedCategory);
  }

  private Supplier getSupplierForCrud() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    return supplierRepository.findByUsers_UserDetails_Email(userDetails.getUsername())
        .orElseThrow(() -> new TransferableApplicationException(
            "User is not a part of supplier organization"));
  }
}
