package com.shaderock.lunch.backend.feature.supplier.controller;

import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SUPPLIER_ADM_SUPPLIER)
// todo when updating isPublic, make sure that there are no subscribers
public class SupplierAdmSupplierController {

}
