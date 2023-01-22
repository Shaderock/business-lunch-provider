<template>
  <v-navigation-drawer
    floating
    permanent
  >
    <v-list
      density="compact"
      nav
    >
      <v-list-item
        append-icon="mdi-call-split"
        title="Navigation"
        type="subheader"
        value="navigation"
      />

      <v-divider/>

      <v-list-item
        prepend-icon="mdi-home"
        title="Home" v-bind:to="'/'"
        value="home"
      />
      <v-list-item v-if="useAuthStore().isAuthenticated"
                   append-icon="mdi-cogs"
                   title="Actions"
                   type="subheader"
                   value="actions"
      />

      <v-divider v-if="useAuthStore().isAuthenticated"/>

      <v-list-item
        v-if="useUserStore().isEmployee"
        prepend-icon="mdi-domain"
        title="My Company"
        v-bind:to="'/company'"
        value="myCompany"
      />

      <v-list-group
        v-if="useUserStore().isOnlyAppUser"
        value="organizationRegistration"
      >
        <template v-slot:activator="{ props }">
          <v-list-item
            prepend-icon="mdi-domain-plus"
            title="Add Organization"
            v-bind="props"
          />
        </template>

        <v-list-item
          prepend-icon="mdi-silverware"
          title="Register Company"
          v-bind:to="'/company/registration'"
          value="registerCompany"
        ></v-list-item>
        <v-list-item
          prepend-icon="mdi-food"
          title="Register supplier"
          value="registerSupplier"
        ></v-list-item>
      </v-list-group>

    </v-list>
  </v-navigation-drawer>
</template>
<script lang="ts" setup>
import {useAuthStore, useUserStore} from "@/store/app";</script>
