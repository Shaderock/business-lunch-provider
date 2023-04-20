<template>
  <v-row justify="center">
    <v-col align-self="center" lg="4">
      <v-form
        ref="companyRegistrationForm"
        @submit.prevent="registerSubmit()"
      >
        <v-card>
          <v-card-title>Register</v-card-title>

          <v-card-item>
            <v-text-field
              v-model="name"
              :error-messages="nameErrors"
              counter="35"
              label="Company Name"
              required
              type="text"
              @blur="validateName"
              @keypress="validateName"
            ></v-text-field>
          </v-card-item>

          <v-card-actions>
            <v-btn color="primary" type="submit" variant="outlined">Register Company</v-btn>
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
import companyService from "@/services/CompanyService";
import router from "@/router";
import {useUserStore} from "@/store/app";

const name = ref("")
const nameErrors = ref("")
const email = ref("")
const emailErrors = ref("")
const phone = ref("")
const phoneErrors = ref("")

async function validateName() {
  try {
    await yup.string().required().label('Name').validate(name.value)
    nameErrors.value = ""

    try {
      let response = await companyService.validateOrganizationName(name.value)
      if (!response.data) {
        emailErrors.value = "Name is already taken"
      }
    } catch (err: any) {
      emailErrors.value = "Couldn't verify if name is taken"
    }
  } catch (err: any) {
    nameErrors.value = err.message
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
      let response = await companyService.validateOrganizationEmail(email.value)
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

async function validatePhone() {
  try {
    await yup.string().required().matches(/^\+?[1-9]\d{7,14}$/).label("Phone (e.g. +37312345678)").validate(phone.value)
    phoneErrors.value = ""
  } catch (err: any) {
    phoneErrors.value = err.message
  }
}


async function registerSubmit() {
  await validateName()
  await validateEmail()
  await validatePhone()

  if (nameErrors.value === "" &&
    emailErrors.value === "" &&
    phoneErrors.value === "") {
    await registerCompany()
  }
}

async function registerCompany() {
  try {
    await companyService.register(email.value, name.value, phone.value)
    await useUserStore().requestUserData()
    await router.push("/company")
    toastManager.showSuccess('Successful company registration', 'Your company has just been created')
  } catch (error) {
    console.log('Something wrong happened during company registration: ' + error)
    toastManager.showDefaultError("There was an error during company registration request")
  }
}
</script>
