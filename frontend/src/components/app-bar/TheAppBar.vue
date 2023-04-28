<template>
  <v-app-bar>
    <v-app-bar-title>
      <v-btn v-bind:to="RouterPaths.ANONYMOUS_HOME" variant="plain">
        <v-icon color="primary" size="x-large" start>
          mdi-food-croissant
        </v-icon>
        Business lunch provider
      </v-btn>
    </v-app-bar-title>

    <v-spacer/>

    <div v-if="useAuthStore().isAuthenticated">
      <v-btn icon
             v-bind:to="RouterPaths.USER_NOTIFICATIONS">
        <v-icon>mdi-bell</v-icon>
      </v-btn>

      <v-btn v-if="useProfileStore().isEmployee"
             :color="useCartStore().isBlinking ? 'success' : ''"
             icon
             variant="plain"
             v-bind:to="RouterPaths.EMPLOYEE_CART">
        <v-fade-transition>

          <v-badge v-if="useCartStore().getCartOptions.length > 0 &&!useCartStore().isBlinking"
                   :content="useCartStore().getCartOptions.length"
                   color="red"
                   inline>
            <v-icon icon="mdi-cart"/>
          </v-badge>

          <v-icon v-else-if="!useCartStore().isBlinking" icon="mdi-cart"/>
        </v-fade-transition>
      </v-btn>

      <v-btn id="profile-actions"
             color="primary"
             variant="tonal">
        <v-icon start>mdi-account-circle</v-icon>
        Profile
        <v-icon end>mdi-menu-down</v-icon>
      </v-btn>

      <v-menu activator="#profile-actions">
        <v-list>
          <v-list-item append-icon="mdi-card-account-details"
                       v-bind:to="RouterPaths.USER_PROFILE">
            Details
          </v-list-item>

          <div v-if="useProfileStore().isEmployee">
            <v-list-subheader>Settings</v-list-subheader>

            <v-list-item
              append-icon="mdi-store-cog"
              v-bind:to="RouterPaths.EMPLOYEE_PREFERENCES_CONFIG">
              Preferences
            </v-list-item>

            <v-list-item
              append-icon="mdi-bell-cog"
              v-bind:to="RouterPaths.EMPLOYEE_NOTIFICATIONS_CONFIG">
              Notifications
            </v-list-item>
          </div>

          <v-divider/>

          <v-list-item append-icon="mdi-logout" variant="plain" @click="logout()">
            Sign out
          </v-list-item>
        </v-list>
      </v-menu>


    </div>

    <div v-else>
      <v-btn
        color="primary"
        v-bind:to="RouterPaths.ANONYMOUS_LOGIN"
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
        <v-list-item
          v-if="useProfileStore().isOnlyAppUser"
          append-icon="mdi-domain"
          title="Register Organization"
          @click="showOrganizationRegistrationDialog = true"
        />
      </v-list>
    </v-menu>
    <OrganizationRegistrationDialog/>
  </v-app-bar>
</template>
<script lang="ts" setup>
import {useAuthStore, useProfileStore} from "@/store/user-app";
import toastManager from "@/services/ToastManager";
import router from "@/router";
import {useTheme} from "vuetify";
import {RouterPaths} from "@/services/RouterPaths";
import {provide, Ref, ref, UnwrapRef} from "vue";
import OrganizationRegistrationDialog from "@/views/anonymous/OrganizationRegistrationDialog.vue";
import {useCartStore} from "@/store/employee-app";

const theme = useTheme();
const showOrganizationRegistrationDialog: Ref<UnwrapRef<boolean>> = ref(false)
provide('showOrganizationRegistrationDialog', showOrganizationRegistrationDialog)

function switchTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

function logout() {
  useAuthStore().logout();
  router.push(RouterPaths.ANONYMOUS_HOME);
  toastManager.showSuccess("Good Bye!", "You were signed out")
}
</script>
