<template>
  <v-dialog v-model="show">
    <v-container>
      <v-row justify="center">
        <v-col cols="12" lg="4" md="6" sm="8">
          <v-form
            ref="companyRegistrationForm"
            @submit.prevent="registerSubmit()">
            <v-card>
              <v-card-title class="text-center">Register Organization</v-card-title>

              <v-card-item>
                <v-text-field
                  v-model="name"
                  :error-messages="nameErrors"
                  :label="isSupplier ? 'Supplier Name' : 'Company Name'"
                  counter="35"
                  required
                  type="text"
                  @blur="validateName"
                  @keypress="validateName"
                />
              </v-card-item>

              <v-card-item>
                <v-switch
                  v-model="isSupplier"
                  color="secondary"
                  inset
                  label="Register as Supplier"
                />
              </v-card-item>

              <v-card-actions class="justify-center">
                <v-btn color="primary" type="submit" variant="outlined">
                  Register Organization
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
  </v-dialog>
</template>
<script lang="ts" setup>
import toastManager from "@/services/ToastManager";
import {inject, Ref, ref, UnwrapRef} from "vue";
import * as yup from 'yup';
import router from "@/router";
import {useProfileStore} from "@/store/user-app";
import organizationService from "@/services/OrganizationService";
import {RouterPaths} from "@/services/RouterPaths";
import supplierService from "@/services/SupplierService";
import companyService from "@/services/CompanyService";

const name: Ref<UnwrapRef<string>> = ref("")
const nameErrors: Ref<UnwrapRef<string>> = ref("")
const show: Ref<UnwrapRef<boolean>> | undefined = inject('showOrganizationRegistrationDialog')
const isSupplier: Ref<UnwrapRef<boolean>> = ref(false)

async function validateName() {
  try {
    await yup.string().required().label('Name').validate(name.value)
    nameErrors.value = ""

    try {
      let response = await organizationService.validateOrganizationName(name.value)
      if (!response.data) {
        nameErrors.value = "Name is already taken"
      }
    } catch (err: any) {
      nameErrors.value = "Couldn't verify if name is taken"
    }
  } catch (err: any) {
    nameErrors.value = err.message
  }
}

async function registerSubmit() {
  await validateName()

  if (nameErrors.value === "") {
    await RegisterOrganization()
  }
}

async function RegisterOrganization() {
  try {
    if (isSupplier.value) {
      await supplierService.register(name.value)
    } else {
      await companyService.register(name.value)
    }

    await useProfileStore().requestUserData()
    await router.push(RouterPaths.EMPLOYEE_OR_SUPPLIER_ORGANIZATION_DETAILS)

    if (show) show.value = false

    toastManager.showSuccess('Successful organization registration', 'Your organization has just been created')
  } catch (error) {
    console.log('Something wrong happened during company registration: ' + error)
    toastManager.showDefaultError("There was an error during company registration request")
  }
}
</script>
