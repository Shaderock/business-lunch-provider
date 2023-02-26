package com.shaderock.lunch.backend.menu.service;

import com.shaderock.lunch.backend.menu.mapper.OptionMapper;
import com.shaderock.lunch.backend.menu.model.dto.OptionDto;
import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.menu.repository.MenuRepository;
import com.shaderock.lunch.backend.menu.repository.OptionRepository;
import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.organization.repository.OrganizationDetailsRepository;
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
// todo fix
public class OptionService {

  private final SupplierRepository supplierRepository;

  private final MenuRepository menuRepository;
  private final OptionRepository optionRepository;
  private final OrganizationDetailsRepository organizationDetailsRepository;
  private final OptionMapper optionMapper;

  public OptionDto readAndMapToDto(String name) {
    Option option = read(name);
    return optionMapper.toDto(option);
  }

  public Option read(String name) {
    LOGGER.info("Attempting to read Option by name=[{}]", name);
    return optionRepository.findByName(name)
        .orElseThrow(() -> new TransferableApplicationException(
            String.format("Option(name=[%s]) not found", name)));
  }

  public OptionDto createAndMapToDto(OptionDto optionDto) {
    Option newOption = create(optionDto);
    return optionMapper.toDto(newOption);
  }

  @Transactional
  public Option create(OptionDto optionDto) throws TransferableApplicationException {
    LOGGER.info("Converting [{}] to Option", optionDto);

    Option newOption = new Option();
    newOption.setName(optionDto.name());

    LOGGER.info("Converted [{}]", newOption);

    return create(newOption);
  }

  @Transactional
  public Option create(Option newOption) {
    LOGGER.info("Attempting to create [{}]", newOption);

    optionRepository.findByName(newOption.getName()).ifPresent(c -> {
      throw new TransferableApplicationException(String.format(
          "Option (name=[%s]) already exists and should be updated instead of created",
          c.getName()));
    });

    Supplier supplier = getSupplierForCrud();
    Menu menu = menuRepository.findBySupplier(supplier).orElseThrow(() -> {
      throw new IllegalStateException(
          String.format("[%s] does not have a Menu initialized", supplier));
    });
//    newOption.setMenu(menu);

    Option persistedOption = optionRepository.save(newOption);

    LOGGER.info("Created [{}]", persistedOption);
    return persistedOption;
  }

  @Transactional
  public OptionDto updateAndMapToDto(OptionDto optionDto) {
    Option updatedOption = update(optionDto);
//    return toDto(updatedOption);
    return optionMapper.toDto(updatedOption);
  }

  @Transactional
  public Option update(OptionDto optionDto) {
    LOGGER.info("Converting [{}] to Option", optionDto);
    Option optionToUpdate = new Option();
    optionToUpdate.setName(optionDto.name());
    optionToUpdate.setId(optionDto.id());
    LOGGER.info("Converted [{}]", optionToUpdate);

    return update(optionToUpdate);
  }

  @Transactional
  public Option update(Option optionToUpdate) {
    LOGGER.info("Attempting to update [{}]", optionToUpdate);

    if (optionToUpdate.getId() == null) {
      throw new TransferableApplicationException("Option id not provided");
    }

    Option persistedOption = optionRepository.findById(optionToUpdate.getId())
        .orElseThrow(() -> new TransferableApplicationException(
            String.format(
                "Option(id=[%s]) doesn't exist and should be created instead of updated",
                optionToUpdate.getId())
        ));

    persistedOption.setName(optionToUpdate.getName());
    persistedOption = optionRepository.save(persistedOption);
    LOGGER.info("Updated [{}]", persistedOption);
    return persistedOption;
  }

  @Transactional
  public void delete(long id) {
    Option persistedOption = optionRepository.findById(id)
        .orElseThrow(() -> {
          throw new TransferableApplicationException(
              String.format("Option (id=[%s]) doesn't exist and can't be deleted", id));
        });

    persistedOption.setDeleted(true);
    optionRepository.save(persistedOption);
  }

  private Supplier getSupplierForCrud() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    return supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(
            userDetails.getUsername())
        .orElseThrow(() -> new TransferableApplicationException(
            "User is not a part of supplier organization"));
  }

}
