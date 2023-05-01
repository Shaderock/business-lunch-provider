<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12">
        <v-data-table
          :headers="headers"
          :items="isLoading ? [] : useSupplierAdmOrdersStore().getCompaniesOrdersRecords"
          :search="search"
          class="elevation-20">

          <template v-slot:top>
            <v-toolbar extended>
              <v-form class="pl-3">
                <v-text-field
                  v-model="selectedDate"
                  :disabled="isLoading"
                  class="w-100"
                  hide-details="auto"
                  label="Select order date"
                  prepend-inner-icon="mdi-calendar"
                  type="date"
                  @update:modelValue="updateSelectedDate()"/>
              </v-form>
              <v-divider vertical/>
              <v-form class="w-25 pl-3">
                <v-text-field
                  v-model="search"
                  hide-details
                  label="Search"
                  prepend-inner-icon="mdi-magnify"
                  single-line/>
              </v-form>
              <v-spacer/>

              <v-btn :disabled="isLoading"
                     class="mr-2"
                     color="secondary"
                     variant="outlined"
                     @click="onRefresh">
                <v-icon icon="mdi-refresh"/>
              </v-btn>

              <template v-slot:extension>
                <v-progress-linear v-if="isLoading" indeterminate/>
              </template>
            </v-toolbar>
          </template>

          <template v-slot:item.status="{ item }">
            <v-icon v-if="item.raw.status === EmployeeOrderStatus.DeclinedBySupplier"
                    color="error" icon="mdi-close"/>
            <v-icon v-else-if="item.raw.status === EmployeeOrderStatus.ConfirmedBySupplier"
                    color="success"
                    icon="mdi-check"/>
            <v-icon v-else-if="item.raw.status === EmployeeOrderStatus.PendingSupplierConfirmation"
                    icon="mdi-store-clock"/>
            <v-icon v-else-if="item.raw.status === EmployeeOrderStatus.PendingAdminConfirmation"
                    icon="mdi-clipboard-text-clock"/>
            {{ item.raw.status }}
          </template>

          <template v-slot:item.actions="{ item }">
            <v-btn :disabled="isLoading"
                   icon
                   variant="plain"
                   @click="onDialogOpen(item.raw)">
              <v-icon icon="mdi-open-in-app"/>
              <v-menu activator="parent">
                <v-card :title="dialogCompanyOrderExtended.subscriberCompany.name" variant="tonal">
                  <v-card-text>
                    <v-list>
                      <div
                        v-for="(extendedOption, index) in dialogCompanyOrderExtended.orderedOptionsExtended"
                        :key="index">
                        <v-divider v-if="index !== 0" thickness="4"/>
                        <v-list-item prepend-icon="mdi-food">
                          <v-list-item-title class="mr-10">
                            {{ extendedOption.option.name }}
                          </v-list-item-title>
                          <template v-slot:append>
                            <v-chip label>
                              {{ extendedOption.amount }}
                            </v-chip>
                          </template>
                        </v-list-item>
                      </div>
                    </v-list>
                  </v-card-text>
                </v-card>
              </v-menu>
            </v-btn>
            <v-btn v-if="item.raw.status === CompanyOrderStatus.PendingSupplierConfirmation"
                   :disabled="isLoading"
                   icon
                   variant="plain">
              <v-icon color="success" icon="mdi-check"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title>Are you sure to confirm this order?
                  </v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="success"
                           prepend-icon="mdi-check"
                           @click="confirmOrder(item.raw)">
                      Confirm order
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>
            <v-btn v-if="item.raw.status === CompanyOrderStatus.PendingSupplierConfirmation"
                   :disabled="isLoading"
                   icon
                   variant="plain">
              <v-icon color="error" icon="mdi-close"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title>Are you sure to decline this order?
                  </v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="error"
                           prepend-icon="mdi-close"
                           @click="declineOrder(item.raw)">
                      Decline order
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
import {onMounted, ref} from "vue";
import {CompanyOrderStatus} from "@/models/CompanyOrderStatus";
import {CompanyOrderRecord, useSupplierAdmOrdersStore} from "@/store/supplier-adm-app";
import {EmployeeOrderStatus} from "@/models/EmployeeOrderStatus";

onMounted(() => {
  useSupplierAdmOrdersStore().requestFreshDataIfEmpty().finally(() => {
    selectedDate.value = useSupplierAdmOrdersStore().getSelectedDate
    useSupplierAdmOrdersStore().requestUpdateOrdersForSelectedDate().finally(() => {
      isLoading.value = false
    })
  })
})

const isLoading = ref(true)
const selectedDate = ref()
const search = ref()
const dialogCompanyOrderExtended = ref()

const headers = [
  {title: 'Company', key: 'companyName'},
  {title: 'Email', key: 'email'},
  {title: 'Phone', key: 'phone'},
  {title: 'Initial price', key: 'defaultSupplierPriceSum'},
  {title: 'Discount', key: 'supplierDiscountSum'},
  {title: 'Income', key: 'income'},
  {title: 'Status', key: 'status', align: 'center'},
  {title: 'Deliver time', key: 'deliverAt', align: 'center'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

async function updateSelectedDate() {
  try {
    isLoading.value = true
    await useSupplierAdmOrdersStore().setSelectedDate(selectedDate.value)
  } finally {
    isLoading.value = false
  }
}

async function confirmOrder(record: CompanyOrderRecord) {
  try {
    isLoading.value = true
    await useSupplierAdmOrdersStore().confirmOrder(record)
  } finally {
    isLoading.value = false
  }
}

async function declineOrder(record: CompanyOrderRecord) {
  try {
    isLoading.value = true
    await useSupplierAdmOrdersStore().declineOrder(record)
  } finally {
    isLoading.value = false
  }
}

async function onRefresh() {
  try {
    isLoading.value = true
    await useSupplierAdmOrdersStore().requestUpdateOrdersForSelectedDate()
  } finally {
    isLoading.value = false
  }
}

async function onDialogOpen(record: CompanyOrderRecord) {
  dialogCompanyOrderExtended.value = useSupplierAdmOrdersStore().getCompanyOrderExtended(record)
}
</script>
