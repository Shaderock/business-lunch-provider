package com.shaderock.lunch.backend.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {

  public static final String API = "/api";
  public static final String SYS_ADM = API + "/sys-adm";
  public static final String SYS_ADM_ORGANIZATION = SYS_ADM + "/organization/details";
  public static final String SYS_ADM_USER = SYS_ADM + "/user";
  public static final String LOGIN = API + "/login";
  public static final String REGISTER = API + "/register";
  public static final String CATEGORY = API + "/category";
  public static final String COMPANY_ADM = API + "/company-adm";
  public static final String OPTION = API + "/option";
  public static final String ORGANIZATION = API + "/organization";
  public static final String SUPPLIER_ADM = API + "/supplier-adm";
  public static final String SUPPLIER_ADM_CATEGORY = SUPPLIER_ADM + "/category";
  public static final String SUPPLIER_ADM_OPTION = SUPPLIER_ADM + "/option";
  public static final String SUPPLIER_ADM_ORGANIZATION = SUPPLIER_ADM + "/organization";
  public static final String USER = API + "/user";
  public static final String NOTIFICATIONS = API + "/notifications";
  private static final String SUBSCRIPTION_URL_PART = "/subscription";
  public static final String COMPANY_ADM_SUBSCRIPTION = COMPANY_ADM + SUBSCRIPTION_URL_PART;
  public static final String SUPPLIER_ADM_SUBSCRIPTION = SUPPLIER_ADM + SUBSCRIPTION_URL_PART;
  private static final String PREFERENCES_URL_PART = "/preferences";
  public static final String SUPPLIER_ADM_PREFERENCES = SUPPLIER_ADM + PREFERENCES_URL_PART;
  private static final String COMPANY_URL_PART = "/company";
  public static final String SYS_ADM_COMPANY = SYS_ADM + COMPANY_URL_PART;
  public static final String COMPANY = API + COMPANY_URL_PART;
  public static final String COMPANY_PREFERENCES = COMPANY + PREFERENCES_URL_PART;
  public static final String SUPPLIER_ADMIN_COMPANY = SUPPLIER_ADM + COMPANY_URL_PART;
  public static final String SUPPLIER_ADM_COMPANY_PREFERENCES =
      SUPPLIER_ADMIN_COMPANY + PREFERENCES_URL_PART;
  private static final String SUPPLIER_URL_PART = "/supplier";
  public static final String SYS_ADM_SUPPLIER = SYS_ADM + SUPPLIER_URL_PART;
  public static final String SUPPLIER = API + SUPPLIER_URL_PART;
  public static final String SUPPLIER_ADM_SUPPLIER = SUPPLIER_ADM + SUPPLIER_URL_PART;
  public static final String PUBLIC_SUPPLIER_PREFERENCES = SUPPLIER_URL_PART + PREFERENCES_URL_PART;
}