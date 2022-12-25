<template>
  <MDBRow center class="needs-validation" novalidate tag="form">
    <MDBCol auto>
      <!-- 2 column grid layout with text inputs for the first and last names -->
      <MDBRow :class="isValidated ? 'mb-5' : 'mb-4'">
        <MDBCol>
          <MDBInput
              id="firstName"
              v-model="firstName"
              :is-valid="isFirstNameValid"
              :is-validated="isValidated"
              :maxlength=20
              counter
              invalidFeedback="Please provide your first name"
              label="First name"
              required
              type="text"
              validFeedback="Looks good!"/>
        </MDBCol>
        <MDBCol>
          <MDBInput
              id="lastName"
              v-model="lastName"
              :is-valid="isLastNameValid"
              :is-validated="isValidated"
              :maxlength=20
              counter
              invalidFeedback="Please provide your last name"
              label="Last name"
              required
              type="text"
              validFeedback="Looks good!"
          />
        </MDBCol>
      </MDBRow>
      <!-- Email input -->
      <MDBInput
          id="email"
          v-model="email"
          :invalidFeedback="invalidEmailFeedback"
          :is-valid="isEmailValid"
          :is-validated="isValidated"
          :wrapperClass="isValidated ? 'mb-5' : 'mb-4'"
          label="Email address"
          required
          type="email"
          validFeedback="Looks good!"/>
      <!-- Password input -->
      <MDBInput
          id="password"
          v-model="password"
          :is-valid="isPasswordValid"
          :is-validated="isValidated"
          :maxlength=25
          :wrapperClass="isValidated ? 'mb-5' : 'mb-4'"
          counter
          helper="Minimum 8 characters"
          invalidFeedback="Password is too weak."
          label="Password"
          required
          type="password">
      </MDBInput>
      <!-- Repeat Password input -->

      <MDBInput
          id="PasswordRepeat"
          v-model="passwordRepeat"
          :is-valid="isPasswordRepeatValid"
          :is-validated="isValidated"
          :maxlength=25
          :wrapperClass="isValidated ? 'mb-5' : 'mb-4'"
          counter
          invalidFeedback="Passwords don't match or empty."
          label="Repeat password"
          required
          type="password"
          validFeedback="Looks good!"/>

      <!-- Submit button -->
      <MDBContainer class="w-50">
        <MDBBtn block color="primary" @click="submit">Sign up</MDBBtn>
      </MDBContainer>
    </MDBCol>
  </MDBRow>
</template>

<script lang="ts" setup>
import {MDBBtn, MDBCol, MDBContainer, MDBInput, MDBRow} from "mdb-vue-ui-kit";
import {useAuthStore} from "../stores/AuthStore";
import {computed, ref} from "vue";
import {ToastManager} from "../services/ToastManager";

const authStore = useAuthStore()

const firstName = ref("");
const lastName = ref("");
const email = ref("");
const password = ref("");
const passwordRepeat = ref("")
const isValidated = ref(false)
const invalidEmailFeedback = ref("")
const isEmailTaken = ref(false)

const isFirstNameValid = computed(() => {
  return firstName.value.length > 0
})

const isLastNameValid = computed(() => {
  return lastName.value.length > 0
})

const isEmailValid = computed(() => {
  const regexp: RegExp = /([-!#-'*+\/-9=?A-Z^-~]+(\.[-!#-'*+\/-9=?A-Z^-~]+)*|"([]!#-[^-~ \t]|(\\[\t -~]))+")@([-!#-'*+\/-9=?A-Z^-~]+(\.[-!#-'*+\/-9=?A-Z^-~]+)*|\[[\t -Z^-~]*])/
  if (!regexp.test(email.value)) {
    invalidEmailFeedback.value = 'Please provide your email'
    return false;
  }

  if (isEmailTaken.value) {
    invalidEmailFeedback.value = 'This email is already taken'
    return false;
  }

  return true;
})

const isPasswordValid = computed(() => {
  return password.value.length >= 8 && password.value.length < 25
})

const isPasswordRepeatValid = computed(() => {
  return password.value === passwordRepeat.value && passwordRepeat.value.length > 0
})

function submit() {
  isValidated.value = true
  isEmailTaken.value = false
  if (isFirstNameValid.value && isLastNameValid.value && isEmailValid.value && isPasswordValid.value && isPasswordRepeatValid.value) {
    register()
  }
}

async function register() {
  try {
    let isUserRegistered: boolean = await authStore.verifyUserRegistered(email.value)
    isEmailTaken.value = isUserRegistered
    if (!isUserRegistered) {
      let toastManager: ToastManager = new ToastManager()
      toastManager.showInfo('Registration', 'Registration in progress. Wait a second.')
      await authStore.register(email.value, password.value, firstName.value, lastName.value)
      toastManager.showSuccess('Verification email',
          'We sent a verification email to your email address. Check it up to complete registration')
    }
  } catch (error) {
    console.log('Something wrong happened during registration: ' + error)
    let toastManager: ToastManager = new ToastManager()
    toastManager.showError("There was an error during registration request")
  }
}
</script>
