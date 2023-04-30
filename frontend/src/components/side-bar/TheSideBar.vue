<template>
  <v-hover>
    <template v-slot:default="{ isHovering, props}">
      <v-navigation-drawer
        expand-on-hover
        permanent
        rail
        v-bind="props"
      >
        <v-list density="compact" nav>
          <v-list-item
            prepend-icon="mdi-home"
            title="Home"
            v-bind:to="RouterPaths.ANONYMOUS_HOME"
          />

          <v-divider/>

          <div v-if="useProfileStore().isOnlyAppUser">

            <v-list-item :hidden="!isHovering" disabled title="Invitations"/>

            <v-list-item
              prepend-icon="mdi-email-plus"
              title="Companies Invitations"
              v-bind:to="RouterPaths.USER_COMPANY_INVITATIONS"
            />
          </div>

          <div v-if="useProfileStore().isUserAndEmployee">
            <v-list-item :hidden="!isHovering" disabled title="Organization"/>

            <OrganizationDetailsNavItem/>

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="My Orders"/>

            <EmployeeOrdersNavItems/>
          </div>

          <div v-if="useProfileStore().isCompanyAdmin">
            <v-list-item :hidden="!isHovering" disabled title="Organization"/>

            <OrganizationDetailsNavItem/>

            <v-list-item
              prepend-icon="mdi-office-building-cog"
              title="Company Preferences"
              v-bind:to="RouterPaths.COMPANY_ADM_COMPANY_PREFERENCES"
            />

            <v-list-item
              prepend-icon="mdi-bell-cog"
              title="Company Notifications"
              v-bind:to="RouterPaths.COMPANY_ADM_COMPANY_NOTIFICATIONS"
            />

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="Subscriptions"/>

            <SupplierSubscriptionsNavItem/>

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="My Orders"/>

            <EmployeeOrdersNavItems/>

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="Company Orders"/>

            <v-list-item
              prepend-icon="mdi-store-plus"
              title="Employees Orders"
              v-bind:to="RouterPaths.COMPANY_ADM_EMPLOYEES_ORDERS"
            />

            <v-list-item
              prepend-icon="mdi-store-clock"
              title="Company Orders"
              v-bind:to="RouterPaths.COMPANY_ADM_COMPANY_ORDERS"
            />

            <v-list-item
              prepend-icon="mdi-view-dashboard-variant"
              title="Dashboard"
              v-bind:to="RouterPaths.COMPANY_ADM_COMPANY_DASHBOARD"
            />

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="Employees"/>

            <v-list-item
              prepend-icon="mdi-account-check"
              title="Employees"
              v-bind:to="RouterPaths.COMPANY_ADM_EMPLOYEES"
            />

            <v-list-item
              prepend-icon="mdi-account-clock"
              title="Users Invitations"
              v-bind:to="RouterPaths.COMPANY_ADM_INVITED_USERS"
            />
          </div>

          <div v-if="useProfileStore().isSupplier">

            <v-list-item :hidden="!isHovering" disabled title="Organization"></v-list-item>

            <OrganizationDetailsNavItem/>

            <v-list-item
              prepend-icon="mdi-office-building-cog"
              title="Supplier Preferences"
              v-bind:to="RouterPaths.SUPPLIER_ADM_SUPPLIER_PREFERENCES"
            />

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="Subscriptions"/>

            <v-list-item
              prepend-icon="mdi-briefcase-arrow-left-right"
              title="Companies Subscribers"
              v-bind:to="RouterPaths.SUPPLIER_ADM_SUBSCRIBED_COMPANIES"
            />

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="Menu"></v-list-item>

            <v-list-item
              prepend-icon="mdi-folder-multiple"
              title="Food Categories"
              v-bind:to="RouterPaths.SUPPLIER_ADM_FOOD_CATEGORIES"
            />

            <v-list-item
              prepend-icon="mdi-food"
              title="Food Options"
              v-bind:to="RouterPaths.SUPPLIER_ADM_FOOD_OPTIONS"
            />

            <v-divider/>

            <v-list-item :hidden="!isHovering" disabled title="Orders"></v-list-item>

            <v-list-item
              prepend-icon="mdi-shopping"
              title="Companies Orders"
              v-bind:to="RouterPaths.SUPPLIER_ADM_COMPANIES_ORDERS"
            />

            <v-list-item
              prepend-icon="mdi-view-dashboard-variant"
              title="Dashboard"
              v-bind:to="RouterPaths.SUPPLIER_ADM_DASHBOARD"
            />
          </div>

          <div v-if="useProfileStore().isSysAdmin">
            <v-list-item
              prepend-icon="mdi-silverware"
              title="Companies"
              v-bind:to="RouterPaths.SYSTEM_ADM_COMPANIES"/>

            <v-list-item
              prepend-icon="mdi-food"
              title="Suppliers"
              v-bind:to="RouterPaths.SYSTEM_ADM_SUPPLIERS"/>

            <v-list-item
              prepend-icon="mdi-domain"
              title="Organizaitons"
              v-bind:to="RouterPaths.SYSTEM_ADM_ORGANIZATIONS"/>

            <v-divider/>

            <v-list-item
              prepend-icon="mdi-account-multiple"
              title="Users"
              v-bind:to="RouterPaths.SYSTEM_ADM_USERS"/>
          </div>
        </v-list>
      </v-navigation-drawer>
    </template>
  </v-hover>
</template>
<script lang="ts" setup>
import {useProfileStore} from "@/store/user-app";
import {RouterPaths} from "@/services/RouterPaths";
import OrganizationDetailsNavItem from "@/components/side-bar/OrganizationDetailsNavItem.vue";
import SupplierSubscriptionsNavItem from "@/components/side-bar/SupplierSubscriptionsNavItem.vue";
import EmployeeOrdersNavItems from "@/components/side-bar/EmployeeOrdersNavItems.vue";
import {VNavigationDrawer} from "vuetify/components";
</script>
