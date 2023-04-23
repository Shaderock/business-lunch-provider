<template>

  <v-row justify="center">
    <v-col v-for="(invitingCompany) in invitingCompanies" :key="invitingCompany.id" cols="4">
      <v-card :title="invitingCompany.name" elevation="20" max-width="400">
        <v-list>
          <v-list-item :subtitle="invitingCompany.email" prepend-icon="mdi-email" title="Email"/>
          <v-divider inset/>

          <v-list-item :subtitle="invitingCompany.phone" prepend-icon="mdi-phone" title="Phone"/>
          <v-divider inset/>

          <v-list-item :subtitle="invitingCompany.formattedCreatedAt || ''"
                       prepend-icon="mdi-calendar" title="Invitation Date"/>
          <v-divider inset/>

          <v-list-subheader title="Description"/>
          <v-container>
            {{ invitingCompany.description }}
          </v-container>

        </v-list>

        <v-card-actions>
          <v-btn color="primary" prepend-icon="mdi-checkbox-marked-circle-outline"
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
