<template>
  <v-container>
    <v-row justify="center">
      <v-col v-for="(invitingCompany) in useInvitationStore().getInvitingCompanies"
             :key="invitingCompany.id" cols="4">
        <v-card :title="invitingCompany.name" elevation="20" max-width="400" variant="tonal">
          <v-progress-linear
            v-if="invitingCompany.logo === '' && invitingCompany.logoThumbnail !== ''"
            indeterminate/>
          <v-card-text>
            <v-img
              :lazy-src="`data:image/jpeg;base64,${invitingCompany.logoThumbnail}`"
              :max-height="useOrganizationStore().getLogoCardMaxHeight + 200"
              :max-width="useOrganizationStore().getLogoCardWidth + 100"
              :src="`data:image/jpeg;base64,${invitingCompany.logo}`"/>
          </v-card-text>

          <v-divider/>

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
  </v-container>
</template>

<script lang="ts" setup>
import {onBeforeMount} from "vue";

import {InvitingCompany, useInvitationStore, useProfileStore} from "@/store/user-app";
import toastManager from "@/services/ToastManager";
import router from "@/router";
import {RouterPaths} from "@/services/RouterPaths";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";

onBeforeMount(() => {
  useInvitationStore().requestFreshInvitationData().finally(() => useInvitationStore().requestCompaniesLogos())
})

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
