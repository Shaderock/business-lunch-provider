<template>
  <v-divider/>

  <v-list-item disabled title="My Orders"/>

  <v-list-item
    prepend-icon="mdi-shopping"
    title="Order History"
    v-bind:to="RouterPaths.EMPLOYEE_ORDER_HISTORY"
  />

  <v-list-item
    prepend-icon="mdi-store-clock"
    title="Pending orders"
    v-bind:to="RouterPaths.EMPLOYEE_PENDING_ORDERS"
  />

  <v-list-item
    prepend-icon="mdi-view-dashboard-variant"
    title="Dashboard"
    v-bind:to="RouterPaths.EMPLOYEE_DASHBOARD"
  />
</template>

<script lang="ts" setup>
import {RouterPaths} from "@/services/RouterPaths";
</script>
