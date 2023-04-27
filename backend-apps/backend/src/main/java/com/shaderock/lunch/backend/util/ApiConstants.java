package com.shaderock.lunch.backend.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {

  public static final String API = "/api";
  public static final String SYS_ADM = API + "/sys-adm";
  public static final String SYS_ADM_ORGANIZATION = SYS_ADM + "/organization/details";
  public static final String SYS_ADM_USER = SYS_ADM + "/user";
  public static final String SYS_ADM_USER_DETAILS = SYS_ADM_USER + "/details";
  public static final String LOGIN = API + "/login";
  public static final String REGISTER = API + "/register";
  public static final String CATEGORY = API + "/category";
  public static final String COMPANY_ADM = API + "/company-adm";
  public static final String COMPANY_ADM_INVITATION = COMPANY_ADM + "/invitation";
  public static final String COMPANY_ADM_USER_DETAILS = COMPANY_ADM + "/user/details";
  public static final String EMPLOYEE = API + "/employee";
  public static final String EMPLOYEE_ORDER = EMPLOYEE + "/order";
  public static final String OPTION = API + "/option";
  public static final String ORGANIZATION_PART = "/organization";
  public static final String COMPANY_ADM_ORGANIZATION = COMPANY_ADM + ORGANIZATION_PART;
  public static final String ORGANIZATION = API + ORGANIZATION_PART;
  public static final String SUPPLIER_ADM = API + "/supplier-adm";
  public static final String SUPPLIER_ADM_CATEGORY = SUPPLIER_ADM + "/category";
  public static final String SUPPLIER_ADM_OPTION = SUPPLIER_ADM + "/option";
  public static final String SUPPLIER_ADM_ORGANIZATION = SUPPLIER_ADM + ORGANIZATION_PART;
  public static final String USER = API + "/user";
  public static final String USER_DETAILS = USER + "/details";
  public static final String USER_EMPLOYEE_PREFERENCES = USER + "/employee-preferences";
  public static final String ANONYM = API + "/anonym";
  public static final String ANONYM_ORGANIZATION = ANONYM + ORGANIZATION_PART;
  public static final String ANONYM_CATEGORY = ANONYM + "/category";
  public static final String ANONYM_OPTION = ANONYM + "/option";
  public static final String INVITATION = API + "/invitation";
  public static final String NOTIFICATIONS = API + "/notifications";
  private static final String ORGANIZATION_ADM = API + "/organization-adm";
  public static final String ORGANIZATION_ADM_ORGANIZATION = ORGANIZATION_ADM + ORGANIZATION_PART;
  private static final String SUBSCRIPTION_URL_PART = "/subscription";
  public static final String COMPANY_ADM_SUBSCRIPTION = COMPANY_ADM + SUBSCRIPTION_URL_PART;
  public static final String SUPPLIER_ADM_SUBSCRIPTION = SUPPLIER_ADM + SUBSCRIPTION_URL_PART;
  private static final String PREFERENCES_URL_PART = "/preferences";
  public static final String SUPPLIER_ADM_PREFERENCES = SUPPLIER_ADM + PREFERENCES_URL_PART;
  private static final String COMPANY_URL_PART = "/company";
  public static final String COMPANY_ADM_COMPANY = COMPANY_ADM + COMPANY_URL_PART;
  public static final String COMPANY_ADM_COMPANY_PREFERENCES =
      COMPANY_ADM_COMPANY + PREFERENCES_URL_PART;
  public static final String SYS_ADM_COMPANY = SYS_ADM + COMPANY_URL_PART;
  public static final String COMPANY = API + COMPANY_URL_PART;
  public static final String SUPPLIER_ADM_COMPANY = SUPPLIER_ADM + COMPANY_URL_PART;
  public static final String SUPPLIER_ADM_COMPANY_PREFERENCES =
      SUPPLIER_ADM_COMPANY + PREFERENCES_URL_PART;
  private static final String SUPPLIER_URL_PART = "/supplier";
  public static final String COMPANY_ADM_SUPPLIER = COMPANY_ADM + SUPPLIER_URL_PART;
  public static final String COMPANY_ADM_SUPPLIER_PREFERENCES =
      COMPANY_ADM_SUPPLIER + PREFERENCES_URL_PART;
  public static final String ANONYM_SUPPLIER = ANONYM + SUPPLIER_URL_PART;
  public static final String ANONYM_SUPPLIER_PREFERENCES = ANONYM_SUPPLIER + PREFERENCES_URL_PART;
  public static final String SYS_ADM_SUPPLIER = SYS_ADM + SUPPLIER_URL_PART;
  public static final String SUPPLIER = API + SUPPLIER_URL_PART;
  public static final String SUPPLIER_ADM_SUPPLIER = SUPPLIER_ADM + SUPPLIER_URL_PART;
  public static final String PUBLIC_SUPPLIER_PREFERENCES = SUPPLIER_URL_PART + PREFERENCES_URL_PART;
}
