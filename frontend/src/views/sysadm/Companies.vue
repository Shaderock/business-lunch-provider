<template>
  <v-data-table
    :headers="headers"
    :items="companyOrganizationDetails"
    :search="search"
    class="elevation-1"
    item-value="name">
    <template v-slot:top>
      <v-card>
        <v-card-title>Companies</v-card-title>
        <v-card-actions>
          <v-col cols="3">
            <v-text-field
              v-model="search"
              label="Search"
            ></v-text-field>
            <v-dialog
              v-model="isCreateDialogShown"
            >
              <template v-slot:activator="{props}">
                <v-btn color="primary" v-bind="props" variant="tonal">New Item</v-btn>
              </template>

              <v-card>
                <v-card-title>Create New Company</v-card-title>
                <v-card-item>
                  <v-text-field
                    v-model="search"
                    label="Search"
                  ></v-text-field>
                </v-card-item>
              </v-card>
            </v-dialog>
          </v-col>
        </v-card-actions>
      </v-card>

    </template>
  </v-data-table>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import {useSysAdmOrganizationStore} from "@/store/app";
import {Company} from "@/models/Company";
import {OrganizationDetails} from "@/models/OrganizationDetails";

const headers = [
  {title: 'id', sortable: true, key: 'id'},
  {title: 'name', sortable: true, key: 'name'},
  {title: 'description', sortable: true, key: 'description'},
  {title: 'email', sortable: true, key: 'email'},
  {title: 'phone', sortable: true, key: 'phone'},
  {title: 'preferencesId', sortable: false, key: 'preferencesId'},
  {title: 'Actions', sortable: false, key: 'actions'}
]

// datatable
const search = ref()
const companies = computed(() => useSysAdmOrganizationStore().getCompanies)
const organizationDetails = computed(() => useSysAdmOrganizationStore().getOrganizationsDetails)
const companyOrganizationDetails = computed(() => {
  const companyIds: string[] = companies.value.map((company: Company) => company.organizationDetailsId);
  return organizationDetails.value.filter((org: OrganizationDetails) => companyIds.includes(org.id));
})

// crud-dialogs
const isCreateDialogShown = ref(false)

function showCreateDialog() {
  isCreateDialogShown.value = true;
}


onMounted(() => {
  useSysAdmOrganizationStore().requestFreshCompaniesData()
})
</script>
