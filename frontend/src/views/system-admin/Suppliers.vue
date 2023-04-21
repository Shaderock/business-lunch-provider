<template>
  <v-data-table
    :headers="headers"
    :items="suppliers"
    :search="search" class="elevation-1"
    const
    item-value="name"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Suppliers</v-toolbar-title>
      </v-toolbar>
      <v-text-field
        v-model="search"
        class="pa-4"
        label="Search"
      ></v-text-field>
    </template>
  </v-data-table>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import {useSysAdmOrganizationStore} from "@/store/sys-adm-app";


const headers = [
  {title: 'id', sortable: true, key: 'id'},
  {title: 'websiteUrl', sortable: false, key: 'websiteUrl'},
  {title: 'menuUrl', sortable: false, key: 'menuUrl'},
  {title: 'preferencesId', sortable: false, key: 'preferencesId'},
  {title: 'menuId', sortable: false, key: 'menuId'},
]

const search = ref()
const suppliers = computed(() => useSysAdmOrganizationStore().getSuppliers)

onMounted(() => {
  useSysAdmOrganizationStore().requestFreshSuppliersData()
})
</script>
