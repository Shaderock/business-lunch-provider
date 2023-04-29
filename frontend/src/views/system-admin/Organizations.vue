<template>
  <v-data-table
    :headers="headers"
    :items="organizationsDetails"
    :search="search" class="elevation-1"
    const
    item-value="name"
  >
    <template v-slot:top>
      <v-card variant="tonal">
        <v-card-title>Organizations</v-card-title>
        <v-card-actions>
          <v-col cols="3">
            <v-text-field
              v-model="search"
              label="Search"
            ></v-text-field>
            <v-btn color="primary" variant="tonal">New Item</v-btn>
          </v-col>
        </v-card-actions>
      </v-card>

    </template>
  </v-data-table>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import {useSysAdmOrganizationStore} from "@/store/sys-adm-app";


const headers = [
  {title: 'id', sortable: true, key: 'id'},
  {title: 'name', sortable: true, key: 'name'},
  {title: 'description', sortable: true, key: 'description'},
  {title: 'email', sortable: true, key: 'email'},
  {title: 'phone', sortable: true, key: 'phone'}
]

const search = ref()
const organizationsDetails = computed(() => useSysAdmOrganizationStore().getOrganizationsDetails)

onMounted(() => {
  useSysAdmOrganizationStore().requestFreshOrganizationsData()
})
</script>
