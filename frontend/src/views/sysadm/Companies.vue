<template>
  <v-data-table
    :headers="headers"
    :items="useSysAdmCompanyStore().getCompanies"
    :search="search"
    class="elevation-1"
    item-value="name">
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Companies</v-toolbar-title>
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
import {useSysAdmCompanyStore} from "@/store/app";

const headers = [
  {title: 'id', sortable: true, key: 'id'},
  {title: 'name', sortable: true, key: 'name'},
  {title: 'description', sortable: true, key: 'description'},
  {title: 'email', sortable: true, key: 'email'},
  {title: 'phone', sortable: true, key: 'phone'},
  {title: 'preferencesId', sortable: false, key: 'preferencesId'},
]

const search = ref()

onMounted(() => {
  useSysAdmCompanyStore().requestFreshCompaniesData()
})
</script>
