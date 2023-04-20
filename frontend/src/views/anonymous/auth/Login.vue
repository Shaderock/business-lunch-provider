<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" md="4" sm="8">
        <v-card class="elevation-12" shaped variant="outlined">
          <v-card-title class="text-h5 text-center">Login</v-card-title>
          <v-card-text>
            <v-form>
              <v-text-field
                v-model="email"
                label="Email"
                placeholder="example@example.com"
                required
                type="email"
              ></v-text-field>
              <v-text-field
                v-model="password"
                label="Password"
                placeholder="Enter your password"
                required
                type="password"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions class="justify-center">
            <v-btn block color="primary" @click="login()">Login</v-btn>
          </v-card-actions>
          <v-card-text class="text-center">
            <v-col>
              <div>
                Don't have an account?
                <v-btn color="secondary"
                       size="small"
                       v-bind:to="RouterPaths.ANONYMOUS_REGISTER"
                       variant="plain">Sign up
                </v-btn>
              </div>
              <div>
                Forgot your password?
                <v-btn color="secondary" size="small" variant="plain">Reset it</v-btn>
              </div>
            </v-col>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

</template>
<script lang="ts" setup>
import {onMounted, ref} from "vue";
import router from "@/router";
import toastManager from "@/services/ToastManager";
import {useAuthStore} from "@/store/app";
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
    await router.push({path: '/'})
  } catch (error) {
    console.log("Something wrong happened during login: ", error)
  }

  if (useAuthStore().isAuthenticated)
    toastManager.showSuccess("Login complete", "You are in the system. Enjoy!")
  else
    toastManager.showError("Sign in problem", "Couldn't login. Try again later.")
}
</script>
