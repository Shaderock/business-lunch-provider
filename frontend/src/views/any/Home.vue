<template>

  <v-progress-linear v-if="isLoading" indeterminate striped/>

  <v-tabs v-model="tab" :disabled="isLoading" align-tabs="center" stacked>
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

  <v-container v-if="isLoading">
    <v-row justify="space-evenly" justify-sm="center">
      <v-col v-for="n in 20" :key="n">
        <v-card :width="cardWidth" elevation="10" height="400" variant="tonal">
        </v-card>
      </v-col>
    </v-row>
  </v-container>

  <div v-else>
    <v-window v-model="tab">
      <v-window-item value="suppliers">
        <v-container>
          <v-row justify="space-evenly" justify-sm="center">
            <v-col v-for="workingSupplier in useWorkingSuppliersStore().getWorkingSuppliersLimited"
                   :key="workingSupplier.name"
                   cols="auto">
              <v-card :title="workingSupplier.name" :width="cardWidth" elevation="10"
                      variant="tonal"
                      @click="openSupplierProfile(workingSupplier)">
                <v-img
                  :height="useOrganizationStore().getLogoCardMaxHeight"
                  :lazy-src="ApiConstants.ANONYM_ORGANIZATION + '/logo' +
                              '?supplierId='+workingSupplier.supplierId +
                              '&width=' + 25 +
                              '&maxHeight='+ useOrganizationStore().getLogoCardMaxHeight"
                  :src="ApiConstants.ANONYM_ORGANIZATION + '/logo' +
                              '?supplierId='+workingSupplier.supplierId +
                              '&width=' + useOrganizationStore().getLogoCardWidth +
                              '&maxHeight='+ useOrganizationStore().getLogoCardMaxHeight"
                  :width="useOrganizationStore().getLogoCardWidth"
                  aspect-ratio="16/9"
                  cover
                  @error="supplierLogoLoaded()"
                  @load="supplierLogoLoaded()">
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
                <v-card-subtitle class="pt-4">Offers</v-card-subtitle>
                <v-sheet class="overflow-auto" color="background" height="150">
                  <v-card-text>
                    <v-chip-group class="disable-events">
                      <v-chip v-for="(tag) in workingSupplier.categoriesTags" :key="tag" label>
                        {{ tag }}
                      </v-chip>
                    </v-chip-group>
                  </v-card-text>
                </v-sheet>

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
import {computed, onMounted, ref} from "vue";
import {useWorkingSuppliersStore, WorkingSupplier} from "@/store/user-app";
import {Utils} from "@/models/Utils";
import {ApiConstants} from "@/services/ApiConstants";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import router from "@/router";

onMounted(() => {
  useWorkingSuppliersStore().requestFreshDataIfNothingCached().finally(() => {
    isLoading.value = false
  })
})

const isLoading = ref(true)
const tab = ref(1)
const cardWidth: number = 352
const searchFilterAppliedSuppliers = computed(() => useSysAdmOrganizationStore().getOrganizationsDetails)

function openSupplierProfile(workingSupplier: WorkingSupplier) {
  router.push({name: 'Anonymous Supplier', params: {supplierName: workingSupplier.name}})
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
