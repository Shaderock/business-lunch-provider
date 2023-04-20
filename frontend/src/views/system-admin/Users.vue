<template>
  <v-data-table
    :headers="headers"
    :items="useSysAdmUserStore().getUsersDetails"
    :search="search"
    class="elevation-1"
    item-value="name"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Users</v-toolbar-title>
      </v-toolbar>
      <v-text-field
        v-model="search"
        label="Search"
        class="pa-4"
      ></v-text-field>
    </template>
  </v-data-table>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useSysAdmUserStore} from "@/store/app";

const headers = [
  {title: 'id', sortable: true, key: 'id'},
  {title: 'appUserId', sortable: true, key: 'appUserId'},
  {title: 'email', sortable: true, key: 'email'},
  {title: 'firstName', sortable: true, key: 'firstName'},
  {title: 'lastName', sortable: true, key: 'lastName'},
  {title: 'roles', sortable: false, key: 'roles'},
]

const search = ref()

onMounted(() => {
  useSysAdmUserStore().requestFreshUsersData()
})
</script>
