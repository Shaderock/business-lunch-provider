<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="6">
        <v-card class="mx-auto" elevation="20">
          <v-toolbar>
            <v-toolbar-title>Organization Preferences</v-toolbar-title>

            <v-btn color="primary" @click="initDialogue()">Edit Preferences</v-btn>
          </v-toolbar>

          <v-list>
            <v-list-subheader>Delivery</v-list-subheader>

            <v-list-item prepend-icon="mdi-car-clock" title="Request offset">
              <v-list-item-subtitle>
                {{ useSupAdmSupPrefStore().getRequestOffset.days() }} Days
                {{ useSupAdmSupPrefStore().getRequestOffset.hours() }} Hours
                {{ useSupAdmSupPrefStore().getRequestOffset.minutes() }} Minutes
              </v-list-item-subtitle>
            </v-list-item>
            <v-divider inset/>

            <v-list-item
              :subtitle="useSupAdmSupPrefStore().getPreferences.workDayStart?.toString()"
              prepend-icon="mdi-clock-start"
              title="Delivered not sooner than"/>
            <v-divider inset/>

            <v-list-item
              :subtitle="useSupAdmSupPrefStore().getPreferences.workDayEnd?.toString()"
              prepend-icon="mdi-clock-end"
              title="Delivered not later than"/>
            <v-divider inset/>

            <v-list-subheader>Orders</v-list-subheader>

            <v-list-item
              :subtitle="useSupAdmSupPrefStore().getPreferences.minimumOrdersPerCompanyRequest"
              title="Minimum orders in a company request"/>
            <v-divider inset/>

            <v-list-item
              :subtitle="useSupAdmSupPrefStore().getPreferences.minimumCategoriesForEmployeeOrder"
              title="Minimum categories in the order"/>
            <v-divider inset/>

            <v-list-item :subtitle="useSupAdmSupPrefStore().getPreferences.orderType"
                         title="OrderType"/>
            <v-divider inset/>

            <v-list-item title="Categories types">
              <v-chip-group class="disable-events">
                <v-chip v-for="(tag) in useSupAdmSupPrefStore().getPreferences.categoriesTags"
                        :key="tag" label>
                  {{ tag }}
                </v-chip>
              </v-chip-group>
            </v-list-item>
            <v-divider inset/>
          </v-list>
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

      <v-dialog v-model="show">
        <v-row justify="center">
          <v-col md="4" sm="8">
            <v-card>
              <v-card-title>Edit Organization Preferences</v-card-title>

              <v-form ref="form" v-model="valid" @submit.prevent="submit()">
                <v-card-text>
                  <v-card-subtitle class="pt4">Request Offset</v-card-subtitle>
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

                  <v-row>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.deliveryStart"
                        :rules="[supplierPreferencesRules.required]"
                        color="primary"
                        hint="Enter the time when you start accepting lunch requests"
                        label="Start work at"
                        type="time"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.deliveryEnd"
                        :rules="[supplierPreferencesRules.required]"
                        color="primary"
                        hint="Enter the time when you stop accepting lunch requests"
                        label="End work at"
                        type="time"
                      />
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.minimumOrders"
                        :rules="[supplierPreferencesRules.required, supplierPreferencesRules.minimumOne]"
                        color="primary"
                        hint="Enter a minimum amount of employee orders required for a company request"
                        label="Minimum orders in company request"
                        type="number"/>
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.minimumCategories"
                        color="primary"
                        hint="Enter a minimum amount of categories required for employee's order"
                        label="Minimum categories in employee's order"
                        type="number"/>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col>
                      <v-select v-model="updatePreferences.orderType"
                                :items="[OrderType.UnlimitedOptions, OrderType.OnlyOneOptionPerCategory, OrderType.OnlyOneOption]"
                                :rules="[supplierPreferencesRules.required]"
                                hint="Select the way employees orders will be created"
                                label="Select order type"/>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col>
                      <v-select v-model="updatePreferences.categoriesTags"
                                :items="[CategoryTag.BURGERS, CategoryTag.PIZZA, CategoryTag.SUSHI, CategoryTag.KEBAB,
                              CategoryTag.PASTRY, CategoryTag.STEAK, CategoryTag.BBQ, CategoryTag.PASTA,
                              CategoryTag.SEAFOOD, CategoryTag.WOK, CategoryTag.MEDITERRANEAN, CategoryTag.TRADITIONAL,
                              CategoryTag.BREAKFAST, CategoryTag.PANCAKES, CategoryTag.ORIENTAL, CategoryTag.VEGETARIAN,
                              CategoryTag.SALADS,  CategoryTag.DESSERTS,  CategoryTag.OTHER]"
                                :rules="[supplierPreferencesRules.required]"
                                chips
                                clearable
                                closable-chips
                                hint="What types of categories do you offer?"
                                label="Tune your menu"
                                multiple/>
                    </v-col>
                  </v-row>
                </v-card-text>

                <v-card-actions>
                  <v-btn color="primary" type="submit" variant="outlined">Save</v-btn>
                  <v-btn color="secondary" variant="plain" @click="show = false">Cancel</v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </v-col>
        </v-row>
      </v-dialog>
    </v-row>
  </v-container>
</template>
<script lang="ts" setup>
import {computed, onMounted, Ref, ref} from "vue";
import {useSupAdmSupPrefStore} from "@/store/supplier-adm-app";
import {OrderType} from "@/models/OrderType";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import moment from "moment";
import toastManager from "@/services/ToastManager";
import {Utils} from "@/models/Utils";
import {VForm} from "vuetify/components";
import {CategoryTag} from "@/models/CategoryTag";

onMounted(() => {
  useSupAdmSupPrefStore().requestFreshPreferencesData()
  //todo request price categories
})

const form = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const tags = computed(() => useSupAdmSupPrefStore().getPreferences.categoriesTags)
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
  categoriesTags: [CategoryTag.SALADS]
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

  updatePreferences.value.deliveryStart = startDate != null ? Utils.dateToTimeAsString(startDate) : '10:00:00'
  updatePreferences.value.deliveryEnd = endDate != null ? Utils.dateToTimeAsString(endDate) : '19:00:00'
  updatePreferences.value.minimumOrders = persistedPreferences?.minimumOrdersPerCompanyRequest ?? 1
  updatePreferences.value.minimumCategories = persistedPreferences?.minimumCategoriesForEmployeeOrder ?? 1
  updatePreferences.value.orderType = persistedPreferences?.orderType ?? OrderType.UnlimitedOptions
  updatePreferences.value.categoriesTags = persistedPreferences?.categoriesTags ?? [CategoryTag.SALADS]
}

async function submit() {
  await form.value?.validate()
  if (valid.value === true) {
    try {
      const days: number = updatePreferences.value.offsetDays
      const hours: number = updatePreferences.value.offsetHours
      const minutes: number = updatePreferences.value.offsetMinutes

      const requestOffset: moment.Duration = moment.duration({days, hours, minutes})

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
        null,
        updatePreferences.value.categoriesTags
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


<style scoped>
.disable-events {
  pointer-events: none
}
</style>
