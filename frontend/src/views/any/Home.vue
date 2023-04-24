<template>
  <v-banner>
    <v-tabs v-model="tab" align-tabs="center" stacked>
      <v-tab color="primary" value="suppliers">
        <v-icon icon="mdi-food"/>
        Suppliers
      </v-tab>
      <v-tab color="primary" value="subscribed">
        <v-icon icon="mdi-star"/>
        Subscribed
      </v-tab>
      <v-tab color="primary" value="favorites">
        <v-icon icon="mdi-heart"/>
        Favorites
      </v-tab>
      <v-tab color="primary" value="filters">
        <v-icon icon="mdi-filter"/>
        Filters
      </v-tab>
    </v-tabs>
    <v-progress-linear v-if="isLoading" indeterminate striped/>
  </v-banner>

  <v-row v-if="isLoading" justify="space-evenly" justify-sm="center">
    <v-col v-for="n in 20" :key="n">
      <v-card :width="cardWidth" elevation="20" height="400">
      </v-card>
    </v-col>
  </v-row>

  <div v-else>
    <v-window v-model="tab">
      <v-window-item value="suppliers">
        <v-container>
          <v-row justify="space-evenly" justify-sm="center">
            <v-col v-for="workingSupplier in useWorkingSuppliersStore().getWorkingSuppliersLimited"
                   :key="workingSupplier.name"
                   cols="auto">
              <v-card :title="workingSupplier.name" :width="cardWidth" elevation="20"
                      @click="openSupplierProfile(workingSupplier)">
                <v-img
                  :lazy-src="ApiConstants.ANONYM_ORGANIZATION + '/logo' +
                              '?supplierId='+workingSupplier.supplierId +
                              '&width=' + 25 +
                              '&maxHeight='+ useOrganizationStore().getLogoCardMaxHeight"
                  :max-height="useOrganizationStore().getLogoCardMaxHeight"
                  :src="ApiConstants.ANONYM_ORGANIZATION + '/logo' +
                              '?supplierId='+workingSupplier.supplierId +
                              '&width=' + useOrganizationStore().getLogoCardWidth +
                              '&maxHeight='+ useOrganizationStore().getLogoCardMaxHeight"
                  :width="useOrganizationStore().getLogoCardWidth"
                  @error="supplierLogoLoaded()"
                  @load="supplierLogoLoaded()">
                  <template v-slot:placeholder>
                    <v-row justify="center">
                      <v-icon :size="useOrganizationStore().getLogoCardMaxHeight"
                              class="text-center"
                              icon="mdi-image-area"/>
                    </v-row>
                  </template>
                </v-img>
                <v-card-subtitle class="pt-4">Offers</v-card-subtitle>
                <v-card-text>
                  <v-chip-group class="disable-events">
                    <v-chip v-for="(tag) in workingSupplier.categoriesTags" :key="tag" label>
                      {{ tag }}
                    </v-chip>
                  </v-chip-group>
                </v-card-text>

                <v-card-subtitle>Working hours</v-card-subtitle>
                <v-card-text>
                  <v-chip-group class="disable-events">
                    <v-chip
                      :text="Utils.stringToTimeAsStringWithoutSeconds(workingSupplier.workDayStart.toString())"
                      clickable="false" label prepend-icon="mdi-clock-start"/>
                    <v-chip
                      :text="Utils.stringToTimeAsStringWithoutSeconds(workingSupplier.workDayEnd.toString())"
                      append-icon="mdi-clock-end" label/>
                  </v-chip-group>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-window-item>
      <v-window-item value="subscribed">

      </v-window-item>
      <v-window-item value="favorites">

      </v-window-item>
      <v-window-item value="filters">

      </v-window-item>
    </v-window>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useWorkingSuppliersStore, WorkingSupplier} from "@/store/user-app";
import {Utils} from "@/models/Utils";
import {ApiConstants} from "@/services/ApiConstants";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";

onMounted(() => {
  useWorkingSuppliersStore().requestFreshDataIfNothingCached().finally(() => {
    isLoading.value = false
  })
})

const isLoading = ref(true)
const tab = ref(1)
const cardWidth: number = 352

async function openSupplierProfile(workingSupplier: WorkingSupplier) {
  // todo
}

async function supplierLogoLoaded() {
  useWorkingSuppliersStore().incrementLimit()
}
</script>

<style scoped>
.disable-events {
  pointer-events: none
}
</style>
