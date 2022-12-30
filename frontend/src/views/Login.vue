<template>
  <v-row justify="center">
    <v-col align-self="center" lg="4">
      <v-form ref="loginForm" @submit="login">
        <v-card>
          <v-card-title>Login</v-card-title>

          <v-divider/>

          <v-card-item>
            <v-text-field
              v-model="email"
              label="Email"
              required
            >
            </v-text-field>
          </v-card-item>
          <v-card-item>
            <v-text-field
              v-model="password"
              label="Password"
              required
              type="password">
            </v-text-field>
          </v-card-item>

          <v-divider/>

          <v-card-actions>
            <v-btn color="primary" variant="outlined" @click="login">Sign in</v-btn>

            <v-btn v-bind:to="'/registration'">Sign up</v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-col>
  </v-row>
</template>
<script lang="ts" setup>
import {onMounted, ref} from "vue";
import router from "@/router";
import toastManager from "@/services/ToastManager";
import {useAuthStore} from "@/store/app";

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
    await router.push("/")
  } catch (error) {
    console.log("Something wrong happened during login: ", error)
  }

  if (useAuthStore().isAuthenticated)
    toastManager.showSuccess("Login complete", "You are in the system. Enjoy!")
  else
    toastManager.showError("Sign in problem", "Couldn't login. Try again later.")
}
</script>
