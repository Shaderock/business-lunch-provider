<template>

  <v-row justify="center">
    <v-col v-for="(invitingCompany) in invitingCompanies" :key="invitingCompany.id">
      <v-card elevation="20" max-width="400">
        <v-card-title>{{ invitingCompany.name }}</v-card-title>
        <v-card-subtitle>Email</v-card-subtitle>
        <v-card-text>{{ invitingCompany.email }}</v-card-text>
        <v-card-subtitle>Phone</v-card-subtitle>
        <v-card-text>{{ invitingCompany.phone }}</v-card-text>
        <v-card-subtitle>Invitation Date</v-card-subtitle>
        <v-card-text>{{ invitingCompany.formattedCreatedAt }}</v-card-text>
        <v-card-subtitle>Description</v-card-subtitle>
        <v-card-text>{{ invitingCompany.description }}</v-card-text>
        <v-card-actions>
          <v-btn color="primary" prepend-icon="mdi-checkbox-marked-circle-outline" variant="tonal"
                 @click="acceptInvitation(invitingCompany)">Accept
          </v-btn>
          <v-btn color="error" prepend-icon="mdi-delete"
                 @click="declineInvitation(invitingCompany)">Decline
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>

</template>

<script lang="ts" setup>
import {computed, ComputedRef, onMounted} from "vue";

import {InvitingCompany, useInvitationStore, useProfileStore} from "@/store/user-app";
import toastManager from "@/services/ToastManager";
import router from "@/router";
import {RouterPaths} from "@/services/RouterPaths";

onMounted(() => {
  useInvitationStore().requestFreshInvitationData()
})

const invitingCompanies: ComputedRef<InvitingCompany[]> = computed(() => useInvitationStore().getInvitingCompanies)

async function acceptInvitation(invitingCompany: InvitingCompany) {
  try {
    await useInvitationStore().accept(invitingCompany.id)
    toastManager.showSuccess("Accepted!", `You are now a part of the company`)
    await useProfileStore().requestUserData()
    await router.push(RouterPaths.EMPLOYEE_OR_SUPPLIER_ORGANIZATION_DETAILS)
  } catch (error) {
    console.log("Error during invitation accepting")
  }
}

async function declineInvitation(invitingCompany: InvitingCompany) {
  try {
    await useInvitationStore().decline(invitingCompany.id)
  } catch (error) {
    console.log("Error during invitation accepting")
  }
}
</script>
