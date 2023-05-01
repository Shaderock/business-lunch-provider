<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="6">
        <v-card class="mx-auto" elevation="20" variant="tonal">
          <v-toolbar extended extension-height="1">
            <v-toolbar-title>Organization Preferences</v-toolbar-title>

            <v-btn :disabled="isLoading" color="primary" @click="initDialogue()">Edit Preferences
            </v-btn>

            <template v-slot:extension>
              <v-progress-linear v-if="isLoading" striped/>
            </template>
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
              :subtitle="useSupAdmSupPrefStore().getPreferences.workDayStart?.toString() || ''"
              prepend-icon="mdi-clock-start"
              title="Work day start"/>
            <v-divider inset/>

            <v-list-item
              :subtitle="useSupAdmSupPrefStore().getPreferences.workDayEnd?.toString() || ''"
              prepend-icon="mdi-clock-end"
              title="Work day end"/>
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
        <v-data-table :headers="headers"
                      :items="useSupAdmSupPrefStore().getCategoriesPrices"
                      class="elevation-20">

          <template v-slot:top>
            <v-toolbar extended extension-height="1" title="Categories Prices">
              <v-btn :disabled="isLoading" color="primary" variant="outlined"
                     @click="onAddCategoriesPriceMenu">Add
                categories price
              </v-btn>

              <template v-slot:extension>
                <v-progress-linear v-if="isLoading" striped/>
              </template>
            </v-toolbar>
          </template>

          <template v-slot:item.actions="{ item, index }">
            <v-btn :disabled="isLoading || isDialogLoading"
                   icon
                   variant="plain"
                   @click="onEditCategoriesPriceMenu(item.raw)">
              <v-icon icon="mdi-pencil"/>
            </v-btn>

            <v-btn
              :disabled="isLoading || isDialogLoading || index !== useSupAdmSupPrefStore().getCategoriesPrices.length - 1"
              icon
              variant="plain">
              <v-icon color="error" icon="mdi-delete"/>
              <v-menu activator="parent">
                <v-card title="Are you sure to remove this categories price?" variant="tonal">
                  <v-card-actions>
                    <v-btn block
                           color="error"
                           prepend-icon="mdi-delete"
                           @click="onRemoveCategoriesPrice">
                      Remove price
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>
          </template>
        </v-data-table>
      </v-col>

      <v-dialog v-model="show" :persistent="isDialogLoading">
        <v-row justify="center">
          <v-col md="4">
            <v-card title="Edit Organization Preferences">
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
                        type="number"/>
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.offsetHours"
                        :rules="[supplierPreferencesRules.minimumZero]"
                        color="primary"
                        label="Request Offset Hours"
                        type="number"/>
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.offsetMinutes"
                        :rules="[supplierPreferencesRules.minimumZero]"
                        color="primary"
                        label="Request Offset Minutes"
                        type="number"/>
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
                        type="time"/>
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.deliveryEnd"
                        :rules="[supplierPreferencesRules.required]"
                        color="primary"
                        hint="Enter the time when you stop accepting lunch requests"
                        label="End work at"
                        type="time"/>
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
                        :rules="[supplierPreferencesRules.required, supplierPreferencesRules.minimumOne]"
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
                                multiple
                                prepend-inner-icon="mdi-tag-plus"/>
                    </v-col>
                  </v-row>
                </v-card-text>

                <v-card-actions>
                  <v-btn :loading="isDialogLoading" block color="primary" type="submit"
                         variant="outlined">Save
                  </v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </v-col>
        </v-row>
      </v-dialog>

      <v-dialog v-model="showAddCategoriesPriceDialog" :persistent="isDialogLoading">
        <v-row justify="center">
          <v-col md="3">
            <v-form @submit.prevent="onAddCategoriesPrice">
              <v-card title="Add Categories Price">
                <v-card-subtitle>
                  Amount:
                  <v-chip color="info" label>
                    {{ useSupAdmSupPrefStore().getNextCategoriesPricesAmount }}
                  </v-chip>
                </v-card-subtitle>

                <v-card-text>
                  <v-text-field
                    v-model="categoriesPrice.price"
                    :rules="[supplierPreferencesRules.required, supplierPreferencesRules.positiveDecimal]"
                    label="Price for categories"
                    min="0"
                    step="0.01"
                    type="number"/>
                </v-card-text>

                <v-card-actions>
                  <v-btn :loading="isDialogLoading" block color="primary" type="submit"
                         variant="outlined">
                    Add
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-form>
          </v-col>
        </v-row>

      </v-dialog>

      <v-dialog v-model="showEditCategoriesPriceDialog" :persistent="isDialogLoading">
        <v-row justify="center">
          <v-col md="3">
            <v-form @submit.prevent="onEditCategoriesPrice">
              <v-card title="Edit Categories Price">
                <v-card-subtitle>
                  Amount:
                  <v-chip color="info" label>{{ categoriesPrice.amount }}</v-chip>
                </v-card-subtitle>

                <v-card-text>
                  <v-text-field
                    v-model="categoriesPrice.price"
                    :rules="[supplierPreferencesRules.required, supplierPreferencesRules.positiveDecimal]"
                    label="Price for categories"
                    min="0"
                    step="0.01"
                    type="number"/>
                </v-card-text>

                <v-card-actions>
                  <v-btn :loading="isDialogLoading" block color="primary" type="submit"
                         variant="outlined">
                    Save
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-form>
          </v-col>
        </v-row>

      </v-dialog>
    </v-row>
  </v-container>
</template>
<script lang="ts" setup>
import {onMounted, Ref, ref} from "vue";
import {useSupAdmSupPrefStore} from "@/store/supplier-adm-app";
import {OrderType} from "@/models/OrderType";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import moment from "moment";
import toastManager from "@/services/ToastManager";
import {Utils} from "@/models/Utils";
import {VForm} from "vuetify/components";
import {CategoryTag} from "@/models/CategoryTag";
import {CategoriesPrice} from "@/models/CategoriesPrice";

onMounted(() => {
  useSupAdmSupPrefStore().requestFreshPreferencesDataIfEmpty().finally(() => {
    isLoading.value = false
  })
})

const form = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const valid = ref(false);
const show = ref(false)
const showAddCategoriesPriceDialog = ref(false)
const showEditCategoriesPriceDialog = ref(false)
const isLoading = ref(true)
const isDialogLoading = ref(false)
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
const categoriesPrice = ref()


const headers = [
  {title: 'Categories (Amount)', key: 'amount', align: 'center'},
  {title: 'Price (MDL)', key: 'price', align: 'center'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

const supplierPreferencesRules = {
  required: (value: any) => !!value || 'Required field.',
  minimumZero: (value: number) => {
    return value >= 0 || 'Can\'t be negative.';
  },
  minimumOne: (value: number) => {
    return value >= 1 || 'Should be greater than zero.';
  },
  positiveDecimal: (value: number) => {
    return value >= 0.01 || 'Should be greater than zero.';
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

  updatePreferences.value.deliveryStart = startDate != null ? Utils.formatDateToTimeAsString(startDate) : '10:00:00'
  updatePreferences.value.deliveryEnd = endDate != null ? Utils.formatDateToTimeAsString(endDate) : '19:00:00'
  updatePreferences.value.minimumOrders = persistedPreferences?.minimumOrdersPerCompanyRequest ?? 1
  updatePreferences.value.minimumCategories = persistedPreferences?.minimumCategoriesForEmployeeOrder ?? 1
  updatePreferences.value.orderType = persistedPreferences?.orderType ?? OrderType.UnlimitedOptions
  updatePreferences.value.categoriesTags = persistedPreferences?.categoriesTags ?? [CategoryTag.SALADS]
}

async function submit() {
  await form.value?.validate()
  if (valid.value === true) {
    try {
      isDialogLoading.value = true
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
    } finally {
      isDialogLoading.value = false
    }
  }
}

async function onAddCategoriesPriceMenu() {
  categoriesPrice.value = new CategoriesPrice('', 1, 0.01)
  showAddCategoriesPriceDialog.value = true
}

async function onEditCategoriesPriceMenu(pfc: CategoriesPrice) {
  categoriesPrice.value = new CategoriesPrice(pfc.id, pfc.amount, pfc.price)
  showEditCategoriesPriceDialog.value = true
}

async function onAddCategoriesPrice() {
  try {
    isDialogLoading.value = true
    await useSupAdmSupPrefStore().addPriceForCategories(categoriesPrice.value)
  } finally {
    isDialogLoading.value = false
  }
}

async function onEditCategoriesPrice() {
  try {
    isDialogLoading.value = true
    await useSupAdmSupPrefStore().editPriceForCategories(categoriesPrice.value)
    showEditCategoriesPriceDialog.value = false
  } finally {
    isDialogLoading.value = false
  }
}

async function onRemoveCategoriesPrice() {
  try {
    isDialogLoading.value = true
    await useSupAdmSupPrefStore().removePriceForCategories()
  } finally {
    isDialogLoading.value = false
  }
}
</script>


<style scoped>
.disable-events {
  pointer-events: none
}
</style>
