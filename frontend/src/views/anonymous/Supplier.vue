<template>
  <v-dialog v-model="isSupplierNotFoundDialogShown" persistent>
    <v-row justify="center">
      <v-col cols="auto">
        <v-card>
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
              :text="Utils.stringToTimeAsStringWithoutSeconds(usePublicSupplierStore().getPreferences?.workDayStart.toString())"
              label
              prepend-icon="mdi-clock-start"/>
            <v-chip
              :text="Utils.stringToTimeAsStringWithoutSeconds(usePublicSupplierStore().getPreferences?.workDayEnd.toString())"
              append-icon="mdi-clock-end"
              label/>
          </v-chip-group>

          <v-btn append-icon="mdi-open-in-new" color="secondary" variant="tonal">
            Details
            <v-dialog activator="parent">
              <v-row justify="center">
                <v-col cols="6">
                  <v-card>
                    <v-card-title>{{ usePublicSupplierStore().getDetails.name }}</v-card-title>
                    <v-card-text>
                      <v-list>
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

                        <v-list-item-subtitle>Description</v-list-item-subtitle>
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
            <v-tabs v-model="tabs" :disabled="isLoading" align-tabs="start" center-active
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
          <v-row justify="space-between" justify-sm="center">
            <v-col v-for="option in usePublicSupplierStore().getOptionsLimited" :key="option.id"
                   :value="option"
                   cols="auto">
              <v-card :width="useOrganizationStore().getLogoCardWidth" elevation="5"
                      variant="outlined">
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
                  <v-btn append-icon="mdi-cart-arrow-down" color="primary" variant="outlined">
                    Add to cart
                  </v-btn>
                  <v-spacer/>
                  <v-chip color="info" label>{{ option.price }} MDL</v-chip>
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
  await usePublicSupplierStore().requestFreshData().finally(() => updateStateAfterDataLoad())
}

function updateStateAfterDataLoad() {
  isSupplierNotFoundDialogShown.value = !usePublicSupplierStore().isSupplierFound;
  const category = usePublicSupplierStore().getCurrentCategory;
  if (category) {
    tabs.value = category;
  }
  isLoading.value = false;
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
