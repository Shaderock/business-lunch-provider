package com.shaderock.lunch.backend.organization.supplier.model.entity;

import static com.shaderock.lunch.backend.utils.FilterManager.DELETED_FILTER;

import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.net.URI;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "suppliers")
@SQLDelete(sql = "UPDATE suppliers SET deleted = true WHERE id=?")
@FilterDef(name = DELETED_FILTER, parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = DELETED_FILTER, condition = "deleted = :isDeleted")
public class Supplier {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private URI websiteUrl;
  @Column(nullable = false)
  private URI menuUrl;

  @Column(nullable = false)
  private boolean deleted = Boolean.FALSE;

  @Exclude
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_details_id", nullable = false)
  private OrganizationDetails organizationDetails;
  @Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id")
  private Menu menu;
  @Exclude
  @OneToOne(mappedBy = "supplier")
  private SupplierPreferences preferences;
  @JoinTable(name = "lunch_subscriptions",
      joinColumns = @JoinColumn(name = "supplier_id"),
      inverseJoinColumns = @JoinColumn(name = "company_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  @Exclude
  private Set<Company> subscribers;
  @JoinTable(name = "lunch_subscriptions_requests",
      joinColumns = @JoinColumn(name = "supplier_id"),
      inverseJoinColumns = @JoinColumn(name = "company_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  @Exclude
  private Set<Company> subscriptionsRequests;
}
