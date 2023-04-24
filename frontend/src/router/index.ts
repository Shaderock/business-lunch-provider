// Composables
import {createRouter, createWebHistory, Router} from 'vue-router'

import {RouterPaths} from "@/services/RouterPaths";
import {useAuthStore, useProfileStore} from "@/store/user-app";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";

const routes = [
  // anonymous

  {
    path: '/', name: 'Home',
    component: () => import('@/views/any/Home.vue')
  },
  {
    path: RouterPaths.ANONYMOUS_LOGIN, name: 'Anonymous Login',
    component: () => import("@/views/anonymous/auth/Login.vue")
  },
  {
    path: RouterPaths.ANONYMOUS_REGISTER, name: 'Anonymous Register',
    component: () => import('@/views/anonymous/auth/Register.vue')
  },

  // user

  {
    path: RouterPaths.USER_PROFILE, name: 'User Profile',
    component: () => import('@/views/user/Profile.vue')
  },
  {
    path: RouterPaths.USER_COMPANY_INVITATIONS, name: 'User Company Invitations',
    component: () => import('@/views/user/CompanyInvitations.vue')
  },
  {
    path: RouterPaths.USER_NOTIFICATIONS, name: 'User Notifications',
    component: () => import('@/views/user/UserNotifications.vue')
  },

  // employee

  {
    path: RouterPaths.EMPLOYEE_COMPANY_SUPPLIERS_SUBSCRIPTIONS, name: 'My Company Suppliers Subscriptions',
    component: () => import('@/views/company-admin/SupplierSubscriptions.vue')
  },
  {
    path: RouterPaths.EMPLOYEE_ORDER_HISTORY, name: 'Employee Order History',
    component: () => import('@/views/employee/EmployeeOrderHistory.vue')
  },
  {
    path: RouterPaths.EMPLOYEE_PENDING_ORDERS, name: 'Employee Pending Orders',
    component: () => import('@/views/employee/EmployeePendingOrders.vue')
  },
  {
    path: RouterPaths.EMPLOYEE_DASHBOARD, name: 'Employee Dashboard',
    component: () => import('@/views/employee/EmployeeDashboard.vue')
  },
  {
    path: RouterPaths.EMPLOYEE_NOTIFICATIONS_CONFIG, name: 'Employee Notification Config',
    component: () => import('@/views/employee/EmployeeNotificationConfig.vue')
  },
  {
    path: RouterPaths.EMPLOYEE_PREFERENCES_CONFIG, name: 'Employee Preferences Config',
    component: () => import('@/views/employee/EmployeePreferencesConfig.vue')
  }, {
    path: RouterPaths.EMPLOYEE_CART, name: 'Employee Cart',
    component: () => import('@/views/employee/EmployeeCart.vue')
  },

  // employee or supplier

  {
    path: RouterPaths.EMPLOYEE_OR_SUPPLIER_ORGANIZATION_DETAILS, name: 'My Organization Details',
    component: () => import('@/views/employee-or-supplier/OrganizationDetails.vue')
  },

  // company administrator

  {
    path: RouterPaths.COMPANY_ADM_COMPANY_PREFERENCES, name: 'Company Preferences',
    component: () => import('@/views/company-admin/CompanyPreferences.vue')
  },
  {
    path: RouterPaths.COMPANY_ADM_COMPANY_NOTIFICATIONS, name: 'Company Notifications',
    component: () => import('@/views/company-admin/CompanyNotifications.vue')
  },
  {
    path: RouterPaths.COMPANY_ADM_EMPLOYEES_ORDERS, name: 'Company Employees Orders',
    component: () => import('@/views/company-admin/EmployeesOrders.vue')
  },
  {
    path: RouterPaths.COMPANY_ADM_COMPANY_DASHBOARD, name: 'Company Dashboard',
    component: () => import('@/views/company-admin/CompanyDashboard.vue')
  },
  {
    path: RouterPaths.COMPANY_ADM_EMPLOYEES, name: 'Company Employees',
    component: () => import('@/views/company-admin/CompanyEmployees.vue')
  },
  {
    path: RouterPaths.COMPANY_ADM_INVITED_USERS, name: 'Company Invited Users',
    component: () => import('@/views/company-admin/CompanyInvitedUsers.vue')
  },

  // supplier administrator

  {
    path: RouterPaths.SUPPLIER_ADM_SUPPLIER_PREFERENCES, name: 'Supplier Preferences',
    component: () => import('@/views/supplier-admin/SupplierPreferences.vue')
  },
  {
    path: RouterPaths.SUPPLIER_ADM_SUBSCRIBED_COMPANIES, name: 'Supplier Companies Subscribers',
    component: () => import('@/views/supplier-admin/SupplierSubscribers.vue')
  },
  {
    path: RouterPaths.SUPPLIER_ADM_FOOD_CATEGORIES, name: 'Supplier Food Categories',
    component: () => import('@/views/supplier-admin/SupplierFoodCategories.vue')
  },
  {
    path: RouterPaths.SUPPLIER_ADM_FOOD_OPTIONS, name: 'Supplier Food Options',
    component: () => import('@/views/supplier-admin/SupplierFoodOptions.vue')
  },
  {
    path: RouterPaths.SUPPLIER_ADM_COMPANIES_ORDERS, name: 'Supplier Companies Orders',
    component: () => import('@/views/supplier-admin/SupplierCompaniesOrders.vue')
  },
  {
    path: RouterPaths.SUPPLIER_ADM_DASHBOARD, name: 'Supplier Dashboard',
    component: () => import('@/views/supplier-admin/SupplierDashboard.vue')
  },

  // system administrator
  {
    path: RouterPaths.SYSTEM_ADM_COMPANIES, name: 'System Admin Companies',
    component: () => import('@/views/system-admin/Companies.vue')
  },
  {
    path: RouterPaths.SYSTEM_ADM_SUPPLIERS, name: 'System Admin Suppliers',
    component: () => import('@/views/system-admin/Suppliers.vue')
  },
  {
    path: RouterPaths.SYSTEM_ADM_ORGANIZATIONS, name: 'System Admin Organizations',
    component: () => import('@/views/system-admin/Organizations.vue')
  },
  {
    path: RouterPaths.SYSTEM_ADM_USERS, name: 'System Admin Users',
    component: () => import('@/views/system-admin/Users.vue')
  }
]
const router: Router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach(async (to, from, next) => {
  await initializeStores()
  next()
})

async function initializeStores() {
  if (!useAuthStore().hasToken)
    useAuthStore().initializeToken()
  if (useAuthStore().hasToken)
    await useProfileStore().requestUserData()
  if (useProfileStore().isEmployee || useProfileStore().isSupplier) {
    await useOrganizationStore().requestFreshOrganizationData()
  }
}

export default router
