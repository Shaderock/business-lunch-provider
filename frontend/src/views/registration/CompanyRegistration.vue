<template>
  <v-row justify="center">
    <v-col align-self="center" lg="4">
      <v-form
        ref="companyRegistrationForm"
        @submit.prevent="registerSubmit"
      >
        <v-card>
          <v-card-title>Register</v-card-title>

          <v-divider/>

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

          <v-card-item>
            <v-text-field
              v-model="email"
              :error-messages="emailErrors"
              label="Company Email"
              required
              type="email"
              @blur="validateEmail"
              @keypress="validateEmail"
            ></v-text-field>
          </v-card-item>

          <v-card-item>
            <v-text-field
              v-model="phone"
              :error-messages="phoneErrors"
              hint="+37312345678"
              label="Phone"
              required
              type="tel"
              @blur="validatePhone"
              @keypress="validatePhone"
            ></v-text-field>
          </v-card-item>

          <v-divider/>

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

<!--<template>-->
<!--  <MDBRow center class="needs-validation" novalidate tag="form">-->
<!--    <MDBCol auto>-->
<!--      <MDBCard style="width: 35rem" border="info">-->
<!--        <MDBCardHeader>-->
<!--          <MDBCardTitle>Company Registration</MDBCardTitle>-->
<!--        </MDBCardHeader>-->
<!--        <MDBCardBody>-->
<!--          <MDBInput-->
<!--              id="name"-->
<!--              v-model="name"-->
<!--              :invalidFeedback="invalidNameFeedback"-->
<!--              :is-valid="isNameValid"-->
<!--              :is-validated="isValidated"-->
<!--              :wrapperClass="isValidated ? 'mb-5' : 'mb-4'"-->
<!--              label="Company name"-->
<!--              required-->
<!--              type="name">-->
<!--          </MDBInput>-->
<!--          <MDBInput-->
<!--              id="phone"-->
<!--              v-model="phone"-->
<!--              :helper="isValidated ? '' : '+37312345678'"-->
<!--              :is-valid="isPhoneValid"-->
<!--              :is-validated="isValidated"-->
<!--              :wrapperClass="isValidated ? 'mb-5' : 'mb-4'"-->
<!--              invalid-feedback="Please provide a contact phone (e.g. +37312345678)"-->
<!--              label="Company contact phone"-->
<!--              required-->
<!--              type="tel"-->
<!--              validFeedback="Looks good!"/>-->
<!--          <MDBInput-->
<!--              id="email"-->
<!--              v-model="email"-->
<!--              :invalidFeedback="invalidEmailFeedback"-->
<!--              :is-valid="isEmailValid"-->
<!--              :is-validated="isValidated"-->

<!--              label="Company Email"-->
<!--              required-->
<!--              type="email"-->
<!--              validFeedback="Looks good!"/>-->
<!--        </MDBCardBody>-->
<!--        <MDBCardFooter border="info">-->
<!--          <MDBContainer class="w-50">-->
<!--            <MDBBtn block color="primary" @click="submit">Register company</MDBBtn>-->
<!--          </MDBContainer>-->
<!--        </MDBCardFooter>-->
<!--      </MDBCard>-->
<!--    </MDBCol>-->
<!--  </MDBRow>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import {-->
<!--  MDBBtn,-->
<!--  MDBCard,-->
<!--  MDBCardBody,-->
<!--  MDBCardFooter,-->
<!--  MDBCardHeader,-->
<!--  MDBCardTitle,-->
<!--  MDBCol,-->
<!--  MDBContainer,-->
<!--  MDBInput,-->
<!--  MDBRow-->
<!--} from "mdb-vue-ui-kit";-->
<!--import {computed, ref} from "vue";-->
<!--import {useAuthStore} from "../../stores/AuthStore";-->
<!--import {ToastManager} from "../../services/ToastManager";-->
<!--import {useCompanyStore} from "../../stores/CompanyStore";-->
<!--import {CompanyService} from "../../services/CompanyService";-->
<!--import router from "../../router";-->
<!--import {useUserStore} from "../../stores/UserStore";-->

<!--const authStore = useAuthStore()-->
<!--const userStore = useUserStore()-->
<!--const companyStore = useCompanyStore()-->
<!--const companyService = new CompanyService()-->
<!--const toastManager = new ToastManager()-->

<!--const email = ref("");-->
<!--const invalidEmailFeedback = ref("")-->
<!--const isEmailTaken = ref(false)-->
<!--const name = ref("");-->
<!--const invalidNameFeedback = ref("")-->
<!--const isNameTaken = ref(false)-->
<!--const phone = ref("")-->
<!--const isValidated = ref(false)-->

<!--const isEmailValid = computed(() => {-->
<!--  const regexp: RegExp = /([-!#-'*+\/-9=?A-Z^-~]+(\.[-!#-'*+\/-9=?A-Z^-~]+)*|"([]!#-[^-~ \t]|(\\[\t -~]))+")@([-!#-'*+\/-9=?A-Z^-~]+(\.[-!#-'*+\/-9=?A-Z^-~]+)*|\[[\t -Z^-~]*])/-->
<!--  if (!regexp.test(email.value)) {-->
<!--    invalidEmailFeedback.value = "Please provide an email"-->
<!--    return false-->
<!--  }-->
<!--  if (isEmailTaken.value) {-->
<!--    invalidEmailFeedback.value = "The email is already taken"-->
<!--    return false;-->
<!--  }-->
<!--  return true;-->
<!--})-->

<!--const isNameValid = computed(() => {-->
<!--  if (name.value.length < 1) {-->
<!--    invalidNameFeedback.value = "Please provide a name"-->
<!--    return false-->
<!--  }-->
<!--  if (isNameTaken.value) {-->
<!--    invalidNameFeedback.value = "The name is already taken"-->
<!--    return false-->
<!--  }-->
<!--  return true-->
<!--})-->

<!--const isPhoneValid = computed(() => {-->
<!--  const regexp: RegExp = /^\+?[1-9][0-9]{7,14}$/-->
<!--  return regexp.test(phone.value);-->
<!--})-->

<!--function submit() {-->
<!--  isValidated.value = true-->
<!--  isNameTaken.value = false-->
<!--  isEmailTaken.value = false-->
<!--  if (isEmailValid.value && isNameValid.value && isPhoneValid.value) {-->
<!--    registerCompany()-->
<!--  }-->
<!--}-->


<!--</script>-->
