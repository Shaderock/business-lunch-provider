package com.shaderock.lunch.backend.feature.order.company.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.order.company.dto.CompanyOrderDto;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrderMapper;
import com.shaderock.lunch.backend.feature.order.company.repository.CompanyOrderRepository;
import com.shaderock.lunch.backend.feature.order.company.type.CompanyOrderStatus;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyOrderService {

  private final CompanyOrderRepository companyOrderRepository;

  private final CompanyOrderValidationService companyOrderValidationService;
  private final CompanyOrderMapper companyOrderMapper;
  private final EmployeeOrderService employeeOrderService;

  @Transactional
  public CompanyOrder create(CompanyOrderDto companyOrderDto, Company company) {
    CompanyOrder order = companyOrderMapper.toEntity(companyOrderDto);
    order = convertMappedEmployeesOrdersToValidEmployeesOrders(order, company);
    companyOrderValidationService.validateCreate(order);

    order.setStatus(CompanyOrderStatus.PENDING_SUPPLIER_CONFIRMATION);
    order.setCreatedAt(LocalDateTime.now());
    return companyOrderRepository.save(order);
  }

  public CompanyOrder convertMappedEmployeesOrdersToValidEmployeesOrders(CompanyOrder companyOrder,
      Company company) {
    Set<EmployeeOrder> employeeOrderList = new HashSet<>();

    for (EmployeeOrder employeeOrder : companyOrder.getEmployeesOrders()) {
      employeeOrderList.add(employeeOrderService.read(employeeOrder.getId(), company));
    }

    companyOrder.setEmployeesOrders(employeeOrderList);
    return companyOrder;
  }

  public CompanyOrder read(@NonNull UUID orderId, @NonNull Company company) {
    return companyOrderRepository.findByIdAndEmployeesOrders_AppUser_OrganizationDetails(orderId,
        company.getOrganizationDetails()).orElseThrow(() -> new CrudValidationException(
        String.format("Company order not found for Company(name=[%s])",
            company.getOrganizationDetails().getName())));
  }

  public List<CompanyOrder> read(@NonNull LocalDate date, @NonNull Company company) {
    return companyOrderRepository.findByEmployeesOrders_AppUser_OrganizationDetails(
            company.getOrganizationDetails()).stream()
        .filter(d -> d.getDeliverAt().toLocalDate().isEqual(date))
        .toList();
  }

  @Transactional
  public CompanyOrder update() {
    throw new NotImplementedException();
  }

  @Transactional
  public void delete(@NonNull CompanyOrder companyOrder) {
    if (companyOrder.getStatus() == CompanyOrderStatus.CONFIRMED) {
      throw new CrudValidationException("Can not delete an already confirmed order");
    }

    companyOrderRepository.delete(companyOrder);
  }
}
