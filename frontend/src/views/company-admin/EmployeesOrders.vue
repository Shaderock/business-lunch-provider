<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="6">
        <v-card class="overflow-auto"
                height="250"
                variant="tonal">
          <v-card-title class="text-error">
            Order validation
          </v-card-title>
          <v-card-text>
            <v-slide-x-transition>
              <div v-if="useCompAdmEmpOrderStore().getCompanyOrderValidation.valid"
                   class="d-flex align-center justify-center fill-height text-h2">
                Order is valid
                <v-icon color="success" icon="mdi-check"/>
              </div>
            </v-slide-x-transition>
            <v-slide-x-transition>
              <div v-for="error in useCompAdmEmpOrderStore().getCompanyOrderValidation.errors"
                   :key="error">

                <v-icon color="error" icon="mdi-alert"/>
                {{ error }}
                <v-divider thickness="4"/>
              </div>
            </v-slide-x-transition>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="6">
        <v-slide-x-transition>
          <v-card v-if="useCompAdmEmpOrderStore().getCompanyOrderValidation.valid"
                  class="overflow-auto"
                  height="250"
                  variant="tonal">
            <v-card-title class="text-primary">
              Summary
            </v-card-title>
            <v-card-subtitle>
              Orders
            </v-card-subtitle>
            <v-card-text>
              <v-chip color="info" label>
                {{ useCompAdmEmpOrderStore().getEmployeesOrdersRecords.length }}
              </v-chip>
            </v-card-text>
            <v-card-subtitle>
              Delivery
            </v-card-subtitle>
            <v-card-text>
              <v-chip color="info" label>
                {{ useCompAdmEmpOrderStore().getSelectedDateTime }}
              </v-chip>
            </v-card-text>
          </v-card>
        </v-slide-x-transition>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12">
        <v-data-table
          :headers="headers"
          :items="useCompAdmEmpOrderStore().getEmployeesOrdersRecords"
          class="elevation-20">
          <template v-slot:top>
            <v-toolbar extended>
              <v-form class="w-25 pl-3">
                <v-text-field
                  v-model="selectedDate"
                  :disabled="isLoading"
                  hide-details="auto"
                  label="Select order date"
                  prepend-inner-icon="mdi-calendar"
                  type="date"
                  @update:modelValue="updateSelectedDate()"/>
              </v-form>
              <v-divider vertical/>
              <v-form class="w-25 pl-3">
                <v-text-field
                  v-model="selectedTime"
                  :disabled="isLoading"
                  hide-details="auto"
                  label="Select order time"
                  prepend-inner-icon="mdi-clock"
                  type="time"
                  @update:modelValue="updateSelectedTime()"/>
              </v-form>
              <v-spacer/>
              <v-btn :disabled="isLoading"
                     append-icon="mdi-refresh"
                     color="secondary"
                     @click="onRefresh">Refresh
              </v-btn>
              <v-btn
                :disabled="isLoading || !useCompAdmEmpOrderStore().getCompanyOrderValidation.valid"
                append-icon="mdi-send-clock"
                color="primary"
                variant="outlined"
                @click="onOrderSend">Send order
              </v-btn>

              <template v-slot:extension>
                <v-tabs v-model="supplierTab" :disabled="isLoading">
                  <v-tab v-for="supplierDetails in useCompAdmEmpOrderStore().getSuppliersDetails"
                         :key="supplierDetails.id"
                         :value="supplierDetails"
                         color="primary"
                         @click="onSupplierTabChanged">
                    {{ supplierDetails.name }}
                  </v-tab>
                </v-tabs>
              </template>

              <template v-slot:prepend>
                <v-progress-circular v-if="isLoading" indeterminate/>
                <v-progress-circular v-else model-value="0"/>
              </template>
            </v-toolbar>
          </template>

          <template v-slot:item.isValid="{ item }">
            <v-btn v-if="!item.raw.isValid"
                   :disabled="isLoading"
                   icon
                   variant="plain">
              <v-icon color="error" icon="mdi-alert"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title class="text-error">Invalid order</v-card-title>
                  <v-card-text>
                    <v-list>
                      <v-list-item
                        v-for="error in useCompAdmEmpOrderStore().getOrderValidationByOrderId(item.raw.orderId)?.errors"
                        :key="error">

                        <v-list-item-title>
                          <v-icon color="error" icon="mdi-alert"/>
                          {{ error }}
                        </v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-card-text>
                </v-card>
              </v-menu>
            </v-btn>
          </template>

          <template v-slot:item.actions="{ item }">
            <v-btn :disabled="isLoading"
                   icon
                   variant="plain">
              <v-icon color="secondary" icon="mdi-open-in-new"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title class="text-primary">Ordered Options</v-card-title>
                  <v-card-text>
                    <v-list>
                      <div v-for="option in useCompAdmEmpOrderStore().getOptionsOfOrder(item.raw)"
                           :key="option.id">
                        <v-list-item :title="option.name" prepend-icon="mdi-food"/>
                        <v-divider inset/>
                      </div>
                    </v-list>
                  </v-card-text>
                </v-card>
              </v-menu>
            </v-btn>

            <v-btn :disabled="isLoading"
                   icon
                   variant="plain">
              <v-icon color="error" icon="mdi-delete"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title>Are you sure to remove this order?
                  </v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="error"
                           prepend-icon="mdi-delete"
                           @click="removeOrder(item.raw)">
                      Remove order
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
import {EmployeesOrdersTableRecord, useCompAdmEmpOrderStore} from "@/store/company-adm-app";

onMounted(() => {
  useCompAdmEmpOrderStore().requestFreshDataIfEmpty().finally(() => {
    selectedDate.value = useCompAdmEmpOrderStore().getSelectedDate
    selectedTime.value = useCompAdmEmpOrderStore().getSelectedTime
    useCompAdmEmpOrderStore().requestUpdateOrdersForSelectedDateTime().finally(() => {
      supplierTab.value = useCompAdmEmpOrderStore().getSelectedSupplierDetails
      isLoading.value = false
    })
  })
})

const isLoading = ref(true)
const selectedDate = ref()
const selectedTime = ref()
const supplierTab = ref()

const headers = [
  {title: 'Email', key: 'email'},
  {title: 'Sup price', key: 'supplierDefaultPrice'},
  {title: 'Sup discount', key: 'supplierDiscount'},
  {title: 'Com discount', key: 'companyDiscount'},
  {title: 'Final price', key: 'finalPrice'},
  {title: 'Options', key: 'options'},
  {title: 'Valid', key: 'isValid'},
  {title: 'Status', key: 'status'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

async function updateSelectedDate() {
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().setSelectedDate(selectedDate.value)
  } finally {
    supplierTab.value = useCompAdmEmpOrderStore().getSelectedSupplierDetails
    isLoading.value = false
  }
}

async function updateSelectedTime() {
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().setSelectedTime(selectedTime.value)
  } finally {
    isLoading.value = false
  }
}

async function removeOrder(employeeOrder: EmployeesOrdersTableRecord) {
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().deleteOrder(employeeOrder)
  } finally {
    isLoading.value = false
  }
}

async function onOrderSend() {
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().sendOrder()
  } finally {
    isLoading.value = false
  }
}

async function onRefresh() {
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().requestUpdateOrdersForSelectedDateTime()
  } finally {
    isLoading.value = false
  }
}

async function onSupplierTabChanged() {
  console.log()
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().setSelectedSupplierDetails(supplierTab.value)
  } finally {
    isLoading.value = false
  }
}
</script>
