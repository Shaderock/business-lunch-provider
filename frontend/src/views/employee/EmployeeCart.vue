<template>
  <v-container v-if="useCartStore().getCartOptions.length > 0">
    <v-row>
      <v-col>
        <v-row>
          <v-col>
            <v-toolbar elevation="5" extended floating>
              <template v-slot:extension>
                <v-tabs v-model="supplierTabs" :disabled="isLoading" align-tabs="start">
                  <v-tab v-for="cartSupplierInfo in useCartStore().getCartSuppliersInfo"
                         :key="cartSupplierInfo.supplier.id"
                         :value="cartSupplierInfo"
                         color="primary"
                         @click="switchSupplier(cartSupplierInfo)">
                    {{ cartSupplierInfo.supplierDetails.name }}
                  </v-tab>
                </v-tabs>
              </template>

              <v-form class="w-50">
                <v-text-field
                  v-model="selectedDate"
                  :disabled="isLoading"
                  class="w-50"
                  hide-details="auto"
                  label="Select order date"
                  prepend-inner-icon="mdi-calendar"
                  type="date"
                  @update:modelValue="updateSelectedDate()"/>
              </v-form>
              <v-spacer/>
              <v-btn
                color="secondary" prepend-icon="mdi-arrow-left-thin"
                @click="router.back()">
                Back
              </v-btn>
              <v-btn :disabled="isLoading"
                     append-icon="mdi-delete"
                     color="error">
                Remove options
                <v-menu activator="parent">
                  <v-card :title="`Are you sure to remove all options for
                    ${useCartStore().getCurrentSupplierInfo?.supplierDetails.name}`"
                          variant="tonal">
                    <v-form @submit.prevent="onSupplierOptionsDelete()">
                      <v-card-actions>
                        <v-btn block color="error" type="submit">Delete options</v-btn>
                      </v-card-actions>
                    </v-form>
                  </v-card>
                </v-menu>
              </v-btn>
            </v-toolbar>
            <v-progress-linear v-if="isLoading" indeterminate/>
          </v-col>
        </v-row>
        <v-row v-if="useCartStore().getCartOptions.length > 0" justify="center">
          <v-col v-if="!isLoading" cols="auto">
            <v-card v-if="useCartStore().getCurrentOrderValidation?.valid === true"
                    elevation="5"
                    variant="tonal"
                    width="350">
              <v-card-title class="text-primary">Summary</v-card-title>
              <v-list>
                <v-list-item title="Supplier">
                  <v-list-item-subtitle>
                    {{ useCartStore().getCurrentSupplierInfo.supplierDetails.name }}
                  </v-list-item-subtitle>
                </v-list-item>
                <v-divider inset/>

                <v-list-item title="Options amount">
                  <v-list-item-subtitle>
                    {{ useCartStore().getCartSupplierCartOptions.length }}
                  </v-list-item-subtitle>
                </v-list-item>
                <v-divider/>

                <v-list-subheader>Options</v-list-subheader>

                <v-sheet class="overflow-auto" color="background" max-height="300">
                  <v-list-item v-for="cartOption in useCartStore().getCartSupplierCartOptions"
                               :key="cartOption.id"
                               :value="cartOption" class="disable-events"
                               prepend-icon="mdi-food">
                    <v-list-item-title>
                      {{ cartOption.category.name }}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                      {{ cartOption.option.name }}
                    </v-list-item-subtitle>
                    <template
                      v-if="useCartStore().getCurrentSupplierInfo?.supplierPreferences.orderType !== OrderType.OnlyOneOptionPerCategory"
                      v-slot:append>
                      <v-chip color="info" label>{{ cartOption.option.price }}</v-chip>
                    </template>
                  </v-list-item>
                </v-sheet>
                <v-divider/>

                <v-list-subheader>Price</v-list-subheader>

                <v-list-item title="Supplier default price sum">
                  <template v-slot:append>
                    <v-chip class="font-weight-bold" color="info" label>
                      +{{ useCartStore().getCurrentOrderWithPrices.supplierDefaultPrice }} MDL
                    </v-chip>
                  </template>
                </v-list-item>
                <v-divider inset/>

                <v-list-item prepend-icon="mdi-sale" title="Supplier discount">
                  <template v-slot:append>
                    <v-chip class="font-weight-bold" color="secondary" label>
                      -{{ useCartStore().getCurrentOrderWithPrices.supplierDiscount }} MDL
                    </v-chip>
                  </template>
                </v-list-item>
                <v-divider inset/>

                <v-list-item prepend-icon="mdi-sale" title="Company discount">
                  <template v-slot:append>
                    <v-chip class="font-weight-bold" color="secondary" label>
                      -{{ useCartStore().getCurrentOrderWithPrices.companyDiscount }} MDL
                    </v-chip>
                  </template>
                </v-list-item>
                <v-divider inset/>

                <v-list-item title="Final price">
                  <template v-slot:append>
                    <v-chip class="font-weight-bold" color="success" label>
                      {{ useCartStore().getCurrentOrderWithPrices.finalPrice }} MDL
                    </v-chip>
                  </template>
                </v-list-item>
              </v-list>
              <v-card-actions>
                <v-btn :disabled="isLoading || !useCartStore().getCurrentOrderValidation?.valid"
                       append-icon="mdi-send-clock"
                       block
                       color="primary"
                       variant="outlined"
                       @click="onOrderSend">Send order
                </v-btn>
              </v-card-actions>
            </v-card>
            <v-card
              v-else
              elevation="5"
              height="700"
              variant="tonal"
              width="350">
              <v-card-title class="text-error">Order validation</v-card-title>
              <v-sheet class="overflow-auto" color="background" height="650">
                <v-card-text
                  v-for="(error, index) in useCartStore().getCurrentOrderValidation?.errors"
                  :key="index"
                  :value="error">
                  <v-icon color="error" icon="mdi-alert"/>
                  {{ error }}
                  <v-divider thickness="4"/>
                </v-card-text>
              </v-sheet>
            </v-card>

          </v-col>

          <v-scroll-y-transition>
            <v-col v-if="!isLoading">
              <v-window v-model="supplierTabs">
                <v-window-item v-for="cartSupplierInfo in useCartStore().getCartSuppliersInfo"
                               :key="cartSupplierInfo.supplier.id"
                               :value="cartSupplierInfo">
                  <v-row justify="center">
                    <v-col v-for="cartOption in useCartStore().getCartSupplierCartOptions"
                           :key="cartOption.id"
                           :value="cartOption"
                           cols="auto">
                      <v-card :width="useOrganizationStore().getLogoCardWidth" elevation="5"
                              variant="tonal">
                        <v-sheet class="overflow-hidden" color="background" height="50">
                          <v-card-title>{{ cartOption.option.name }}</v-card-title>
                        </v-sheet>
                        <v-img v-if="cartOption.option.hasPhoto"
                               :height="useOrganizationStore().getLogoCardMaxHeight"
                               :lazy-src="`${ApiConstants.ANONYM_OPTION}/photo/thumbnail?optionId=${cartOption.option.id}`"
                               :src="`${ApiConstants.ANONYM_OPTION}/photo?optionId=${cartOption.option.id}`"
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
                            {{ cartOption.option.description }}
                          </v-card-text>
                        </v-sheet>

                        <v-card-text class="text-caption">{{ cartOption.option.gram }}</v-card-text>
                        <v-card-actions>
                          <v-btn v-if="useProfileStore().isEmployee"
                                 append-icon="mdi-cart-remove"
                                 color="error">
                            Remove
                            <v-menu activator="parent">
                              <v-card
                                :title="`Are you sure to remove ${cartOption.option.name} from cart`">
                                <v-card-actions>
                                  <v-form @submit.prevent="onOptionDelete(cartOption)">
                                    <v-card-actions>
                                      <v-btn block color="error" type="submit">Remove option</v-btn>
                                    </v-card-actions>
                                  </v-form>
                                </v-card-actions>
                              </v-card>
                            </v-menu>
                          </v-btn>
                          <v-spacer/>
                          <v-chip
                            v-if="useCartStore().getCurrentSupplierInfo?.supplierPreferences.orderType !== OrderType.OnlyOneOptionPerCategory"
                            color="info" label>{{ cartOption.option.price }} MDL
                          </v-chip>

                          <v-menu v-else open-on-hover>
                            <template v-slot:activator="{ props }">
                              <v-btn
                                append-icon="mdi-information"
                                color="info"
                                size="small"
                                v-bind="props"
                                variant="tonal">
                                By Category
                              </v-btn>
                            </template>
                            <v-list>
                              <v-list-item
                                v-for="categoriesPrice in useCartStore().getCurrentSupplierInfo?.supplierCategoriesPrices
                                .sort((cur, next) => cur.amount - next.amount)"
                                :key="categoriesPrice">
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
                                  <v-chip color="info" label>{{ categoriesPrice.price }} MDL
                                  </v-chip>
                                </template>
                              </v-list-item>
                            </v-list>
                          </v-menu>
                        </v-card-actions>
                      </v-card>
                    </v-col>
                  </v-row>
                </v-window-item>
              </v-window>
            </v-col>
          </v-scroll-y-transition>
        </v-row>
      </v-col>
    </v-row>
  </v-container>

  <div v-else class="d-flex align-center justify-center fill-height ">
    <v-card variant="tonal">
      <v-card-text class="text-h2">
        <v-icon icon="mdi-cart-off"/>
        Cart is empty
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="secondary" prepend-icon="mdi-arrow-left-thin"
          @click="router.back()">
          Back
        </v-btn>
      </v-card-actions>
    </v-card>

  </div>

  <v-snackbar v-model="snackbar">
    {{ snackBarText }}

    <template v-slot:actions>
      <v-btn
        color="red"
        variant="plain"
        @click="snackbar = false">
        Close
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {CartOption, CartSupplierInfo, useCartStore} from "@/store/employee-app";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import {ApiConstants} from "@/services/ApiConstants";
import {useProfileStore, usePublicSupplierStore} from "@/store/user-app";
import {Utils} from "@/models/Utils";
import router from "@/router";
import {OrderType} from "@/models/OrderType";

const supplierTabs = ref()
const snackbar = ref()
const snackBarText = ref()
const isLoading = ref(false)
const selectedDate = ref('')

onMounted(() => {
  selectedDate.value = Utils.formatDateWithoutTimeWithDashes(new Date)
  useCartStore().initializeFromLocalStorage()
  updateSelectedDate()
  if (useCartStore().getCartOptions.length > 0) {
    switchSupplier(useCartStore().getCartOptions[0])
  }
})

async function switchSupplier(cartSupplierInfo: CartSupplierInfo) {
  isLoading.value = true
  try {
    await useCartStore().setCurrentSupplier(cartSupplierInfo)
  } finally {
    isLoading.value = false
  }
}

async function onSupplierOptionsDelete() {
  try {
    isLoading.value = true
    await useCartStore().removeCartOptionsForCurrentSupplier()
    supplierTabs.value = useCartStore().getCurrentSupplierInfo
  } finally {
    isLoading.value = false
  }
}

async function onOptionDelete(cartOption: CartOption) {
  try {
    isLoading.value = true
    await useCartStore().removeCartOption(cartOption)
  } finally {
    isLoading.value = false
  }
}

async function onOrderSend() {
  isLoading.value = true
  try {
    await useCartStore().sendCurrentOptions()
  } finally {
    isLoading.value = false
  }
}

async function updateSelectedDate() {
  isLoading.value = true
  try {
    await useCartStore().setSelectedDate(selectedDate.value)
  } finally {
    isLoading.value = false
  }

}
</script>

<style scoped>
.disable-events {
  pointer-events: none
}
</style>
