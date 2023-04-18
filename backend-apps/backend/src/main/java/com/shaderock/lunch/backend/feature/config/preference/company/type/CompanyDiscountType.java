package com.shaderock.lunch.backend.feature.config.preference.company.type;

public enum CompanyDiscountType {
  /**
   * A percentage discount is applied for the first order
   */
  PERCENTAGE_FIRST,
  /**
   * A fixed discount value is applied for the first order
   */
  SPECIFIC_AMOUNT_FIRST,
  /**
   * A percentage discount is applied for the first order, however it can't exceed maximum discount
   * value
   */
  SPECIFIC_OVER_PERCENTAGE_FIRST,

  /**
   * A fixed discount value is applied on any orders until not spent fully
   */
  SPECIFIC_PER_DAY
}
