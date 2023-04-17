package com.shaderock.lunch.backend.data.entity;

import static com.shaderock.lunch.backend.util.FilterManager.SOFT_DELETE_FILTER;
import static com.shaderock.lunch.backend.util.FilterManager.SOFT_DELETE_FILTER_PARAM_NAME;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@MappedSuperclass
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FilterDef(name = SOFT_DELETE_FILTER,
    parameters = @ParamDef(name = SOFT_DELETE_FILTER_PARAM_NAME, type = BooleanJavaType.class),
    defaultCondition = "is_deleted = :" + SOFT_DELETE_FILTER_PARAM_NAME)
@Filter(name = SOFT_DELETE_FILTER, condition = "is_deleted = :" + SOFT_DELETE_FILTER_PARAM_NAME)
public class DeletableEntity extends BaseEntity {

  @Column(nullable = false, name = "is_deleted")
  protected boolean isDeleted = false;

  public DeletableEntity(UUID id, boolean isDeleted) {
    super(id);
    this.isDeleted = isDeleted;
  }

  public DeletableEntity(UUID id) {
    super(id);
  }
}
