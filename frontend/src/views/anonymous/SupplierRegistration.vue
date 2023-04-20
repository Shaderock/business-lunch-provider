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

<!--async function registerCompany() {-->
<!--  try {-->
<!--    let response = await supplierService.validateOrganizationName(name.value)-->
<!--    isNameTaken.value = !response.data-->
<!--    response = await supplierService.validateOrganizationEmail(email.value)-->
<!--    isEmailTaken.value = !response.data-->
<!--    if (!isNameTaken.value && !isEmailTaken.value) {-->
<!--      await supplierService.registerCompany(email.value, name.value, phone.value)-->
<!--      await userStore.requestUserData()-->
<!--      await router.push("/company")-->
<!--      toastManager.showSuccess('Successful company registration', 'Your company has just been created')-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.log('Something wrong happened during company registration: ' + error)-->
<!--    toastManager.showDefaultError("There was an error during company registration request")-->
<!--  }-->
<!--}-->
<!--</script>-->
