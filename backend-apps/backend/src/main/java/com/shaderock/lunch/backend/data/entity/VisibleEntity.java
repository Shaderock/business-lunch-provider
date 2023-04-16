package com.shaderock.lunch.backend.data.entity;

import static com.shaderock.lunch.backend.utils.FilterManager.VISIBILITY_FILTER;
import static com.shaderock.lunch.backend.utils.FilterManager.VISIBILITY_FILTER_PARAM_NAME;

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
@FilterDef(name = VISIBILITY_FILTER,
    parameters = @ParamDef(name = VISIBILITY_FILTER_PARAM_NAME, type = BooleanJavaType.class),
    defaultCondition = "is_public = :" + VISIBILITY_FILTER_PARAM_NAME)
@Filter(name = VISIBILITY_FILTER, condition = "is_public = :" + VISIBILITY_FILTER_PARAM_NAME)
public class VisibleEntity extends DeletableEntity {

  @Column(nullable = false)
  protected boolean isPublic = false;

  public VisibleEntity(UUID id, boolean isDeleted, boolean isPublic) {
    super(id, isDeleted);
    this.isPublic = isPublic;
  }

  public VisibleEntity(UUID id, boolean isPublic) {
    super(id);
    this.isPublic = isPublic;
  }
}
