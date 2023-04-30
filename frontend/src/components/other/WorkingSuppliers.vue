<template>
  <v-container>
    <v-row justify="space-evenly" justify-sm="center">
      <v-col
        v-for="workingSupplier in useWorkingSuppliersStore().getWorkingsSuppliersLimitedFiltered"
        :key="workingSupplier.name"
        cols="auto">
        <v-card :title="workingSupplier.name" :width="useOrganizationStore().getLogoCardWidth"
                elevation="10"
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
          <v-card-subtitle class="pt-4">
            Offers
            <v-icon icon="mdi-tag"/>
          </v-card-subtitle>
          <v-sheet class="overflow-auto" color="background" height="150">
            <v-card-text>
              <v-chip-group class="disable-events">
                <v-chip v-for="(tag) in workingSupplier.categoriesTags" :key="tag" label>
                  {{ tag }}
                </v-chip>
              </v-chip-group>
            </v-card-text>
          </v-sheet>
          <v-card-subtitle>
            Working hours
            <v-icon icon="mdi-clock"/>
          </v-card-subtitle>
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
</template>

<script lang="ts" setup>

import {useWorkingSuppliersStore, WorkingSupplier} from "@/store/user-app";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import {ApiConstants} from "@/services/ApiConstants";
import {Utils} from "@/models/Utils";
import router from "@/router";

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
