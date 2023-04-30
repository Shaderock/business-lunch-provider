<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="auto">
        <v-card class="mx-auto" elevation="20" shaped variant="tonal">
          <v-card-title class="text-h5 text-center">Login</v-card-title>
          <v-divider/>
          <v-card-text>
            <v-form>
              <v-text-field
                v-model="email"
                label="Email"
                placeholder="example@example.com"
                required
                type="email"
              />
              <v-text-field
                v-model="password"
                label="Password"
                placeholder="Enter your password"
                required
                type="password"
              />
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-btn class="flex-grow-1"
                   color="primary"
                   variant="outlined"
                   @click="login()">
              Login
            </v-btn>

            <v-btn append-icon="mdi-lock-reset"
                   color="secondary"
                   size="small"
                   variant="plain">
              Reset password
            </v-btn>

            <v-btn append-icon="mdi-account-plus"
                   color="secondary"
                   size="small"
                   v-bind:to="RouterPaths.ANONYMOUS_REGISTER"
                   variant="plain">
              Register
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<script lang="ts" setup>
import {onMounted, ref} from "vue";
import router from "@/router";
import toastManager from "@/services/ToastManager";
import {useAuthStore} from "@/store/user-app";
import {RouterPaths} from "@/services/RouterPaths";

const email = ref("")
const password = ref("")

onMounted(() => {
  if (router.currentRoute.value.query.emailConfirmed) {
    toastManager.showSuccess("Registration complete!", "Your email has been verified")
  }
})

async function login() {
  try {
    await useAuthStore().login(email.value, password.value)
  } catch (error) {
    console.log("Something wrong happened during login: ", error)
  }

  if (useAuthStore().isAuthenticated) {
    toastManager.showSuccess("Login complete", "You are in the system. Enjoy!")
    await router.push(RouterPaths.ANONYMOUS_HOME)
  } else {
    toastManager.showError("Sign in problem", "Couldn't login. Try again later.")
  }

}
</script>
