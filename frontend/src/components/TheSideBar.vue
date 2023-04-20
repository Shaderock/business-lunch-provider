<template>
  <!--todo configure router guards-->
  <v-navigation-drawer
    elevation="5"
    expand-on-hover
    floating
    permanent
    rail
  >
    <v-list density="compact" nav>
      <v-list-item
        prepend-icon="mdi-home"
        title="Home"
        v-bind:to="'/'"
      />

      <v-divider/>

      <div v-if="useUserStore().isOnlyAppUser">
        <v-list-item
          prepend-icon="mdi-email-plus"
          title="Companies Invitations"
          v-bind:to="RouterConstants.USER_COMPANY_INVITATIONS"
          value="companiesInvitations"
        />
      </div>

      <div v-if="useUserStore().isOnlyEmployee">
        <v-list-item
          prepend-icon="mdi-office-building"
          title="Organization Details"
          v-bind:to="RouterConstants.EMPLOYEE_OR_SUPPLIER_COMPANY_INVITATIONS"
          value="organizationDetails"
        />

        <v-divider/>

        <v-list-item disabled title="My Company Subscriptions"/>

        <v-list-item
          prepend-icon="mdi-briefcase-arrow-left-right"
          title="Supplier Subscriptions"
          v-bind:to="RouterConstants.EMPLOYEE_SUBSCRIBED_SUPPLIER"
          value="subscribedSuppliers"
        />

        <v-divider/>

        <v-list-item disabled title="My Orders"/>

        <v-list-item
          prepend-icon="mdi-shopping"
          title="Order History"
          v-bind:to="RouterConstants.EMPLOYEE_ORDER_HISTORY"
          value="employeeOrderHistory"
        />

        <v-list-item
          prepend-icon="mdi-store-clock"
          title="Pending orders"
          v-bind:to="RouterConstants.EMPLOYEE_PENDING_ORDERS"
          value="employeePendingOrders"
        />

        <v-list-item
          prepend-icon="mdi-view-dashboard-variant"
          title="Dashboard"
          v-bind:to="RouterConstants.EMPLOYEE_DASHBOARD"
          value="employeeDashboard"
        />
      </div>

      <div v-if="useUserStore().isCompanyAdmin">
        <v-list-item disabled title="Organization"/>

        <v-list-item
          prepend-icon="mdi-office-building"
          title="Organization Details"
          v-bind:to="RouterConstants.EMPLOYEE_OR_SUPPLIER_COMPANY_INVITATIONS"
          value="organizationDetails"
        />

        <v-list-item
          prepend-icon="mdi-office-building-cog"
          title="Company Preferences"
          v-bind:to="RouterConstants.COMPANY_ADM_COMPANY_PREFERENCES"
          value="companyPreferences"
        />

        <v-list-item
          prepend-icon="mdi-bell-cog"
          title="Company Notifications"
          v-bind:to="RouterConstants.COMPANY_ADM_COMPANY_NOTIFICATIONS"
          value="companyNotificationConfig"
        />

        <v-divider/>

        <v-list-item disabled title="Subscriptions"/>

        <v-list-item
          prepend-icon="mdi-briefcase-arrow-left-right"
          title="Supplier Subscriptions"
          v-bind:to="RouterConstants.EMPLOYEE_SUBSCRIBED_SUPPLIER"
          value="subscribedSuppliers"
        />

        <v-divider/>

        <v-list-item disabled title="My Orders"/>

        <v-list-item
          prepend-icon="mdi-shopping"
          title="Order History"
          v-bind:to="RouterConstants.EMPLOYEE_ORDER_HISTORY"
          value="employeeOrderHistory"
        />

        <v-list-item
          prepend-icon="mdi-store-clock"
          title="Pending orders"
          v-bind:to="RouterConstants.EMPLOYEE_PENDING_ORDERS"
          value="employeePendingOrders"
        />

        <v-list-item
          prepend-icon="mdi-view-dashboard-variant"
          title="Dashboard"
          v-bind:to="RouterConstants.EMPLOYEE_DASHBOARD"
          value="employeeDashboard"
        />

        <v-divider/>

        <v-list-item disabled title="Company Orders"/>

        <v-list-item
          prepend-icon="mdi-store"
          title="Company Orders"
          v-bind:to="RouterConstants.COMPANY_ADM_EMPLOYEES_ORDERS"
          value="companyOrders"
        />

        <v-list-item
          prepend-icon="mdi-view-dashboard-variant"
          title="Dashboard"
          v-bind:to="RouterConstants.COMPANY_ADM_COMPANY_DASHBOARD"
          value="companyDashboard"
        />

        <v-list-item disabled title="Employees"/>

        <v-list-item
          prepend-icon="mdi-account-check"
          title="Employees"
          v-bind:to="RouterConstants.COMPANY_ADM_EMPLOYEES"
          value="companyEmployees"
        />

        <v-list-item
          prepend-icon="mdi-account-clock"
          title="Invited users"
          v-bind:to="RouterConstants.COMPANY_ADM_INVITED_USERS"
          value="companyInvitedUsers"
        />
      </div>

      <div v-if="useUserStore().isSupplier">

        <v-list-item disabled title="Organization"></v-list-item>

        <v-list-item
          prepend-icon="mdi-office-building"
          title="Organization Details"
          v-bind:to="RouterConstants.EMPLOYEE_OR_SUPPLIER_COMPANY_INVITATIONS"
          value="organizationDetails"
        />

        <v-list-item
          prepend-icon="mdi-office-building-cog"
          title="Supplier Preferences"
          v-bind:to="RouterConstants.SUPPLIER_ADM_SUPPLIER_PREFERENCES"
          value="supplierPreferences"
        />

        <v-list-item disabled title="Subscriptions"></v-list-item>

        <v-list-item
          prepend-icon="mdi-briefcase-arrow-left-right"
          title="Companies Subscribers"
          v-bind:to="RouterConstants.SUPPLIER_ADM_SUBSCRIBED_COMPANIES"
          value="subscribedCompanies"
        />

        <v-divider/>

        <v-list-item disabled title="Menu"></v-list-item>

        <v-list-item
          prepend-icon="mdi-folder-multiple"
          title="Food Categories"
          v-bind:to="RouterConstants.SUPPLIER_ADM_FOOD_CATEGORIES"
          value="foodCategories"
        />

        <v-list-item
          prepend-icon="mdi-food"
          title="Food Options"
          v-bind:to="RouterConstants.SUPPLIER_ADM_FOOD_OPTIONS"
          value="foodOptions"
        />

        <v-divider/>

        <v-list-item disabled title="Orders"></v-list-item>

        <v-list-item
          prepend-icon="mdi-shopping"
          title="Companies Orders"
          v-bind:to="RouterConstants.SUPPLIER_ADM_COMPANIES_ORDERS"
          value="companiesOrdersRequests"
        />

        <v-list-item
          prepend-icon="mdi-view-dashboard-variant"
          title="Dashboard"
          v-bind:to="RouterConstants.SUPPLIER_ADM_DASHBOARD"
          value="supplierDashboard"
        />
      </div>

      <div v-if="useUserStore().isSysAdmin">
        <v-list-item
          prepend-icon="mdi-silverware"
          title="Companies"
          v-bind:to="'/sys-adm/companies'"
          value="companies">
        </v-list-item>

        <v-list-item
          prepend-icon="mdi-food"
          title="Suppliers"
          v-bind:to="'/sys-adm/suppliers'"
          value="suppliers">
        </v-list-item>

        <v-list-item
          prepend-icon="mdi-domain"
          title="Organizaitons"
          v-bind:to="'/sys-adm/organizations'"
          value="organizations">
        </v-list-item>

        <v-divider></v-divider>

        <v-list-item
          prepend-icon="mdi-account-multiple"
          title="Users"
          v-bind:to="'/sys-adm/users'"
          value="users">
        </v-list-item>
      </div>
    </v-list>
  </v-navigation-drawer>
</template>
<script lang="ts" setup>
import {useUserStore} from "@/store/app";
import {RouterConstants} from "@/services/RouterConstants";</script>
