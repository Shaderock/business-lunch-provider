package com.shaderock.lunch.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {

  public static final String API = "/api";
  public static final String SYS_ADM = API + "/sys-adm";
  public static final String SYS_ADM_ORGANIZATION_DETAILS = SYS_ADM + "/organization/details";
  public static final String SYS_ADM_SUPPLIER = SYS_ADM + "/supplier";
  public static final String SYS_ADM_USER = SYS_ADM + "/user";
  public static final String SYS_ADM_COMPANY = SYS_ADM + "/company";
  public static final String LOGIN = API + "/login";
  public static final String REGISTER = API + "/register";
  public static final String CATEGORY = API + "/category";
  public static final String COMPANY = API + "/company";
  public static final String COMPANY_PREFERENCES = COMPANY + "/preferences";
  public static final String SUPPLIER = API + "/supplier";
  public static final String OPTION = API + "/option";
  public static final String ORGANIZATION = API + "/organization";
  public static final String SUPPLIER_ADM = API + "/supplier-adm";
  public static final String SUPPLIER_ADM_CATEGORY = SUPPLIER_ADM + "/category";
  public static final String SUPPLIER_ADM_OPTION = SUPPLIER_ADM + "/option";
  public static final String USER = API + "/user";
}
