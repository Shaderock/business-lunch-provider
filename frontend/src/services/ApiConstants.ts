export class ApiConstants {
  static readonly API: string = "/api"

  static readonly BACKEND_URL: string = `${process.env.VUE_APP_BACKEND_URL ?? 'http://104.248.80.209:8081'}${ApiConstants.API}`

  static readonly SYS_ADM: string = ApiConstants.BACKEND_URL + "/sys-adm"
  static readonly SYS_ADM_ORGANIZATION: string = ApiConstants.SYS_ADM + "/organization/details"
  static readonly SYS_ADM_USER: string = ApiConstants.SYS_ADM + "/user"
  static readonly SYS_ADM_USER_DETAILS: string = ApiConstants.SYS_ADM_USER + "/details"
  static readonly LOGIN: string = ApiConstants.BACKEND_URL + "/login"
  static readonly REGISTER: string = ApiConstants.BACKEND_URL + "/register"
  static readonly CATEGORY: string = ApiConstants.BACKEND_URL + "/category"
  static readonly COMPANY_ADM: string = ApiConstants.BACKEND_URL + "/company-adm"
  static readonly COMPANY_ADM_INVITATION: string = ApiConstants.COMPANY_ADM + "/invitation"
  static readonly COMPANY_ADM_USER_DETAILS: string = ApiConstants.COMPANY_ADM + "/user/details"
  static readonly COMPANY_ADM_EMPLOYEE_ORDER: string = ApiConstants.COMPANY_ADM + "/employee-order";
  static readonly EMPLOYEE: string = ApiConstants.BACKEND_URL + "/employee"
  static readonly EMPLOYEE_ORDER: string = ApiConstants.EMPLOYEE + "/order"
  static readonly OPTION: string = ApiConstants.BACKEND_URL + "/option"
  static readonly ORGANIZATION_PART = "/organization"
  static readonly COMPANY_ADM_ORGANIZATION: string = ApiConstants.COMPANY_ADM + ApiConstants.ORGANIZATION_PART;
  static readonly ORGANIZATION: string = ApiConstants.BACKEND_URL + ApiConstants.ORGANIZATION_PART
  static readonly SUPPLIER_ADM: string = ApiConstants.BACKEND_URL + "/supplier-adm"
  static readonly SUPPLIER_ADM_CATEGORY: string = ApiConstants.SUPPLIER_ADM + "/category"
  static readonly SUPPLIER_ADM_OPTION: string = ApiConstants.SUPPLIER_ADM + "/option"
  static readonly SUPPLIER_ADM_ORGANIZATION: string = ApiConstants.SUPPLIER_ADM + ApiConstants.ORGANIZATION_PART
  static readonly USER: string = ApiConstants.BACKEND_URL + "/user"
  static readonly USER_DETAILS: string = ApiConstants.USER + "/details"
  static readonly USER_EMPLOYEE_PREFERENCES: string = ApiConstants.USER + "/employee-preferences"
  static readonly ANONYM: string = ApiConstants.BACKEND_URL + "/anonym"
  static readonly ANONYM_OPTION: string = ApiConstants.ANONYM + "/option";
  static readonly ANONYM_ORGANIZATION: string = ApiConstants.ANONYM + ApiConstants.ORGANIZATION_PART
  static readonly INVITATION: string = ApiConstants.BACKEND_URL + "/invitation"
  static readonly NOTIFICATIONS: string = ApiConstants.BACKEND_URL + "/notifications"
  static readonly ORGANIZATION_ADM: string = ApiConstants.BACKEND_URL + "/organization-adm"
  static readonly ORGANIZATION_ADM_ORGANIZATION: string = ApiConstants.ORGANIZATION_ADM + ApiConstants.ORGANIZATION_PART
  static readonly SUBSCRIPTION_URL_PART: string = "/subscription"
  static readonly COMPANY_ADM_SUBSCRIPTION: string = ApiConstants.COMPANY_ADM + ApiConstants.SUBSCRIPTION_URL_PART
  static readonly SUPPLIER_ADM_SUBSCRIPTION: string = ApiConstants.SUPPLIER_ADM + ApiConstants.SUBSCRIPTION_URL_PART
  static readonly PREFERENCES_URL_PART: string = "/preferences"
  static readonly SUPPLIER_ADM_PREFERENCES: string = ApiConstants.SUPPLIER_ADM + ApiConstants.PREFERENCES_URL_PART
  static readonly COMPANY_URL_PART: string = "/company"
  static readonly EMPLOYEE_COMPANY_PREFERENCES: string =
    ApiConstants.EMPLOYEE + ApiConstants.COMPANY_URL_PART + ApiConstants.PREFERENCES_URL_PART;
  static readonly COMPANY_ADM_COMPANY: string = ApiConstants.COMPANY_ADM + ApiConstants.COMPANY_URL_PART
  static readonly COMPANY_ADM_COMPANY_PREFERENCES: string =
    ApiConstants.COMPANY_ADM_COMPANY + ApiConstants.PREFERENCES_URL_PART
  static readonly SYS_ADM_COMPANY: string = ApiConstants.SYS_ADM + ApiConstants.COMPANY_URL_PART
  static readonly COMPANY: string = ApiConstants.BACKEND_URL + ApiConstants.COMPANY_URL_PART
  static readonly SUPPLIER_ADM_COMPANY: string = ApiConstants.SUPPLIER_ADM + ApiConstants.COMPANY_URL_PART
  static readonly SUPPLIER_ADM_COMPANY_PREFERENCES: string = ApiConstants.SUPPLIER_ADM_COMPANY + ApiConstants.PREFERENCES_URL_PART
  static readonly SUPPLIER_URL_PART: string = "/supplier"
  static readonly COMPANY_ADM_SUPPLIER: string = ApiConstants.COMPANY_ADM + ApiConstants.SUPPLIER_URL_PART
  static readonly COMPANY_ADM_SUPPLIER_PREFERENCES: string = ApiConstants.COMPANY_ADM_SUPPLIER + ApiConstants.PREFERENCES_URL_PART
  static readonly ANONYM_SUPPLIER: string = ApiConstants.ANONYM + ApiConstants.SUPPLIER_URL_PART
  static readonly ANONYM_SUPPLIER_PREFERENCES: string = ApiConstants.ANONYM_SUPPLIER + ApiConstants.PREFERENCES_URL_PART
  static readonly SYS_ADM_SUPPLIER: string = ApiConstants.SYS_ADM + ApiConstants.SUPPLIER_URL_PART
  static readonly SUPPLIER: string = ApiConstants.BACKEND_URL + ApiConstants.SUPPLIER_URL_PART
  static readonly SUPPLIER_ADM_SUPPLIER: string = ApiConstants.SUPPLIER_ADM + ApiConstants.SUPPLIER_URL_PART
  static readonly PUBLIC_SUPPLIER_PREFERENCES: string = ApiConstants.SUPPLIER_URL_PART + ApiConstants.PREFERENCES_URL_PART
  static readonly ANONYM_CATEGORIES: string = ApiConstants.ANONYM + "/category";
  static readonly EMPLOYEE_COMPANY_SUBSCRIPTION: string = ApiConstants.EMPLOYEE + ApiConstants.COMPANY_URL_PART + ApiConstants.SUBSCRIPTION_URL_PART;
  static readonly EMPLOYEE_OPTION: string = ApiConstants.EMPLOYEE + "/option";
  static readonly COMPANY_ADM_COMPANY_ORDER: string = ApiConstants.COMPANY_ADM + "/company-order";
  static readonly SUPPLIER_ADM_COMPANY_ORDER: string = ApiConstants.SUPPLIER_ADM + "/company-order";
  static readonly SUPPLIER_ADM_EMPLOYEE_ORDER: string = ApiConstants.SUPPLIER_ADM + "/employee-order";
  static readonly EMPLOYEE_SUPPLIER: string = ApiConstants.EMPLOYEE + ApiConstants.SUPPLIER_URL_PART;
  static readonly EMPLOYEE_SUPPLIER_PREFERENCES: string = ApiConstants.EMPLOYEE + ApiConstants.SUPPLIER_URL_PART +
    ApiConstants.PREFERENCES_URL_PART;
  static readonly EMPLOYEE_ORGANIZATION: string = ApiConstants.EMPLOYEE + ApiConstants.ORGANIZATION_PART;
  static readonly SUPPLIER_ADM_CATEGORIES_PRICE: string = ApiConstants.SUPPLIER_ADM + "/categories-price";
  static readonly ANONYM_CATEGORIES_PRICE: string = ApiConstants.ANONYM + "/categories-price";
}
