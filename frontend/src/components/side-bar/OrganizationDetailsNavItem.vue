<template>
  <v-list-item
    prepend-icon="mdi-office-building"
    title="Organization Details"
    v-bind:to="RouterPaths.EMPLOYEE_OR_SUPPLIER_ORGANIZATION_DETAILS"
  />
</template>

<script lang="ts" setup>
import {RouterPaths} from "@/services/RouterPaths";
</script>

