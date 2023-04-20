export class RouterConstants {
  static readonly USER_COMPANY_INVITATIONS: string = '/user/companies-invitations'

  static readonly EMPLOYEE_OR_SUPPLIER_COMPANY_INVITATIONS: string = '/organization-details'

  private static readonly EMPLOYEE: string = '/employee'
  static readonly EMPLOYEE_SUBSCRIBED_SUPPLIER: string = RouterConstants.EMPLOYEE + '/subscribed-suppliers'
  static readonly EMPLOYEE_ORDER_HISTORY: string = RouterConstants.EMPLOYEE + '/orders-history'
  static readonly EMPLOYEE_PENDING_ORDERS: string = RouterConstants.EMPLOYEE + '/pending-orders'
  static readonly EMPLOYEE_DASHBOARD: string = RouterConstants.EMPLOYEE + '/dashboard'

  private static readonly COMPANY_ADM: string = '/company-adm'
  static readonly COMPANY_ADM_COMPANY_PREFERENCES: string = RouterConstants.COMPANY_ADM + '/company-preferences'
  static readonly COMPANY_ADM_COMPANY_NOTIFICATIONS: string = RouterConstants.COMPANY_ADM + '/company-notification-config'
  static readonly COMPANY_ADM_EMPLOYEES_ORDERS: string = RouterConstants.COMPANY_ADM + '/employees-orders'
  static readonly COMPANY_ADM_COMPANY_DASHBOARD: string = RouterConstants.COMPANY_ADM + '/company-dashboard'
  static readonly COMPANY_ADM_EMPLOYEES: string = RouterConstants.COMPANY_ADM + '/employees'
  static readonly COMPANY_ADM_INVITED_USERS: string = RouterConstants.COMPANY_ADM + '/invited'

  private static readonly SUPPLIER_ADM: string = '/supplier-adm'
  static readonly SUPPLIER_ADM_SUPPLIER_PREFERENCES: string = RouterConstants.SUPPLIER_ADM + '/supplier-preferences'
  static readonly SUPPLIER_ADM_SUBSCRIBED_COMPANIES: string = RouterConstants.SUPPLIER_ADM + '/subscribed-companies'
  static readonly SUPPLIER_ADM_FOOD_CATEGORIES: string = RouterConstants.SUPPLIER_ADM + '/food-categories'
  static readonly SUPPLIER_ADM_FOOD_OPTIONS: string = RouterConstants.SUPPLIER_ADM + '/food-options'
  static readonly SUPPLIER_ADM_COMPANIES_ORDERS: string = RouterConstants.SUPPLIER_ADM + '/companies-orders'
  static readonly SUPPLIER_ADM_DASHBOARD: string = RouterConstants.SUPPLIER_ADM + '/supplier-dashboard'
}
