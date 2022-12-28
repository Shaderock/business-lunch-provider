<template>
  <MDBContainer>
    <MDBRow center>
      <MDBCol auto>
        <MDBCard>
          <MDBCardHeader>
            <MDBCardTitle>Login</MDBCardTitle>
          </MDBCardHeader>
          <MDBCardBody>
            <form>
              <!-- Email input -->
              <MDBInput
                  id="email"
                  v-model="email"
                  label="Email address"
                  type="email"
                  wrapperClass="mb-4"/>
              <!-- Password input -->
              <MDBInput
                  id="password"
                  v-model="password"
                  label="Password"
                  type="password"
                  wrapperClass="mb-4"/>
              <!-- 2 column grid layout for inline styling -->
              <!-- Submit button -->
              <MDBBtn block color="primary" @click="login">Sign in</MDBBtn>

              <!-- Register buttons -->
              <div class="text-center mt-2">
                <p>Not a member?
                  <router-link to="/register">Register</router-link>
                </p>
              </div>
            </form>
          </MDBCardBody>
        </MDBCard>
      </MDBCol>
    </MDBRow>
  </MDBContainer>
</template>

<script lang="ts" setup>
import {
  MDBBtn,
  MDBCard,
  MDBCardBody,
  MDBCardHeader,
  MDBCardTitle,
  MDBCol,
  MDBContainer,
  MDBInput,
  MDBRow
} from "mdb-vue-ui-kit";
import {onMounted, ref} from "vue";
import {useAuthStore} from "../stores/AuthStore";
import {ToastManager} from "../services/ToastManager";
import {useRouter} from "vue-router";

const authStore = useAuthStore()
const toastManager: ToastManager = new ToastManager()
const router = useRouter()

onMounted(() => {
  if (router.currentRoute.value.query.emailConfirmed) {
    toastManager.showSuccess("Registration complete!", "Your email has been verified")
  }
})

async function login() {
  try {
    await authStore.login(email.value, password.value)
    await router.push({name: 'Home'})
    if (authStore.isAuthenticated)
      toastManager.showSuccess("Login complete", "You are in the system. Enjoy!")
    else
      toastManager.showError("Sign in problem", "Couldn't login. Try again later.")
  } catch (error) {
    console.log("Something wrong happened during login: ", error)
  }
}

const email = ref("");
const password = ref("");
</script>
