<template>
  <v-card class="mx-auto">
    <v-app-bar :elevation="5">

      <v-app-bar-title>
        <v-btn v-bind:to="'/'" variant="plain">
          <v-icon color="primary" size="x-large" start>
            mdi-food-croissant
          </v-icon>
          Business lunch provider
        </v-btn>
      </v-app-bar-title>

      <v-spacer/>

      <div v-if="useAuthStore().isAuthenticated">
        <v-btn icon>
          <v-icon>mdi-bell</v-icon>
        </v-btn>

        <v-btn icon>
          <v-icon>mdi-cart</v-icon>
        </v-btn>

        <v-btn id="profile-actions" color="primary"
               variant="tonal">
          <v-icon start>
            mdi-account-circle
          </v-icon>
          Profile
          <v-icon end>
            mdi-menu-down
          </v-icon>
        </v-btn>

        <v-menu activator="#profile-actions">
          <v-list>
            <v-list-item append-icon="mdi-card-account-details"
                         v-bind:to="'/details'">
              Details
            </v-list-item>
            <v-list-subheader>Settings</v-list-subheader>
            <v-list-item append-icon="mdi-store-cog"
                         v-bind:to="'/preferences'">
              Preferences
            </v-list-item>
            <v-list-item append-icon="mdi-bell-cog"
                         v-bind:to="'/notifications'">
              Notifications
            </v-list-item>
            <v-divider></v-divider>
            <v-list-item append-icon="mdi-logout" variant="plain" @click="logout()">
              Sign out
            </v-list-item>
          </v-list>
        </v-menu>


      </div>

      <div v-else>
        <v-btn
          color="primary"
          v-bind:to="'/login'"
          variant="outlined">
          <v-icon start>mdi-login</v-icon>
          Sign in
        </v-btn>
      </div>

      <v-btn id="dots-actions" icon>
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>

      <v-menu activator="#dots-actions">
        <v-list>
          <v-list-item append-icon="mdi-theme-light-dark" @click="switchTheme()">
            Switch theme
          </v-list-item>

          <v-list-subheader v-if="useUserStore().isOnlyAppUser">
            Register organization
          </v-list-subheader>

          <v-list-item
            v-if="useUserStore().isOnlyAppUser"
            append-icon="mdi-silverware"
            title="Become a Company"
            v-bind:to="'/company/registration'"
            value="registerCompany"
          ></v-list-item>

          <v-list-item
            v-if="useUserStore().isOnlyAppUser"
            append-icon="mdi-food"
            title="Become a Supplier"
            v-bind:to="'/supplier/registration'"
            value="registerSupplier"
          ></v-list-item>

        </v-list>
      </v-menu>

    </v-app-bar>
  </v-card>
</template>
<script lang="ts" setup>
import {useAuthStore, useUserStore} from "@/store/app";
import toastManager from "@/services/ToastManager";
import router from "@/router";
import {useTheme} from "vuetify";

const theme = useTheme();

function switchTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

function logout() {
  useAuthStore().logout();
  router.push('/');
  toastManager.showSuccess("Good Bye!", "You were signed out")
}
</script>
