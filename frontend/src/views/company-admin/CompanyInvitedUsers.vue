<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="auto">
        <v-data-table
          :class="`elevation-20`"
          :headers="headers"
          :items="invitations">
          <template v-slot:top>
            <v-toolbar>
              <v-toolbar-title>Invitation</v-toolbar-title>
              <v-btn append-icon="mdi-account-plus" color="primary"
                     @click="showInviteDialog = true">
                Invite user
              </v-btn>
            </v-toolbar>
          </template>

          <template v-slot:item.actions="{ item }">
            <v-btn icon size="small" variant="plain">
              <v-icon color="error" icon="mdi-delete"/>
              <v-menu activator="parent">
                <v-card variant="tonal">
                  <v-card-title>Are you sure to dismiss invitation for {{ item.raw.email }}?
                  </v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="dismissInvitation(item.raw)">
                      Dismiss invitation
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>
          </template>
        </v-data-table>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col>
        <v-dialog v-model="showInviteDialog">
          <v-row justify="center">
            <v-col cols="10" lg="3" md="5" sm="8">
              <v-card>
                <v-card-title>Invitation</v-card-title>
                <v-card-text>
                  <v-text-field
                    v-model="invitationUserEmail"
                    :rules="[email => !!email || 'Required field.']"
                    hint="example@example.com"
                    label="User Email"
                  />
                </v-card-text>
                <v-card-actions>
                  <v-btn class="flex-grow-1" color="primary" variant="outlined"
                         @click="sendInvitation()">
                    Send Invitation
                  </v-btn>
                  <v-btn color="secondary" variant="plain" @click="showInviteDialog = false">Cancel
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </v-dialog>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>

import {computed, ComputedRef, onMounted, ref} from "vue";
import {useCompAdmInvitationStore} from "@/store/company-adm-app";
import {Invitation} from "@/models/Invitation";

//todo add loader
onMounted(() => {
  useCompAdmInvitationStore().requestFreshInvitationData()
})

const showInviteDialog = ref(false)
const invitationUserEmail = ref('')
const invitations: ComputedRef<Invitation[]> = computed(() => useCompAdmInvitationStore().getInvitations)
const headers = [
  {title: 'Email', key: 'userEmail'},
  {title: 'Invitation Date', key: 'formattedCreatedAt'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

async function sendInvitation() {
  await useCompAdmInvitationStore().send(invitationUserEmail.value)
  showInviteDialog.value = false
}

function dismissInvitation(invitation: Invitation) {
  useCompAdmInvitationStore().dismissInvitation(invitation.userEmail)
}
</script>
