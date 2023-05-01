<template>
  <v-tabs v-model="tab" :disabled="isLoading" align-tabs="center" stacked>
    <v-tab v-if="useProfileStore().isEmployee" color="primary" value="subscribed"
           @click="useWorkingSuppliersStore().setShowSubscribed(true)">
      <v-icon icon="mdi-star"/>
      Subscribed
    </v-tab>
    <v-tab color="primary" value="suppliers"
           @click="useWorkingSuppliersStore().setShowSubscribed(false)">
      <v-icon icon="mdi-food"/>
      Suppliers
    </v-tab>
    <v-tab color="primary" value="favorites"
           @click="useWorkingSuppliersStore().setShowSubscribed(false)">
      <v-icon icon="mdi-heart"/>
      Favorites
    </v-tab>
    <v-tab color="primary" value="filters">
      <v-icon icon="mdi-filter"/>
      Filters
    </v-tab>
  </v-tabs>

  <v-progress-linear v-if="isLoading" indeterminate striped/>

  <v-container v-if="isLoading">
    <v-row justify="space-evenly" justify-sm="center">
      <v-col v-for="n in 20" :key="n">
        <v-card :width="useOrganizationStore().getLogoCardWidth" elevation="10" height="400"
                variant="tonal">
        </v-card>
      </v-col>
    </v-row>
  </v-container>

  <div v-else>
    <v-window v-model="tab">
      <v-window-item value="suppliers">
        <WorkingSuppliers/>
      </v-window-item>
      <v-window-item value="subscribed">
        <WorkingSuppliers/>
      </v-window-item>
      <v-window-item value="favorites">

      </v-window-item>
      <v-window-item value="filters">
        <!--        todo search with <v-autocomplete>-->
      </v-window-item>
    </v-window>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useProfileStore, useWorkingSuppliersStore} from "@/store/user-app";
import WorkingSuppliers from "@/components/other/WorkingSuppliers.vue";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";

onMounted(() => {
  useWorkingSuppliersStore().requestFreshDataIfNothingCached().finally(() => {
    if (useProfileStore().isEmployee) {
      tab.value = "subscribed"
      useWorkingSuppliersStore().setShowSubscribed(true)
    } else {
      tab.value = "suppliers"
    }
    isLoading.value = false
  })
})

const isLoading = ref(true)
const tab = ref('')
</script>
