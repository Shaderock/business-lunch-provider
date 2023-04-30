<template>
  <v-container>
    <v-row justify="center">
      <v-col>
        <v-data-table :class="`elevation-20`"
                      :headers="subscriptionHeaders"
                      :items="useEmployeeSubscriptionStore().getSubscriptionSuppliers"
                      density="compact">

          <template v-slot:top>
            <v-toolbar extended extension-height="1">
              <v-toolbar-title>Subscriptions</v-toolbar-title>

              <v-btn append-icon="mdi-store-plus" color="primary" variant="outlined"
                     @click="isDialogShown = true">
                Subscribe
              </v-btn>

              <template v-slot:extension>
                <v-progress-linear v-if="isLoading" indeterminate/>
              </template>

            </v-toolbar>
          </template>

          <template v-slot:item.actions="{ item }">
            <v-btn :disabled="isLoading" icon variant="plain">
              <v-icon color="error" icon="mdi-delete"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title>Are you sure you want to unsubscribe from {{ item.raw.name }}?
                  </v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="unsubscribe(item.raw)">
                      Unsubscribe
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>

  <v-dialog v-model="isDialogShown" closable="false">
    <v-row justify="center">
      <v-col lg="3" md="5" sm="8">
        <v-card title="Subscribe to a supplier">
          <v-card-text>
            <v-select v-model="selectedSupplierNames"
                      :items="useEmployeeSubscriptionStore().getValidSuppliersDetailsListNameForSubscription"
                      :loading="isSubscriptionInProgress"
                      :rules="[rules.required]"
                      chips
                      clearable
                      closable-chips
                      multiple
                      prepend-inner-icon="mdi-store"/>
            <v-card-actions>
              <v-btn :disabled="isSubscriptionInProgress || selectedSupplierNames.length === 0"
                     block color="primary"
                     variant="outlined"
                     @click="subscribe(selectedSupplierNames)">
                Subscribe
              </v-btn>
            </v-card-actions>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-dialog>
</template>

<script lang="ts" setup>

import {onBeforeMount, ref} from "vue";
import {SubscriptionSupplier, useEmployeeSubscriptionStore} from "@/store/company-adm-app";
import toastManager from "@/services/ToastManager";

onBeforeMount(() => {
  useEmployeeSubscriptionStore().requestFreshData().finally(() => isLoading.value = false)
})

const isLoading = ref(true)
const isDialogShown = ref(false)
const selectedSupplierNames = ref([])
const isSubscriptionInProgress = ref(false)
const rules = {
  required: (value: any) => !!value || 'Required field.',
};

const subscriptionHeaders = [
  {title: 'Name', key: 'name'},
  {title: 'Tel', key: 'phone'},
  {title: 'Email', key: 'email'},
  {title: 'Order type', key: 'orderType'},
  {title: "Menu", key: 'menuUrl'},
  {title: 'Minimum orders', key: 'minimumOrdersPerCompanyRequest'},
  {title: 'Minimum categories', key: 'minimumCategoriesForEmployeeOrder'},
  {title: 'Subscription Status', key: 'subscriptionStatus'},
  {title: 'Subscription Date', key: 'subscriptionDate'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

function unsubscribe(subscriptionSupplier: SubscriptionSupplier) {
  useEmployeeSubscriptionStore().unsubscribe(subscriptionSupplier.supplierId)
}

async function subscribe(names: string[]) {
  isSubscriptionInProgress.value = true
  await useEmployeeSubscriptionStore().subscribe(names)
  toastManager.showSuccess("Subscription sent", "A request for subscription is awaiting to be confirmed by supplier")
  await useEmployeeSubscriptionStore().requestFreshData()
  isSubscriptionInProgress.value = false
  selectedSupplierNames.value = []
}
</script>
