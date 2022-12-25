<template>
  <MDBContainer>
    <MDBRow center>
      <MDBCol auto>
        <form>
          <!-- Email input -->
          <MDBInput
              id="form2Email"
              v-model="email"
              label="Email address"
              type="email"
              wrapperClass="mb-4"/>
          <!-- Password input -->
          <MDBInput
              id="form2Password"
              v-model="password"
              label="Password"
              type="password"
              wrapperClass="mb-4"/>
          <!-- 2 column grid layout for inline styling -->
          <!-- Submit button -->
          <MDBBtn block color="primary"> Sign in</MDBBtn>

          <!-- Register buttons -->
          <div class="text-center mt-2">
            <p>Not a member?
              <router-link to="/register">Register</router-link>
            </p>
          </div>
        </form>
      </MDBCol>
    </MDBRow>
  </MDBContainer>
</template>

<script lang="ts" setup>
import {MDBBtn, MDBCol, MDBContainer, MDBInput, MDBRow} from "mdb-vue-ui-kit";
import {ref} from "vue";

const email = ref("");
const password = ref("");
</script>
