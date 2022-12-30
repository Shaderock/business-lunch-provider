<template>
  <v-row justify="start">
    <v-col align-self="center" lg="4" xs="11">
      <v-card>
        <v-card-title>My Company</v-card-title>

        <v-divider/>

        <v-card-item>
          <v-list>
            <v-list-item-subtitle>Name</v-list-item-subtitle>
            <v-list-item>{{ useCompanyStore().getCompany.name }}</v-list-item>
            <v-list-item-subtitle>Email</v-list-item-subtitle>
            <v-list-item>{{ useCompanyStore().getCompany.email }}</v-list-item>
            <v-list-item-subtitle>Phone</v-list-item-subtitle>
            <v-list-item>{{ useCompanyStore().getCompany.phone }}</v-list-item>
          </v-list>
        </v-card-item>

      </v-card>
    </v-col>
  </v-row>
</template>
<script lang="ts" setup>
import {onMounted} from "vue";
import {useCompanyStore} from "@/store/app";

onMounted(() => {
  useCompanyStore().requestData()
})
</script>
