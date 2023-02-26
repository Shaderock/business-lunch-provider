package com.shaderock.lunch.backend.menu.service;

import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.menu.repository.MenuRepository;
import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

  private final MenuRepository menuRepository;

  @Transactional
  public Menu create(Menu menu) {
    LOGGER.info("Attempting to create {}", menu);
    if (Objects.isNull(menu)) {
      throw new CrudValidationException("Can not create null");
    }
    if (Objects.isNull(menu.getSupplier())) {
      throw new CrudValidationException("Menu has no supplier assigned");
    }
    Menu persistedMenu = menuRepository.save(menu);
    LOGGER.info("Created {}", menu);

    return persistedMenu;
  }

  public Menu read(Long id) {
    return menuRepository.findById(id).orElseThrow(
        () -> new CrudValidationException(String.format("Menu(id=[%s] not found", id)));
  }

  @Transactional
  public Menu update(Menu menu) {
    LOGGER.info("Attempting to update {}", menu);
    if (Objects.isNull(menu)) {
      throw new CrudValidationException("Can not update null");
    }
    if (Objects.isNull(menu.getSupplier())) {
      throw new CrudValidationException("Menu has no supplier assigned");
    }

    Menu persistedMenu = read(menu.getId());
    persistedMenu.setCategories(menu.getCategories());
    persistedMenu.setSupplier(menu.getSupplier());

    LOGGER.info("Updated {}", persistedMenu);
    return persistedMenu;
  }


  public void delete(Menu menu) {
    // todo validate
    menuRepository.delete(menu);
  }
}
