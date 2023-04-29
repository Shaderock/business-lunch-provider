<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12">
        <v-data-table
          :headers="headers"
          :items="useEmployeeOrderStore().getEmployeeOrders">
          <template v-slot:top>
            <v-toolbar extended extension-height="1">
              <v-form class="w-50">
                <v-text-field
                  v-model="selectedDate"
                  :disabled="isLoading"
                  hide-details="auto"
                  label="Select order date"
                  prepend-inner-icon="mdi-calendar"
                  type="date"
                  @update:modelValue="updateSelectedDate()"/>
              </v-form>
              <template v-slot:extension>
                <v-progress-linear v-if="isLoading" indeterminate/>
              </template>
            </v-toolbar>

          </template>

          <template v-slot:item.actions="{ item }">
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

    <v-scroll-y-transition>
      <div v-if="!isLoading">
        <v-row justify="center">
          <v-col v-for="option in useEmployeeOrderStore().getOptions"
                 :key="option.id"
                 :value="option"
                 cols="auto">
            <v-card :width="useOrganizationStore().getLogoCardWidth" elevation="5"
                    variant="tonal">
              <v-sheet class="overflow-hidden" color="background" height="50">
                <v-card-title>{{ option.name }}</v-card-title>
              </v-sheet>
              <v-img v-if="option.hasPhoto"
                     :height="useOrganizationStore().getLogoCardMaxHeight"
                     :lazy-src="`${ApiConstants.ANONYM_OPTION}/photo/thumbnail?optionId=${option.id}`"
                     :src="`${ApiConstants.ANONYM_OPTION}/photo?optionId=${option.id}`"
                     aspect-ratio="16/9"
                     cover>
                <template v-slot:placeholder>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-progress-circular indeterminate/>
                  </div>
                </template>
                <template v-slot:error>
                  <div class="d-flex align-center justify-center fill-height">
                    <v-icon :size="useOrganizationStore().getLogoCardMaxHeight"
                            icon="mdi-image"/>
                  </div>
                </template>
              </v-img>

              <div v-else class="d-flex align-center justify-center fill-height">
                <v-icon :size="useOrganizationStore().getLogoCardMaxHeight"
                        icon="mdi-image"/>
              </div>

              <v-card-subtitle class="overflow-auto pt-2">
                Description
              </v-card-subtitle>

              <v-sheet class="overflow-auto" color="background" height="120">
                <v-card-text>
                  {{ option.description }}
                </v-card-text>
              </v-sheet>

              <v-card-text class="text-caption">{{ option.gram }}</v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </div>
    </v-scroll-y-transition>
  </v-container>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useEmployeeOrderStore} from "@/store/employee-app";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import {ApiConstants} from "@/services/ApiConstants";
import {EmployeeOrder} from "@/models/EmployeeOrder";

onMounted(() => {
  selectedDate.value = useEmployeeOrderStore().getSelectedDate
  useEmployeeOrderStore().requestUpdateOrdersForSelectedDate().finally(() => isLoading.value = false)
})

const isLoading = ref(true)
const selectedDate = ref()

const headers = [
  {title: 'Supplier price', key: 'supplierDefaultPrice'},
  {title: 'Supplier discount', key: 'supplierDiscount'},
  {title: 'Company discount', key: 'companyDiscount'},
  {title: 'Final price', key: 'finalPrice'},
  {title: "Status", key: 'status'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

async function updateSelectedDate() {
  try {
    isLoading.value = true
    await useEmployeeOrderStore().setSelectedDate(selectedDate.value)
  } finally {
    isLoading.value = false
  }
}

async function removeOrder(employeeOrder: EmployeeOrder) {
  try {
    isLoading.value = true
    await useEmployeeOrderStore().deleteOrder(employeeOrder)
  } finally {
    isLoading.value = false
  }
}
</script>
