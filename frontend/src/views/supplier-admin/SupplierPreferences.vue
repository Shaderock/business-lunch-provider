<template>
  <v-row justify="center">
    <v-col cols="6">
      <v-card class="mx-auto" elevation="20">
        <v-toolbar>
          <v-toolbar-title>Organization Preferences</v-toolbar-title>

          <v-btn color="primary" @click="initDialogue()">Edit Preferences</v-btn>
        </v-toolbar>

        <v-card-subtitle class="pt-4">
          <v-icon icon="mdi-car-clock"/>
          Request offset
        </v-card-subtitle>
        <v-card-text v-if="useSupAdmSupPrefStore().getPreferences?.requestOffset">
          {{ useSupAdmSupPrefStore().getRequestOffset.days() }} Days
          {{ useSupAdmSupPrefStore().getRequestOffset.hours() }} Hours
          {{ useSupAdmSupPrefStore().getRequestOffset.minutes() }} Minutes
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>

        <v-card-subtitle>
          <v-icon icon="mdi-clock-start"/>
          Delivered not sooner than
        </v-card-subtitle>
        <v-card-text v-if="useSupAdmSupPrefStore().getPreferences?.deliveryPeriodStartTime">
          {{ useSupAdmSupPrefStore().getPreferences.deliveryPeriodStartTime }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>


        <v-card-subtitle>
          <v-icon icon="mdi-clock-end"/>
          Delivered not later than
        </v-card-subtitle>
        <v-card-text v-if="useSupAdmSupPrefStore().getPreferences?.deliveryPeriodEndTime">
          {{ useSupAdmSupPrefStore().getPreferences.deliveryPeriodEndTime }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>


        <v-card-subtitle>
          Minimum orders in a company request
        </v-card-subtitle>
        <v-card-text v-if="useSupAdmSupPrefStore().getPreferences?.minimumOrdersPerCompanyRequest">
          {{ useSupAdmSupPrefStore().getPreferences.minimumOrdersPerCompanyRequest }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>

        <v-card-subtitle>
          Minimum categories in the order
        </v-card-subtitle>
        <v-card-text
          v-if="useSupAdmSupPrefStore().getPreferences?.minimumCategoriesForEmployeeOrder">
          {{ useSupAdmSupPrefStore().getPreferences.minimumCategoriesForEmployeeOrder }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>

        <v-card-subtitle>
          Minimum categories in the order
        </v-card-subtitle>
        <v-card-text v-if="useSupAdmSupPrefStore().getPreferences?.orderType">
          {{ useSupAdmSupPrefStore().getPreferences.orderType }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>
      </v-card>
    </v-col>

    <v-col cols="6">
      <v-card class="mx-auto" elevation="20">

        <v-toolbar>
          <v-toolbar-title>Categories Prices</v-toolbar-title>
          <v-btn color="primary">Add Price</v-btn>
        </v-toolbar>

        <v-card-text>
          <v-table>
            <thead>
            <tr>
              <th scope="col">Categories (Amount)</th>
              <th scope="col">Price (MDL)</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </v-table>
        </v-card-text>
      </v-card>
    </v-col>

    <!--todo refactor validations to not push confirm button twice-->
    <v-dialog v-model="show">
      <v-col cols="12" lg="4" md="6" sm="8">
        <v-card>
          <v-card-title>Edit Organization Preferences</v-card-title>

          <v-form ref="form" v-model="valid" @submit.prevent="submit()">
            <v-card-text>
              <v-card-subtitle class="pt4"></v-card-subtitle>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model="updatePreferences.offsetDays"
                    :rules="[supplierPreferencesRules.minimumZero]"
                    color="primary"
                    label="Request Offset Days"
                    type="number"
                  />
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="updatePreferences.offsetHours"
                    :rules="[supplierPreferencesRules.minimumZero]"
                    color="primary"
                    label="Request Offset Hours"
                    type="number"
                  />
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="updatePreferences.offsetMinutes"
                    :rules="[supplierPreferencesRules.minimumZero]"
                    color="primary"
                    label="Request Offset Minutes"
                    type="number"
                  />
                </v-col>
              </v-row>

              <v-text-field
                v-model="updatePreferences.deliveryStart"
                :rules="[supplierPreferencesRules.required]"
                color="primary"
                hint="Enter the time when the first order can be delivered to the company"
                label="Delivered not sooner than"
                type="time"
              ></v-text-field>

              <v-text-field
                v-model="updatePreferences.deliveryEnd"
                :rules="[supplierPreferencesRules.required]"
                color="primary"
                hint="Enter the time when the last order can be delivered to the company"
                label="Delivered not later than"
                type="time"
              ></v-text-field>


              <v-text-field
                v-model="updatePreferences.minimumOrders"
                :rules="[supplierPreferencesRules.required, supplierPreferencesRules.minimumOne]"
                color="primary"
                hint="Enter a minimum amount of employee orders required for a company request"
                label="Minimum orders in company request"
                type="number"
              ></v-text-field>

              <v-text-field
                v-model="updatePreferences.minimumCategories"
                color="primary"
                hint="Enter a minimum amount of categories required for employee's order"
                label="Minimum categories in employee's order"

                type="number"
              ></v-text-field>


              <v-select v-model="updatePreferences.orderType"
                        :items="[OrderType.UnlimitedOptions, OrderType.OnlyOneOptionPerCategory]"
                        :rules="[supplierPreferencesRules.required]"
                        hint="Select the way employees orders will be created"
                        label="Select order type"/>

            </v-card-text>

            <v-card-actions>
              <v-btn color="primary" type="submit" variant="outlined">Save</v-btn>
              <v-btn color="secondary" variant="plain" @click="show = false">Cancel</v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-col>
    </v-dialog>
  </v-row>

</template>
<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useSupAdmSupPrefStore} from "@/store/supplier-adm-app";
import {OrderType} from "@/models/OrderType";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import moment from "moment";
import toastManager from "@/services/ToastManager";
import {Utils} from "@/models/Utils";

onMounted(() => {
  useSupAdmSupPrefStore().requestFreshPreferencesData()
  // todo request price categories
})

const valid = ref(false);
const show = ref(false)
const updatePreferences = ref({
  offsetDays: 0,
  offsetHours: 0,
  offsetMinutes: 30,
  deliveryStart: '',
  deliveryEnd: '',
  minimumOrders: 1,
  minimumCategories: 1,
  orderType: OrderType.UnlimitedOptions,
});

const supplierPreferencesRules = {
  required: (value: any) => !!value || 'Required field.',
  minimumZero: (value: number) => {
    return value >= 0 || 'Can\'t be negative.';
  },
  minimumOne: (value: number) => {
    return value >= 1 || 'Should be greater than zero.';
  },
};

function initDialogue() {
  show.value = true
  valid.value = false
  const persistedPreferences = useSupAdmSupPrefStore().getPreferences

  let requestOffset: moment.Duration
  let days: number = 0
  let hours: number = 0
  let minutes: number = 30

  if (persistedPreferences?.requestOffset) {
    requestOffset = moment.duration(useSupAdmSupPrefStore().getPreferences?.requestOffset)
    days = requestOffset.days()
    hours = requestOffset.hours()
    minutes = requestOffset.minutes()
  }

  updatePreferences.value.offsetDays = days
  updatePreferences.value.offsetHours = hours
  updatePreferences.value.offsetMinutes = minutes

  const startDate: Date | null = useSupAdmSupPrefStore().getStartTime
  const endDate: Date | null = useSupAdmSupPrefStore().getEndTime

  let startTime: string = startDate != null ? Utils.dateToTimeAsString(startDate) : '10:00:00'
  let endTime: string = endDate != null ? Utils.dateToTimeAsString(endDate) : '19:00:00'

  updatePreferences.value.deliveryStart = startTime
  updatePreferences.value.deliveryEnd = endTime
  updatePreferences.value.minimumOrders = persistedPreferences?.minimumOrdersPerCompanyRequest ?? 1
  updatePreferences.value.minimumCategories = persistedPreferences?.minimumCategoriesForEmployeeOrder ?? 1
  updatePreferences.value.orderType = persistedPreferences?.orderType ?? OrderType.UnlimitedOptions
}

async function submit() {
  if (valid.value === true) {
    try {
      const days: number = updatePreferences.value.offsetDays
      const hours: number = updatePreferences.value.offsetHours
      const minutes: number = updatePreferences.value.offsetMinutes

      const requestOffset: moment.Duration = moment.duration({days, hours, minutes})
      console.log(requestOffset.humanize())

      await supplierPreferencesService.update(new SupplierPreferences(
        null,
        null,
        requestOffset,
        Utils.stringToTime(updatePreferences.value.deliveryStart),
        Utils.stringToTime(updatePreferences.value.deliveryEnd),
        updatePreferences.value.minimumOrders,
        updatePreferences.value.minimumCategories,
        updatePreferences.value.orderType,
        null,
        null
      ))
      toastManager.showSuccess("Updated!", "Your organization preferences were updated successfully")
      show.value = false
      await useSupAdmSupPrefStore().requestFreshPreferencesData()
    } catch (error) {
      console.log('Something wrong happened during preferences update: ' + error)
      toastManager.showDefaultError("There was an error during preferences update")
    }
  }
}
</script>
