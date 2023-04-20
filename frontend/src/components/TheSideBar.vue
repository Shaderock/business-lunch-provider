<template>
  <v-navigation-drawer
    expand-on-hover
    floating
    permanent
    rail
    elevation="5"
  >
    <v-list
      density="compact"
      nav
    >
      <v-list-item
        prepend-icon="mdi-home"
        color="primary"
        title="Home"
        v-bind:to="'/'"
        variant="plain"
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

      <div v-if="useUserStore().isSysAdmin">
        <v-divider/>

        <v-list-item
          prepend-icon="mdi-silverware"
          title="Companies"
          value="companies"
          v-bind:to="'/sys-adm/companies'">
        </v-list-item>
        <v-list-item
          prepend-icon="mdi-food"
          title="Suppliers"
          value="suppliers"
          v-bind:to="'/sys-adm/suppliers'">
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
          value="users"
          v-bind:to="'/sys-adm/users'">
        </v-list-item>
      </div>
    </v-list>
  </v-navigation-drawer>
</template>
<script lang="ts" setup>
import {useAuthStore, useUserStore} from "@/store/app";</script>
