<template>
  <v-app-bar :elevation="3">
    <v-app-bar-title>
      Business Lunch Provider
    </v-app-bar-title>

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
