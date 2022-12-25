<script lang="ts" setup>
import {MDBContainer, MDBFooter} from 'mdb-vue-ui-kit';
import TheNavigation from "./components/TheNavigation.vue";
import {onMounted} from "vue";
import {useAuthStore} from "./stores/AuthStore";

const authStore = useAuthStore()

onMounted(() => {
  authStore.initializeUser()
})
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
