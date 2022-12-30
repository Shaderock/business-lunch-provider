<template>
  <v-row justify="center">
    <v-col align-self="center" lg="4">
      <v-form
        ref="userRegistrationForm"
        @submit.prevent="registerSubmit"
      >
        <v-card>
          <v-card-title>Register</v-card-title>

          <v-divider/>

          <v-card-item>
            <v-text-field
              v-model="firstName"
              :error-messages="firstNameErrors"
              counter="20"
              label="First Name"
              required
              type="text"
              @blur="validateFirstName"
              @keypress="validateFirstName"
            ></v-text-field>
          </v-card-item>

          <v-card-item>
            <v-text-field
              v-model="lastName"
              :error-messages="lastNameErrors"
              counter="20"
              label="Last Name"
              required
              type="text"
              @blur="validateLastName"
              @keypress="validateLastName"
            ></v-text-field>
          </v-card-item>

          <v-card-item>
            <v-text-field
              v-model="email"
              :error-messages="emailErrors"
              label="Email"
              required
              type="email"
              @blur="validateEmail"
              @keypress="validateEmail"
            ></v-text-field>
          </v-card-item>

          <v-card-item>
            <v-text-field
              v-model="password"
              :error-messages="passwordErrors"
              counter="25"
              label="Password"
              required
              type="password"
              @blur="validatePassword"
              @keypress="validatePassword"
            ></v-text-field>
          </v-card-item>

          <v-card-item>
            <v-text-field
              v-model="passwordRepeat"
              :error-messages="passwordRepeatErrors"
              label="Repeat Password"
              required
              type="password"
              @blur="validatePasswordRepeat"
              @keypress="validatePasswordRepeat"
            ></v-text-field>
          </v-card-item>

          <v-divider/>

          <v-card-actions>
            <v-btn color="primary" type="submit" variant="outlined">Sign up</v-btn>

            <v-btn v-bind:to="'/login'">Sign in</v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-col>
  </v-row>
</template>
<script lang="ts" setup>
import toastManager from "@/services/ToastManager";
import {ref} from "vue";
import * as yup from 'yup';
import authService from "@/services/AuthService";

const firstName = ref("")
const firstNameErrors = ref("")
const lastName = ref("")
const lastNameErrors = ref("")
const email = ref("")
const emailErrors = ref("")
const password = ref("")
const passwordErrors = ref("")
const passwordRepeat = ref("")
const passwordRepeatErrors = ref("")

async function validateFirstName() {
  try {
    await yup.string().required().label('First Name').validate(firstName.value)
    firstNameErrors.value = ""
  } catch (err: any) {
    firstNameErrors.value = err.message
  }
}

async function validateLastName() {
  try {
    await yup.string().required().label('Last Name').validate(lastName.value)
    lastNameErrors.value = ""
  } catch (err: any) {
    lastNameErrors.value = err.message
  }
}

async function validateEmail() {
  try {
    await yup.string()
      .required()
      .email()
      .label('Email')
      .validate(email.value);

    emailErrors.value = ""

    try {
      let response = await authService.verifyUserNotRegistered(email.value)
      if (!response.data) {
        emailErrors.value = "Email is already taken"
      }
    } catch (err: any) {
      emailErrors.value = "Couldn't verify if email is taken"
    }

  } catch (err: any) {
    emailErrors.value = err.message
  }
}

async function validatePassword() {
  try {
    await yup.string().required().min(8).max(25).label("Password").validate(password.value)
    passwordErrors.value = ""
  } catch (err: any) {
    passwordErrors.value = err.message
  }
}

async function validatePasswordRepeat() {
  try {
    const pass = [password.value]
    await yup.string()
      .required()
      .label("Password Repeat")
      .oneOf(pass, "Passwords do not match")
      .validate(passwordRepeat.value)

    passwordRepeatErrors.value = ""
  } catch (err: any) {
    passwordRepeatErrors.value = err.message
  }
}

async function registerSubmit() {
  await validateFirstName()
  await validateLastName()
  await validateEmail()
  await validatePassword()
  await validatePasswordRepeat()

  if (firstNameErrors.value === "" &&
    lastNameErrors.value === "" &&
    emailErrors.value === "" &&
    passwordErrors.value === "" &&
    passwordRepeatErrors.value === "") {
    await registerUser()
  }
}

async function registerCompany() {
  try {
    toastManager.showInfo('Registration', 'Registration in progress. Wait a second.')
    await authService.register(email.value, password.value, firstName.value, lastName.value)
    toastManager.showSuccess('Verification email',
      'We sent a verification email to your email address. Check it up to complete registration')
  } catch (error) {
    console.log('Something wrong happened during registration: ' + error)
    toastManager.showDefaultError("There was an error during registration request")
  }
}
</script>
