<template>
  <v-row justify="center">
    <v-col cols="auto">
      <v-data-table
        :class="`elevation-20`">
        <template v-slot:top>
          <v-toolbar>
            <v-toolbar-title>Invitations</v-toolbar-title>
            <v-btn color="primary">Invite User</v-btn>
          </v-toolbar>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

<script lang="ts" setup>

import {onMounted} from "vue";
import {useInvitationStore} from "@/store/company-adm-app";

// todo add loader
onMounted(() => {
  useInvitationStore().requestFreshInvitationData()
})
</script>
