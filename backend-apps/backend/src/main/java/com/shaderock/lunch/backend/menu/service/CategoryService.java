package com.shaderock.lunch.backend.menu.service;


import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.menu.model.mapper.CategoryMapper;
import com.shaderock.lunch.backend.menu.repository.CategoryRepository;
import com.shaderock.lunch.backend.menu.repository.MenuRepository;
import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;
  private final SupplierRepository supplierRepository;
  private final CategoryMapper categoryMapper;

  public CategoryDto readAndMapToDto(String name) {
    Category category = read(name);
    return categoryMapper.toDto(category);
  }

  public Category read(String name) {
    LOGGER.info("Attempting to read Category by name=[{}]", name);
    return categoryRepository.findByName(name)
        .orElseThrow(() -> new TransferableApplicationException(
            String.format("Category(name=[%s]) not found", name)));
  }

  public CategoryDto createAndMapToDto(CategoryDto categoryDto) {
    Category newCategory = create(categoryDto);
    return categoryMapper.toDto(newCategory);
  }

  @Transactional
  public Category create(CategoryDto categoryDto) throws TransferableApplicationException {
    LOGGER.info("Converting [{}] to Category", categoryDto);

    Category newCategory = new Category();
    newCategory.setName(categoryDto.name());

    LOGGER.info("Converted [{}]", newCategory);

    return create(newCategory);
  }

  @Transactional
  public Category create(Category newCategory) {
    LOGGER.info("Attempting to create [{}]", newCategory);

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

    LOGGER.info("Created [{}]", persistedCategory);
    return persistedCategory;
  }

  @Transactional
  public CategoryDto updateAndMapToDto(CategoryDto categoryDto) {
    Category updatedCategory = update(categoryDto);
    return categoryMapper.toDto(updatedCategory);
  }

  @Transactional
  public Category update(CategoryDto categoryDto) {
    LOGGER.info("Converting [{}] to Category", categoryDto);
    Category categoryToUpdate = new Category();
    categoryToUpdate.setName(categoryDto.name());
    categoryToUpdate.setId(categoryDto.id());
    LOGGER.info("Converted [{}]", categoryToUpdate);

    return update(categoryToUpdate);
  }

  @Transactional
  public Category update(Category categoryToUpdate) {
    LOGGER.info("Attempting to update [{}]", categoryToUpdate);

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
    LOGGER.info("Updated [{}]", persistedCategory);
    return persistedCategory;
  }

  @Transactional
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
