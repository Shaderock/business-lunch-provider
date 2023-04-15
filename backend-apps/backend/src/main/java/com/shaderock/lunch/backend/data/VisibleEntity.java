package com.shaderock.lunch.backend.data;

import static com.shaderock.lunch.backend.utils.FilterManager.VISIBILITY_FILTER;
import static com.shaderock.lunch.backend.utils.FilterManager.VISIBILITY_FILTER_PARAM_NAME;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
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
    defaultCondition = "is_deleted = :" + VISIBILITY_FILTER_PARAM_NAME)
@Filter(name = VISIBILITY_FILTER, condition = "is_deleted = :" + VISIBILITY_FILTER_PARAM_NAME)
public class VisibleEntity extends DeletableEntity {

  @Column(nullable = false)
  protected boolean isPublic = false;

}
