<template>
  <v-container>
    <v-row justify="center">
      <v-col>
        <v-data-table :class="`elevation-20`"
                      :headers="subscribersHeaders"
                      :items="useSubscribersCompaniesStore().getSubscribersCompanies"
                      density="compact">
          <template v-slot:top>
            <v-toolbar extended extension-height="1">
              <v-toolbar-title>Subscribers</v-toolbar-title>

              <template v-slot:extension>
                <v-progress-linear v-if="isLoading" indeterminate/>
              </template>

            </v-toolbar>
          </template>

          <template v-slot:item.actions="{ item }">
            <v-btn v-if="item.raw.subscriptionStatus == SubscriptionStatus.Pending"
                   :disabled="isLoading" icon
                   variant="plain" @click="acceptSubscription(item.raw)">
              <v-icon color="success" icon="mdi-check"/>
            </v-btn>

            <v-btn :disabled="isLoading" icon variant="plain">
              <v-icon color="error" icon="mdi-delete"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to decline subscription of {{
                      item.raw.name
                    }}?
                  </v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="declineSubscription(item.raw)">
                      Dismiss subscription
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
</template>

<script lang="ts" setup>

import {onBeforeMount, ref} from "vue";
import {SubscriberCompany, useSubscribersCompaniesStore} from "@/store/supplier-adm-app";
import {SubscriptionStatus} from "@/models/SubscriptionStatus";

onBeforeMount(() => {
  useSubscribersCompaniesStore().requestFreshData().finally(() => isLoading.value = false)
})

const isLoading = ref(true)

const subscribersHeaders = [
  {title: 'Name', key: 'name'},
  {title: 'Tel', key: 'phone'},
  {title: 'Email', key: 'email'},
  {title: 'DeliveryDate', key: 'deliveryAddress'},
  {title: 'Subscription Status', key: 'subscriptionStatus'},
  {title: 'Subscription Date', key: 'subscriptionDate'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

async function declineSubscription(subscriberCompany: SubscriberCompany) {
  isLoading.value = true
  await useSubscribersCompaniesStore().declineSubscription(subscriberCompany.companyId)
  isLoading.value = false
}

async function acceptSubscription(subscriberCompany: SubscriberCompany) {
  isLoading.value = true
  await useSubscribersCompaniesStore().acceptSubscription(subscriberCompany.companyId)
  isLoading.value = false
}
</script>
