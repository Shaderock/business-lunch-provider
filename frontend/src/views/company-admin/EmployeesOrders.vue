<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="6">
        <v-card class="overflow-auto"
                height="270"
                variant="tonal">
          <v-card-title
            :class="useCompAdmEmpOrderStore().getCompanyOrderValidation.valid ? 'text-success' : 'text-error'">
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
              <div>
                <div v-for="error in useCompAdmEmpOrderStore().getCompanyOrderValidation.errors"
                     :key="error">

                  <v-icon color="error" icon="mdi-alert"/>
                  {{ error }}
                  <v-divider thickness="4"/>
                </div>
              </div>
            </v-slide-x-transition>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="6">
        <v-slide-x-transition>
          <v-card v-if="useCompAdmEmpOrderStore().getCompanyOrderValidation.valid"
                  class="overflow-auto"
                  height="270"
                  variant="tonal">
            <v-card-title class="text-primary">
              Summary
            </v-card-title>
            <v-card-subtitle>
              Orders
            </v-card-subtitle>
            <v-card-text>
              <v-chip color="info" label>
                {{
                  useCompAdmEmpOrderStore().getEmployeesOrdersRecords
                  .filter(r => r.status === EmployeeOrderStatus.PendingAdminConfirmation).length
                }}
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
            <v-card-actions>
              <v-btn
                :disabled="isLoading || !useCompAdmEmpOrderStore().getCompanyOrderValidation.valid"
                append-icon="mdi-send-clock"
                block
                color="primary"
                variant="outlined"
                @click="onOrderSend">request lunch
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-slide-x-transition>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12">
        <v-data-table
          :headers="headers"
          :items="isLoading ? [] : useCompAdmEmpOrderStore().getEmployeesOrdersRecords"
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
              <v-form class="pl-3">
                <v-text-field
                  v-model="selectedTime"
                  :disabled="isLoading"
                  class="w-100"
                  hide-details="auto"
                  label="Select order time"
                  prepend-inner-icon="mdi-clock"
                  type="time"
                  @update:modelValue="updateSelectedTime()"/>
              </v-form>
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
              <v-btn :disabled="isLoading"
                     color="primary"
                     variant="outlined"
                     @click="isAddDialogOpened = true">
                <v-icon icon="mdi-hamburger-plus"/>
              </v-btn>

              <template v-slot:extension>
                <v-tabs v-model="supplierDetailsTab" :disabled="isLoading">
                  <v-tab v-for="supplierDetails in useCompAdmEmpOrderStore().getSuppliersDetails"
                         :key="supplierDetails.id ?? ''"
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
            <v-icon v-else color="success" icon="mdi-check"/>
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
                   variant="plain">
              <v-icon icon="mdi-open-in-app"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title class="text-primary">Ordered Options</v-card-title>
                  <v-card-text>
                    <v-list>
                      <div
                        v-for="(option, index) in useCompAdmEmpOrderStore().getOptionsOfOrder(item.raw)"
                        :key="option.id">
                        <v-divider v-if="index !== 0" thickness="4"/>
                        <v-list-item :title="option.name" prepend-icon="mdi-food"/>
                      </div>
                    </v-list>
                  </v-card-text>
                </v-card>
              </v-menu>
            </v-btn>

            <v-btn v-if="item.raw.status === EmployeeOrderStatus.PendingAdminConfirmation"
                   :disabled="isLoading"
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

  <v-dialog v-model="isAddDialogOpened" :persistent="isDialogLoading">
    <v-row justify="center">
      <v-col cols="4">
        <v-card title="Add employee order">
          <v-form @submit.prevent="onAddOrder()">
            <v-card-text>
              <v-autocomplete
                v-model="selectedUserId"
                :disabled="isDialogLoading"
                :items="useCompAdmUserStore().getEmployeesDetails"
                item-title="email"
                item-value="id"
                label="User"/>
              <v-autocomplete
                v-model="selectedSupplierId"
                :disabled="isDialogLoading"
                :items="useEmployeeSubscriptionStore().getPublicSubscriptionSuppliers"
                item-title="name"
                item-value="supplierId"
                label="Supplier"
                @update:modelValue="onSelectedSupplierChange"/>
              <v-autocomplete
                v-model="selectedOptions"
                :disabled="!selectedSupplierId || isDialogLoading"
                :items="optionsToSelect"
                chips
                clearable
                item-title="name"
                item-value="id"
                label="Options"
                multiple/>
            </v-card-text>
            <v-card-actions>
              <v-btn
                :disabled="selectedOptions.length === 0 || !selectedSupplierId || !selectedUserId"
                :loading="isDialogLoading" block
                color="primary" type="submit" variant="outlined">Validate & Add order
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-col>
      <v-fade-transition>
        <v-col v-if="addDialogOrderValidation && !addDialogOrderValidation.valid" cols="4">
          <v-card height="370">
            <v-card-title class="text-error">Order Validation</v-card-title>
            <v-card-text>
              <v-list>
                <v-list-item v-for="error in addDialogOrderValidation?.errors || []"
                             :key="error">
                  <v-list-item-title>
                    <v-icon color="error" icon="mdi-alert"/>
                    {{ error }}
                  </v-list-item-title>
                </v-list-item>
              </v-list>
            </v-card-text>
          </v-card>
        </v-col>
      </v-fade-transition>
    </v-row>
  </v-dialog>
</template>

<script lang="ts" setup>
import {computed, ComputedRef, onMounted, ref} from "vue";
import {
  EmployeesOrdersTableRecord,
  useCompAdmEmpOrderStore,
  useCompAdmUserStore,
  useEmployeeSubscriptionStore
} from "@/store/company-adm-app";
import {EmployeeOrder} from "@/models/EmployeeOrder";
import {EmployeeOrderStatus} from "@/models/EmployeeOrderStatus";
import employeeOrderService from "@/services/EmployeeOrderService";
import toastManager from "@/services/ToastManager";

onMounted(() => {
  useCompAdmEmpOrderStore().requestFreshDataIfEmpty().finally(() => {
    selectedDate.value = useCompAdmEmpOrderStore().getSelectedDate
    selectedTime.value = useCompAdmEmpOrderStore().getSelectedTime
    useCompAdmEmpOrderStore().requestUpdateOrdersForSelectedDateTime().finally(() => {
      supplierDetailsTab.value = useCompAdmEmpOrderStore().getSelectedSupplierDetails
      isLoading.value = false
    })
  })
})

const isLoading = ref(true)
const selectedDate = ref()
const selectedTime = ref()
const supplierDetailsTab = ref()
const search = ref()
const isAddDialogOpened = ref(false)
const isDialogLoading = ref(false)
const selectedSupplierId = ref('')
const selectedUserId = ref()
const selectedOptions = ref([])
const optionsToSelect: ComputedRef<unknown[] | undefined> = computed(() => useCompAdmEmpOrderStore().getSuppliersOptions
.find(so => so.supplier?.id === selectedSupplierId.value)?.options)
const addDialogOrderValidation = ref()

const headers = [
  {title: 'Email', key: 'email'},
  {title: 'Sup price', key: 'supplierDefaultPrice'},
  {title: 'Sup discount', key: 'supplierDiscount'},
  {title: 'Com discount', key: 'companyDiscount'},
  {title: 'Final price', key: 'finalPrice'},
  {title: 'Options', key: 'options', align: 'center'},
  {title: 'Valid', key: 'isValid', align: 'center'},
  {title: 'Status', key: 'status', align: 'center'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

async function onAddOrder() {
  try {
    isDialogLoading.value = true
    const employeeOrder = new EmployeeOrder(
      '',
      selectedUserId.value,
      '',
      0,
      0,
      0,
      0,
      EmployeeOrderStatus.PendingAdminConfirmation,
      selectedOptions.value,
      selectedDate.value);

    const response = await employeeOrderService
    .compAdmRequestToValidateSingle(employeeOrder)
    addDialogOrderValidation.value = response.data

    if (addDialogOrderValidation.value.valid) {
      await employeeOrderService.compAdmCreateEmployeeOrder(employeeOrder)
      await useCompAdmEmpOrderStore().requestUpdateOrdersForSelectedDateTime()
      selectedSupplierId.value = ''
      selectedUserId.value = ''
      selectedOptions.value = []
      isAddDialogOpened.value = false
      supplierDetailsTab.value = useCompAdmEmpOrderStore().getSelectedSupplierDetails
    }
  } finally {
    isDialogLoading.value = false
  }
}

async function onSelectedSupplierChange() {
  try {
    isDialogLoading.value = true
    await useCompAdmEmpOrderStore().requestSupplierOptionsRefreshIfEmpty(selectedSupplierId.value)
  } finally {
    selectedOptions.value = []
    isDialogLoading.value = false
  }
}

async function updateSelectedDate() {
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().setSelectedDate(selectedDate.value)
  } finally {
    supplierDetailsTab.value = useCompAdmEmpOrderStore().getSelectedSupplierDetails
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
    await useCompAdmEmpOrderStore().requestUpdateOrdersForSelectedDateTime()
    toastManager.showSuccess("Lunch ordered", "Waiting for supplier confirmation")
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
  try {
    isLoading.value = true
    await useCompAdmEmpOrderStore().setSelectedSupplierDetails(supplierDetailsTab.value)
  } finally {
    isLoading.value = false
  }
}
</script>
