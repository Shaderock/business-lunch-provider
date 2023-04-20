<template>
  <v-divider/>

  <v-list-item disabled title="Subscriptions"/>

  <v-list-item
    prepend-icon="mdi-briefcase-arrow-left-right"
    title="Supplier Subscriptions"
    v-bind:to="RouterPaths.EMPLOYEE_COMPANY_SUPPLIERS_SUBSCRIPTIONS"
  />
</template>

<script lang="ts" setup>
import {RouterPaths} from "@/services/RouterPaths";
</script>

