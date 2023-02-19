<template>
  <v-app-bar :elevation="3">
    <v-btn>
      <v-icon size="x-large">mdi-menu</v-icon>
    </v-btn>

    <v-divider vertical></v-divider>

    <v-app-bar-title>Business Lunch Provider</v-app-bar-title>

    <v-spacer/>

    <v-btn
      v-if="useAuthStore().isAuthenticated"
      prepend-icon="mdi-account-circle"
      variant="outlined"
    >
      Profile
    </v-btn>
    <v-btn
      v-if="useAuthStore().isAuthenticated"
      @click="logout"
    >
      Sign out
    </v-btn>
    <v-btn
      v-if="!useAuthStore().isAuthenticated"
      color="primary"
      v-bind:to="'/login'"
      variant="outlined"
    >
      Sign in
    </v-btn>

    <v-divider vertical/>

    <v-btn v-if="useAuthStore().isAuthenticated">
      <v-icon>mdi-dots-vertical</v-icon>
    </v-btn>
  </v-app-bar>
</template>
<script lang="ts" setup>
import {useAuthStore} from "@/store/app";
import toastManager from "@/services/ToastManager";
import router from "@/router";

function logout() {
  useAuthStore().logout();
  router.push('/');
  toastManager.showSuccess("Good Bye!", "You were signed out")
}
</script>
