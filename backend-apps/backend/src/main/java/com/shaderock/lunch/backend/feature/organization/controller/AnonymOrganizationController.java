package com.shaderock.lunch.backend.feature.organization.controller;

import com.shaderock.lunch.backend.feature.organization.dto.PublicOrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import com.shaderock.lunch.backend.util.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ANONYM_ORGANIZATION)
public class AnonymOrganizationController {

  private final OrganizationDetailsMapper organizationDetailsMapper;
  private final SupplierService supplierService;
  private final ImageService imageService;

  @GetMapping("/supplier/all")
  @Transactional
  public ResponseEntity<List<PublicOrganizationDetailsDto>> readPublicSuppliers() {
    List<Supplier> suppliers = supplierService.read();
    List<OrganizationDetails> detailsList = suppliers.stream()
        .map(Supplier::getOrganizationDetails).toList();

    return ResponseEntity.ok(
        detailsList.stream().map(organizationDetailsMapper::toPublicDto).toList());
  }

  @SneakyThrows
  @Transactional
  @GetMapping(value = "/logo", produces = MediaType.IMAGE_JPEG_VALUE)
  @Cacheable(value = "logo", key = "#supplierId.toString() + '-' + #width + '-' + #maxHeight")
  public byte[] readMenuImage(@RequestParam @NotNull UUID supplierId,
      @RequestParam @NotNull int width, @RequestParam @NotNull int maxHeight) {
    Supplier supplier = supplierService.read(supplierId);

    byte[] logo = supplier.getOrganizationDetails().getLogo();

    return imageService.resizeByWidthAndCropToFitHeight(logo, width, maxHeight);
  }
}
