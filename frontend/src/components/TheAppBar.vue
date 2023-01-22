<template>
  <v-app-bar :elevation="3">
    <v-btn>
      <v-icon size="x-large">mdi-menu</v-icon>
    </v-btn>

    <v-divider vertical></v-divider>

    <v-app-bar-title>Business Lunch Provider</v-app-bar-title>

    <v-spacer/>

    <v-btn
      v-if="useAuthStore().isAuthenticated"
      prepend-icon="mdi-account-circle"
      variant="outlined"
    >
      Profile
    </v-btn>
    <v-btn
      v-if="useAuthStore().isAuthenticated"
      @click="logout"
    >
      Sign out
    </v-btn>
    <v-btn
      v-if="!useAuthStore().isAuthenticated"
      color="primary"
      v-bind:to="'/login'"
      variant="outlined"
    >
      Sign in
    </v-btn>

    <v-divider vertical/>

    <v-btn v-if="useAuthStore().isAuthenticated">
      <v-icon>mdi-dots-vertical</v-icon>
    </v-btn>
  </v-app-bar>
</template>
<script lang="ts" setup>
import {useAuthStore} from "@/store/app";
import toastManager from "@/services/ToastManager";
import router from "@/router";

function logout() {
  useAuthStore().logout();
  router.push('/');
  toastManager.showSuccess("Good Bye!", "You were signed out")
}
</script>

<!--<template>-->
<!--  <MDBContainer fluid>-->
<!--    <MDBNavbar bg="light" container expand="md" light>-->
<!--      <MDBNavbarToggler-->
<!--          target="#navbarSupportedContent"-->
<!--          @click="collapse = !collapse">-->
<!--      </MDBNavbarToggler>-->
<!--      <MDBCollapse id="navbarSupportedContent" v-model="collapse">-->
<!--        <MDBNavbarNav class="mb-lg-0">-->
<!--          <MDBNavbarItem to="/">-->
<!--            <MDBIcon icon="home"/>-->
<!--            Home-->
<!--          </MDBNavbarItem>-->
<!--          <MDBNavbarItem v-if="userStore.isOnlyAppUser">-->
<!--            <MDBDropdown v-model="organizationCreationDropdown" class="nav-item">-->
<!--              <MDBDropdownToggle-->
<!--                  class="nav-link" tag="a"-->
<!--                  @click="organizationCreationDropdown = !organizationCreationDropdown">Create Organization-->
<!--              </MDBDropdownToggle>-->
<!--              <MDBDropdownMenu aria-labelledby="dropdownMenuButton">-->
<!--                <MDBNavbarItem to="/company/registration">-->
<!--                  <MDBDropdownItem href="#">Register my company</MDBDropdownItem>-->
<!--                </MDBNavbarItem>-->
<!--                <MDBNavbarItem to="/supplier/registration">-->
<!--                  <MDBDropdownItem href="#">Become a supplier</MDBDropdownItem>-->
<!--                </MDBNavbarItem>-->
<!--              </MDBDropdownMenu>-->
<!--            </MDBDropdown>-->
<!--          </MDBNavbarItem>-->
<!--          <MDBNavbarItem v-else-if="userStore.isEmployee">-->
<!--            <MDBNavbarItem to="/company">-->
<!--              <MDBIcon icon="building"/>-->
<!--              My Company-->
<!--            </MDBNavbarItem>-->
<!--          </MDBNavbarItem>-->
<!--        </MDBNavbarNav>-->

<!--        <MDBNavbarNav v-if="authStore.isAuthenticated" class="d-flex flex-row" right>-->
<!--          <MDBNavbarItem class="me-3 me-lg-0" to="#">-->
<!--            <MDBIcon icon="shopping-cart"></MDBIcon>-->
<!--          </MDBNavbarItem>-->
<!--          <MDBNavbarItem class="me-3 me-lg-0" href="#">-->
<!--            <MDBIcon icon="bell"/>-->
<!--          </MDBNavbarItem>-->
<!--          <MDBNavbarItem class="me-3 me-lg-0 dropdown">-->
<!--            <MDBDropdown v-model="profileActionsDropdown">-->
<!--              <MDBDropdownToggle class="nav-link" tag="a" @click="profileActionsDropdown = !profileActionsDropdown">-->
<!--                {{ userStore.getUser.email }}-->
<!--                <MDBIcon icon="user"/>-->
<!--              </MDBDropdownToggle>-->
<!--              <MDBDropdownMenu>-->
<!--                <MDBNavbarItem to="/profile">-->
<!--                  <MDBDropdownItem href="#">My Profile</MDBDropdownItem>-->
<!--                </MDBNavbarItem>-->
<!--                &lt;!&ndash;todo&ndash;&gt;-->
<!--                <MDBDropdownItem href="#">My Preferences</MDBDropdownItem>-->
<!--                <MDBDropdownItem href="#" @click="logout">Logout</MDBDropdownItem>-->
<!--              </MDBDropdownMenu>-->
<!--            </MDBDropdown>-->
<!--          </MDBNavbarItem>-->
<!--        </MDBNavbarNav>-->

<!--        <MDBNavbarNav v-else right>-->
<!--          <MDBNavbarItem class="d-flex align-items-end" to="/login">-->
<!--            <MDBBtn outline="primary">Login</MDBBtn>-->
<!--          </MDBNavbarItem>-->
<!--        </MDBNavbarNav>-->
<!--      </MDBCollapse>-->
<!--    </MDBNavbar>-->
<!--  </MDBContainer>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import {-->
<!--  MDBBtn,-->
<!--  MDBCollapse,-->
<!--  MDBContainer,-->
<!--  MDBDropdown,-->
<!--  MDBDropdownItem,-->
<!--  MDBDropdownMenu,-->
<!--  MDBDropdownToggle,-->
<!--  MDBIcon,-->
<!--  MDBNavbar,-->
<!--  MDBNavbarItem,-->
<!--  MDBNavbarNav,-->
<!--  MDBNavbarToggler-->
<!--} from 'mdb-vue-ui-kit';-->
<!--import {ref} from 'vue';-->
<!--import {useAuthStore} from "../stores/AuthStore";-->
<!--import {ToastManager} from "../services/ToastManager";-->
<!--import {useRouter} from "vue-router";-->
<!--import {useUserStore} from "../stores/UserStore";-->

<!--const collapse = ref(false);-->
<!--const organizationCreationDropdown = ref(false);-->
<!--const profileActionsDropdown = ref(false);-->
<!--const authStore = useAuthStore();-->
<!--const userStore = useUserStore();-->
<!--const router = useRouter()-->


<!--</script>-->
