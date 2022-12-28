<template>
  <MDBCard v-if="companyStore.getCompany !== null" style="width: 18rem">
    <MDBCardImg alt="..." src="https://mdbootstrap.com/img/new/standard/city/062.webp" top/>
    <MDBCardHeader>
      <MDBCardTitle>
        <MDBIcon icon="building"/>
        My Organization
      </MDBCardTitle>
    </MDBCardHeader>
    <MDBListGroup flush>
      <MDBListGroupItem>
        <div class="fw-bold">Name</div>
        {{ companyStore.getCompany.name }}
      </MDBListGroupItem>
      <MDBListGroupItem>
        <div class="fw-bold">Email</div>
        {{ companyStore.getCompany.email }}
      </MDBListGroupItem>
      <MDBListGroupItem>
        <div class="fw-bold">Contact Phone</div>
        {{ companyStore.getCompany.phone }}
      </MDBListGroupItem>
    </MDBListGroup>
  </MDBCard>
</template>

<script lang="ts" setup>
import {useCompanyStore} from "../stores/CompanyStore";

import {
  MDBCard,
  MDBCardHeader,
  MDBCardImg,
  MDBCardTitle,
  MDBIcon,
  MDBListGroup,
  MDBListGroupItem
} from "mdb-vue-ui-kit";
import {onMounted} from "vue";

const companyStore = useCompanyStore()

onMounted(() => {
  companyStore.requestData()
})
</script>
