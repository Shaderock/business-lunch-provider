<template>
  <MDBContainer fluid>
    <MDBNavbar bg="light" container expand="md" light>
      <MDBNavbarToggler
          target="#navbarSupportedContent"
          @click="collapse1 = !collapse1">
      </MDBNavbarToggler>
      <MDBCollapse id="navbarSupportedContent" v-model="collapse1">
        <MDBNavbarNav class="mb-lg-0">
          <MDBNavbarItem active to="food">
            Food
          </MDBNavbarItem>
          <MDBNavbarItem>
            <!-- Navbar dropdown -->
            <MDBDropdown v-model="dropdown1" class="nav-item">
              <MDBDropdownToggle
                  class="nav-link"
                  tag="a"
                  @click="dropdown1 = !dropdown1">Dropdown
              </MDBDropdownToggle>
              <MDBDropdownMenu aria-labelledby="dropdownMenuButton">
                <MDBDropdownItem href="#">Action</MDBDropdownItem>
                <MDBDropdownItem href="#">Another Action</MDBDropdownItem>
                <MDBDropdownItem href="#">Something else here</MDBDropdownItem>
              </MDBDropdownMenu>
            </MDBDropdown>
          </MDBNavbarItem>
          <MDBNavbarItem disabled to="#">
            Disabled
          </MDBNavbarItem>
        </MDBNavbarNav>

        <MDBNavbarNav v-if="authStore.isAuthenticated" class="d-flex flex-row" right>
          <!-- Icons -->
          <MDBNavbarItem class="me-3 me-lg-0" to="#">
            <MDBIcon icon="shopping-cart"></MDBIcon>
          </MDBNavbarItem>
          <MDBNavbarItem class="me-3 me-lg-0" href="#">
            <MDBIcon icon="bell"></MDBIcon>
          </MDBNavbarItem>
          <!-- Icon dropdown -->
          <MDBNavbarItem class="me-3 me-lg-0 dropdown">
            <MDBDropdown v-model="dropdown3">
              <MDBDropdownToggle
                  class="nav-link"
                  tag="a"
                  @click="dropdown3 = !dropdown3">
                <MDBIcon icon="user"/>
              </MDBDropdownToggle>
              <MDBDropdownMenu>
                <MDBDropdownItem href="#" @click="logout ">Logout</MDBDropdownItem>
                <MDBDropdownItem href="#">Another Action</MDBDropdownItem>
                <MDBDropdownItem href="#">Something else here</MDBDropdownItem>
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

const collapse1 = ref(false);
const dropdown1 = ref(false);
const dropdown3 = ref(false);
const authStore = useAuthStore();
const router = useRouter()

function logout() {
  authStore.logout();
  router.push('/');
  new ToastManager().showSuccess("Good Bye!", "You were signed out")
}
</script>
