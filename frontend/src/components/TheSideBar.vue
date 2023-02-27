<template>
  <v-navigation-drawer
    expand-on-hover
    floating
    permanent
    rail
  >
    <v-list
      density="compact"
      nav
    >
      <v-list-item
        prepend-icon="mdi-home"
        title="Home" v-bind:to="'/'"
        value="home"
      />

      <v-divider v-if="useAuthStore().isAuthenticated"/>

      <v-list-item
        v-if="useUserStore().isEmployee"
        prepend-icon="mdi-domain"
        title="My Company"
        v-bind:to="'/company'"
        value="myCompany"
      />

      <v-list-item
        v-if="useUserStore().isSupplier"
        prepend-icon="mdi-domain"
        title="My Company"
        v-bind:to="'/supplier'"
        value="mySupplierOrganization"
      />

      <v-divider v-if="useAuthStore().isAuthenticated"/>

      <v-list-item
        v-if="useUserStore().isOnlyAppUser"
        prepend-icon="mdi-silverware"
        title="Register Company"
        v-bind:to="'/company/registration'"
        value="registerCompany"
      ></v-list-item>
      <v-list-item
        v-if="useUserStore().isOnlyAppUser"
        prepend-icon="mdi-food"
        title="Register supplier"
        v-bind:to="'/supplier/registration'"
        value="registerSupplier"
      ></v-list-item>

      <div v-if="useUserStore().isSysAdmin">
        <v-divider/>

        <v-list-item
          prepend-icon="mdi-silverware"
          title="Companies"
          value="companies"
          v-bind:to="'/sysadm/companies'">
        </v-list-item>
        <v-list-item
          prepend-icon="mdi-food"
          title="Suppliers"
          value="suppliers"
          v-bind:to="'/sysadm/suppliers'">
        </v-list-item>
        <v-list-item
          prepend-icon="mdi-domain"
          title="Organizaitons"
          v-bind:to="'/sysadm/organizations'"
          value="organizations">
        </v-list-item>
        <v-divider></v-divider>

        <v-list-item
          prepend-icon="mdi-account-multiple"
          title="Users"
          value="users"
          v-bind:to="'/sysadm/users'">
        </v-list-item>
      </div>
    </v-list>
  </v-navigation-drawer>
</template>
<script lang="ts" setup>
import {useAuthStore, useUserStore} from "@/store/app";</script>
