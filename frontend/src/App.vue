<script lang="ts" setup>
import {MDBContainer, MDBFooter} from 'mdb-vue-ui-kit';
import TheNavigation from "./components/TheNavigation.vue";
import {onMounted} from "vue";
import {useAuthStore} from "./stores/AuthStore";
import {useUserStore} from "./stores/UserStore";
import {useCompanyStore} from "./stores/CompanyStore";

const authStore = useAuthStore()
const userStore = useUserStore()
const companyStore = useCompanyStore()
onMounted(() => {
  initializeStores()
})

async function initializeStores() {
  await authStore.initializeToken()
  if (authStore.isAuthenticated)
    await userStore.requestUserData()
  if (userStore.isEmployee) {
    await companyStore.requestData()
  }
}
</script>

<template>
  <TheNavigation/>
  <MDBContainer class="mt-5" fluid>
    <router-view/>
  </MDBContainer>
  <MDBContainer fluid>
    <MDBFooter/>
  </MDBContainer>
</template>

<style>
#app {
  font-family: Roboto, Helvetica, Arial, sans-serif;
}
</style>
