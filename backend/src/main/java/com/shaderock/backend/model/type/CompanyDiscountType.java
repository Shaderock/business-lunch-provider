package com.shaderock.backend.model.type;

public enum CompanyDiscountType {
  /**
   * A percentage discount is applied for any option but only for the first order
   */
  PERCENTAGE_FIRST,
  /**
   * A constant discount value is applied for any option but only for the first order
   */
  SPECIFIC_AMOUNT_FIRST,
  /**
   * A percentage discount is applied for any option but only for the first order, however it can't exceed maximum
   * discount value
   */
  SPECIFIC_OVER_PERCENTAGE_FIRST
}
