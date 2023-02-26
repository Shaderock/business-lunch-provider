<template>
  <v-data-table
    :headers="headers"
    :items="useSysAdmSupplierStore().getSuppliers"
    class="elevation-1" const
    item-value="name"
    search=ref()
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
import {onMounted, ref} from "vue";
import {useSysAdmSupplierStore} from "@/store/app";

const headers = [
  {title: 'id', sortable: true, key: 'id'},
  {title: 'name', sortable: true, key: 'name'},
  {title: 'description', sortable: true, key: 'description'},
  {title: 'email', sortable: true, key: 'email'},
  {title: 'phone', sortable: true, key: 'phone'},
  {title: 'websiteUrl', sortable: false, key: 'websiteUrl'},
  {title: 'menuUrl', sortable: false, key: 'menuUrl'},
  {title: 'preferencesId', sortable: false, key: 'preferencesId'},
  {title: 'menuId', sortable: false, key: 'menuId'},
]

const search = ref()

onMounted(() => {
  useSysAdmSupplierStore().requestFreshSuppliersData()
})
</script>
