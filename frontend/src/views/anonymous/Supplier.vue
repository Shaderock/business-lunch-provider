<template>
  <v-dialog v-model="isSupplierNotFoundDialogShown" persistent>
    <v-row justify="center">
      <v-col cols="auto">
        <v-card v>
          <v-card-title>Supplier {{ usePublicSupplierStore().currentSupplierName }} not found
          </v-card-title>
          <v-card-text>
            <v-row justify="center">
              <v-col>
                <v-icon class="text-center" icon="mdi-emoticon-sad" size="50"/>
              </v-col>
            </v-row>

          </v-card-text>
          <v-card-actions>
            <v-btn block color="primary" prepend-icon="mdi-home"
                   v-bind:to="RouterPaths.ANONYMOUS_HOME"
                   variant="outlined">
              Home
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

  </v-dialog>

  <v-img v-if="usePublicSupplierStore().isSupplierFound && usePublicSupplierStore().getSupplier?.id"
         :height="250"
         :lazy-src="ApiConstants.ANONYM_ORGANIZATION + '/logo' +
                              '?supplierId='+usePublicSupplierStore().getSupplier?.id +
                              '&width=' + 100 +
                              '&maxHeight='+ useOrganizationStore().getLogoCardMaxHeight"
         :src="ApiConstants.ANONYM_ORGANIZATION + '/logo' +
                              '?supplierId='+usePublicSupplierStore().getSupplier?.id +
                              '&width=' + 1000 +
                              '&maxHeight='+ 250"
         aspect-ratio="16-9"
         cover>
    <template v-slot:placeholder>
      <div class="d-flex align-center justify-center fill-height">
        <v-progress-circular indeterminate/>
      </div>
    </template>
  </v-img>

  <v-container>
    <v-row>
      <v-col>
        <v-toolbar elevation="5" extended>
          <v-btn color="secondary"
                 prepend-icon="mdi-arrow-left-thin"
                 size="small"
                 variant="plain"
                 @click="routerBack()">
            Back
          </v-btn>
          <v-btn color="secondary"
                 prepend-icon="mdi-refresh"
                 size="small"
                 variant="plain"
                 @click="refreshData()">
            Refresh
          </v-btn>

          <v-toolbar-title class="text-h4 font-weight-bold text-center">
            {{ usePublicSupplierStore().getCurrentSupplierName }}
          </v-toolbar-title>

          <v-spacer/>

          <v-chip-group v-if="usePublicSupplierStore().isSupplierFound" class="disable-events">
            <v-chip
              :text="Utils.stringToTimeAsStringWithoutSeconds(usePublicSupplierStore()
              .getPreferences?.workDayStart.toString() || new Date().toString())"
              label
              prepend-icon="mdi-clock-start"/>
            <v-chip
              :text="Utils.stringToTimeAsStringWithoutSeconds(usePublicSupplierStore()
              .getPreferences?.workDayEnd.toString() || new Date().toString())"
              append-icon="mdi-clock-end"
              label/>
          </v-chip-group>

          <v-btn append-icon="mdi-open-in-app" color="info" variant="tonal">
            Details
            <v-dialog activator="parent">
              <v-row justify="center">
                <v-col md="6">
                  <v-card>
                    <v-card-title>{{ usePublicSupplierStore().getDetails?.name || ''}}</v-card-title>
                    <v-card-text>
                      <v-list>
                        <v-list-subheader title="Details"/>
                        <v-divider inset/>
                        <v-list-item :subtitle="usePublicSupplierStore().getDetails?.email || ''"
                                     prepend-icon="mdi-email"
                                     title="Email"/>
                        <v-divider inset/>

                        <v-list-item :subtitle="usePublicSupplierStore().getDetails?.phone || ''"
                                     prepend-icon="mdi-phone"
                                     title="Phone"/>
                        <v-divider inset/>

                        <v-list-item :href="usePublicSupplierStore().getSupplier?.websiteUrl || ''"
                                     :subtitle="usePublicSupplierStore().getSupplier?.websiteUrl || ''"
                                     prepend-icon="mdi-web"
                                     title="Website URL"/>
                        <v-divider inset/>

                        <v-list-item :href="usePublicSupplierStore().getSupplier?.menuUrl || ''"
                                     :subtitle="usePublicSupplierStore().getSupplier?.menuUrl || ''"
                                     prepend-icon="mdi-web"
                                     title="Menu URL"/>
                        <v-divider inset/>

                        <v-list-subheader title="Preferences"/>

                        <v-list-item
                          :subtitle="usePublicSupplierStore().getPreferences?.minimumCategoriesForEmployeeOrder || 0"
                          title="Minimum categories in an order"/>
                        <v-divider inset/>

                        <v-list-item
                          :subtitle="usePublicSupplierStore().getPreferences?.minimumOrdersPerCompanyRequest || 0"
                          title="Minimum employees orders per company lunch request"/>
                        <v-divider inset/>

                        <v-list-item
                          :subtitle="usePublicSupplierStore().getPreferences?.orderType || ''"
                          title="Order type"/>
                        <v-divider inset/>

                        <v-list-subheader title="Delivery"/>

                        <v-list-item
                          :subtitle="moment.duration(usePublicSupplierStore().getPreferences?.requestOffset).humanize()"
                          prepend-icon="mdi-car-clock"
                          title="Request offset"/>
                        <v-divider inset/>

                        <v-list-item-subtitle class="pt-2">Description</v-list-item-subtitle>
                      </v-list>

                      <v-sheet class="overflow-auto" color="surface" height="150">
                        {{ usePublicSupplierStore().getDetails?.description }}
                      </v-sheet>
                    </v-card-text>
                  </v-card>
                </v-col>
              </v-row>
            </v-dialog>
          </v-btn>

          <template v-slot:extension>
            <v-tabs v-model="tabs"
                    :disabled="isLoading"
                    align-tabs="start"
                    center-active
                    density="compact"
                    show-arrows>
              <v-tab v-for="categoryOptions in usePublicSupplierStore().getCategoriesOptions"
                     :key="categoryOptions.category.id"
                     :value="categoryOptions"
                     color="primary"
                     @click="onTabSwitch(categoryOptions)">
                {{ categoryOptions.category.name }}
              </v-tab>
            </v-tabs>
          </template>
        </v-toolbar>
      </v-col>
      <v-progress-linear v-if="isLoading" indeterminate/>
    </v-row>
  </v-container>

  <v-container>
    <v-window v-model="tabs">
      <v-window-item v-for="categoryOptions in usePublicSupplierStore().getCategoriesOptions"
                     :key="categoryOptions.category.id"
                     :value="categoryOptions">
        <v-container class="fill-height">
          <v-row justify="center" justify-sm="center">
            <v-col v-for="option in usePublicSupplierStore().getOptionsLimited" :key="option.id"
                   :value="option"
                   cols="auto">
              <v-card :width="useOrganizationStore().getLogoCardWidth" elevation="10"
                      variant="tonal">
                <v-sheet class="overflow-hidden" color="background" height="50">
                  <v-card-title>{{ option.name }}</v-card-title>
                </v-sheet>
                <v-img v-if="option.hasPhoto"
                       :height="useOrganizationStore().getLogoCardMaxHeight"
                       :lazy-src="`${ApiConstants.ANONYM_OPTION}/photo/thumbnail?optionId=${option.id}`"
                       :src="`${ApiConstants.ANONYM_OPTION}/photo?optionId=${option.id}`"
                       aspect-ratio="16/9"
                       cover
                       @error="optionPhotoLoaded()"
                       @load="optionPhotoLoaded()">
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
                <v-card-actions>
                  <v-btn v-if="usePublicSupplierStore().isCompanySubscribed"
                         append-icon="mdi-cart-arrow-down"
                         color="primary"
                         variant="outlined"
                         @click="addOptionToCart(option, categoryOptions)">
                    Add to cart
                  </v-btn>
                  <v-spacer/>
                  <v-chip
                    v-if="usePublicSupplierStore().getPreferences?.orderType !== OrderType.OnlyOneOptionPerCategory"
                    color="info" label>{{ option.price }} MDL
                  </v-chip>

                  <v-menu v-else open-on-hover>
                    <template v-slot:activator="{ props }">
                      <v-btn
                        append-icon="mdi-information"
                        color="info"
                        size="small"
                        v-bind="props"
                        variant="tonal">
                        By category
                      </v-btn>
                    </template>
                    <v-list>
                      <v-list-item
                        v-for="categoriesPrice in usePublicSupplierStore().getCategoriesPrices"
                        :key="categoriesPrice.id">
                        <v-tooltip
                          v-if="categoriesPrice.amount === usePublicSupplierStore().getPreferences?.minimumCategoriesForEmployeeOrder"
                          class="color-info" location="bottom">
                          <template v-slot:activator="{ props }">
                            <v-list-item-title class="text-info mr-10" v-bind="props">
                              {{ categoriesPrice.amount }} categories
                              <v-icon icon="mdi-information"/>
                            </v-list-item-title>
                          </template>
                          Minimum required categories for one order
                        </v-tooltip>

                        <v-list-item-title v-else class="mr-10">
                          {{ categoriesPrice.amount }} categories
                        </v-list-item-title>

                        <template v-slot:append>
                          <v-chip color="info" label>{{ categoriesPrice.price }} MDL</v-chip>
                        </template>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-window-item>
    </v-window>
  </v-container>
</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {onBeforeMount, onMounted, ref} from "vue";
import {CategoryOptions, usePublicSupplierStore} from "@/store/user-app";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import router from "@/router";
import {RouterPaths} from "@/services/RouterPaths";
import {Utils} from "@/models/Utils";
import {ApiConstants} from "@/services/ApiConstants";
import {useCartStore} from "@/store/employee-app";
import {Option} from "@/models/Option";
import {OrderType} from "@/models/OrderType";
import moment from 'moment';

const route = useRoute();
const isLoading = ref(false)
const isSupplierNotFoundDialogShown = ref(false)
const tabs = ref()

onBeforeMount(() => {
  let supplierName = route.params.supplierName;
  if (Array.isArray(supplierName)) {
    supplierName = supplierName[0];
  }
  usePublicSupplierStore().setCurrentSupplierName(supplierName);
})

onMounted(() => {
  refreshData()
});

async function onTabSwitch(categoryOptions: CategoryOptions) {
  isLoading.value = true
  await usePublicSupplierStore().setCurrentCategoryAndFetch(categoryOptions)
  isLoading.value = false
}

function optionPhotoLoaded() {
  usePublicSupplierStore().incrementLimit()
}

async function refreshData() {
  isLoading.value = true;
  usePublicSupplierStore().clearData()
  try {
    await usePublicSupplierStore().requestFreshData()
  } finally {
    updateStateAfterDataLoad()
  }
}

function updateStateAfterDataLoad() {
  isSupplierNotFoundDialogShown.value = !usePublicSupplierStore().isSupplierFound;
  const category = usePublicSupplierStore().getCurrentCategory;
  if (category) {
    tabs.value = category;
  }
  isLoading.value = false;
}

function addOptionToCart(option: Option, categoryOptions: CategoryOptions) {
  const supplier = usePublicSupplierStore().getSupplier
  const details = usePublicSupplierStore().getDetails
  const preferences = usePublicSupplierStore().getPreferences
  const categoriesPrices = usePublicSupplierStore().getCategoriesPrices

  if (supplier && details && preferences)
    useCartStore().addCartOption(
      {
        id: Utils.uuid(),
        option: option,
        category: categoryOptions.category,
        supplier: supplier,
        supplierDetails: details,
        supplierPreferences: preferences,
        supplierCategoriesPrices: categoriesPrices
      })
}

function routerBack() {
  router.back()
}
</script>

<style scoped>
.disable-events {
  pointer-events: none
}
</style>
