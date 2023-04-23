<template>
  <v-row justify="center">
    <v-col cols="12">
      <!--todo add search-->
      <v-data-table
        :class="`elevation-20`"
        :headers="employeesHeaders"
        :items="employees">
        <template v-slot:top>
          <v-toolbar extended extension-height="1">
            <v-toolbar-title>Employees</v-toolbar-title>

            <template v-slot:extension>
              <v-progress-linear v-if="loading" indeterminate/>
            </template>
          </v-toolbar>
        </template>

        <template v-slot:item.isAdmin="{ item }">
          <v-checkbox-btn v-model="item.raw.isAdmin" disabled/>
        </template>

        <template v-slot:item.actions="{ item }">
          <v-tooltip v-if="!item.raw.isAdmin" location="left">
            <template v-slot:activator="{ props }">
              <v-btn
                color="secondary"
                icon
                size="small"
                v-bind="props"
                variant="plain"
                @click="grantAdmin(item.raw)">
                <v-icon icon="mdi-police-badge"/>
              </v-btn>
            </template>
            Grant admin rights
          </v-tooltip>

          <v-tooltip
            v-if="item.raw.isAdmin && useProfileStore().getUserDetails?.email !== item.raw.email"
            location="left">
            <template v-slot:activator="{ props }">
              <v-btn color="secondary"
                     icon
                     size="small"
                     v-bind="props"
                     variant="plain"
                     @click="revokeAdmin(item.raw)">
                <v-icon icon="mdi-account-tie-outline"/>
              </v-btn>
            </template>
            Revoke admin rights
          </v-tooltip>

          <v-btn v-if="useProfileStore().getUserDetails?.email !== item.raw.email"
                 icon
                 size="small"
                 variant="plain">
            <v-icon color="error" icon="mdi-delete"/>
            <v-menu activator="parent">
              <v-card>
                <v-card-title>Are you sure to delete {{ item.raw.email }}?</v-card-title>
                <v-card-actions>
                  <v-btn block
                         color="secondary"
                         @click="deleteEmployee(item.raw)">
                    Delete
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-menu>
          </v-btn>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

<script lang="ts" setup>

import {computed, ComputedRef, onMounted, ref} from "vue";
import {Employee, useCompAdmUserStore} from "@/store/company-adm-app";
import {useProfileStore} from "@/store/user-app";

onMounted(() => {
  useCompAdmUserStore().requestFreshEmployeesData().finally(() => loading.value = false)
})

const loading = ref(true)
const employees: ComputedRef<Employee[]> = computed(() => useCompAdmUserStore().getEmployees)
const employeesHeaders = [
  {title: 'Email', key: 'email'},
  {title: 'First Name', key: 'firstName'},
  {title: 'Last Name', key: 'lastName'},
  {title: 'Administrator', key: 'isAdmin'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

function deleteEmployee(employee: Employee) {
  useCompAdmUserStore().removeEmployeeByEmail(employee.email)
}

function revokeAdmin(employee: Employee) {
  useCompAdmUserStore().revokeAdminRights(employee.email)
}

function grantAdmin(employee: Employee) {
  useCompAdmUserStore().grantAdminRights(employee.email)
}
</script>
