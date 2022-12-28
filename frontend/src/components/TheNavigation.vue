<template>
  <MDBContainer fluid>
    <MDBNavbar bg="light" container expand="md" light>
      <MDBNavbarToggler
          target="#navbarSupportedContent"
          @click="collapse = !collapse">
      </MDBNavbarToggler>
      <MDBCollapse id="navbarSupportedContent" v-model="collapse">
        <MDBNavbarNav class="mb-lg-0">
          <MDBNavbarItem to="/">
            <MDBIcon icon="home"/>
            Home
          </MDBNavbarItem>
          <MDBNavbarItem v-if="userStore.isOnlyAppUser">
            <!-- Navbar dropdown -->
            <MDBDropdown v-model="organizationCreationDropdown" class="nav-item">
              <MDBDropdownToggle
                  class="nav-link"
                  tag="a"
                  @click="organizationCreationDropdown = !organizationCreationDropdown">Create Organization
              </MDBDropdownToggle>
              <MDBDropdownMenu aria-labelledby="dropdownMenuButton">
                <MDBDropdownItem href="#" @click="router.push('/company/registration')">Register my company
                </MDBDropdownItem>
                <MDBDropdownItem href="#" @click="router.push('/supplier/registration')">Become a supplier
                </MDBDropdownItem>
              </MDBDropdownMenu>
            </MDBDropdown>
          </MDBNavbarItem>
          <MDBNavbarItem v-else-if="userStore.isEmployee">
            <MDBNavbarItem to="/company">
              <MDBIcon icon="building"/>
              My Company
            </MDBNavbarItem>
          </MDBNavbarItem>
        </MDBNavbarNav>

        <MDBNavbarNav v-if="authStore.isAuthenticated" class="d-flex flex-row" right>
          <!-- Icons -->
          <MDBNavbarItem class="me-3 me-lg-0" to="#">
            <MDBIcon icon="shopping-cart"></MDBIcon>
          </MDBNavbarItem>
          <MDBNavbarItem class="me-3 me-lg-0" href="#">
            <MDBIcon icon="bell"/>
          </MDBNavbarItem>
          <!-- Icon dropdown -->
          <MDBNavbarItem class="me-3 me-lg-0 dropdown">
            <MDBDropdown v-model="profileActionsDropdown">
              <MDBDropdownToggle class="nav-link" tag="a" @click="profileActionsDropdown = !profileActionsDropdown">
                {{ userStore.getUser.email }}
                <MDBIcon icon="user"/>
              </MDBDropdownToggle>
              <MDBDropdownMenu>
                <MDBDropdownItem href="/profile">My Profile</MDBDropdownItem>
                <!--todo-->
                <MDBDropdownItem href="#">My Preferences</MDBDropdownItem>
                <MDBDropdownItem href="#" @click="logout">Logout</MDBDropdownItem>
              </MDBDropdownMenu>
            </MDBDropdown>
          </MDBNavbarItem>
        </MDBNavbarNav>

        <MDBNavbarNav v-else right>
          <MDBNavbarItem class="d-flex align-items-end" to="/login">
            <MDBBtn outline="primary">Login</MDBBtn>
          </MDBNavbarItem>
        </MDBNavbarNav>
      </MDBCollapse>
    </MDBNavbar>
  </MDBContainer>
</template>

<script lang="ts" setup>
import {
  MDBBtn,
  MDBCollapse,
  MDBContainer,
  MDBDropdown,
  MDBDropdownItem,
  MDBDropdownMenu,
  MDBDropdownToggle,
  MDBIcon,
  MDBNavbar,
  MDBNavbarItem,
  MDBNavbarNav,
  MDBNavbarToggler
} from 'mdb-vue-ui-kit';
import {ref} from 'vue';
import {useAuthStore} from "../stores/AuthStore";
import {ToastManager} from "../services/ToastManager";
import {useRouter} from "vue-router";
import {useUserStore} from "../stores/UserStore";

const collapse = ref(false);
const organizationCreationDropdown = ref(false);
const profileActionsDropdown = ref(false);
const authStore = useAuthStore();
const userStore = useUserStore();
const router = useRouter()

function logout() {
  authStore.logout();
  router.push('/');
  new ToastManager().showSuccess("Good Bye!", "You were signed out")
}
</script>
