<template>
  <v-row justify="center">
    <v-col cols="12" md="4" sm="8">

      <v-card class="mx-auto" elevation="20">
        <v-card-title>My Organization</v-card-title>

        <v-divider/>

        <v-card-subtitle class="pt-4">Name</v-card-subtitle>
        <v-card-text v-if="useOrganizationStore().getOrganization?.name">
          {{ useOrganizationStore().getOrganization.name }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>


        <v-card-subtitle>
          <v-icon icon="mdi-email"/>
          Email
        </v-card-subtitle>
        <v-card-text v-if="useOrganizationStore().getOrganization?.email">
          {{ useOrganizationStore().getOrganization.email }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>


        <v-card-subtitle>
          <v-icon icon="mdi-phone"/>
          Phone
        </v-card-subtitle>
        <v-card-text v-if="useOrganizationStore().getOrganization?.phone">
          {{ useOrganizationStore().getOrganization.phone }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>


        <v-card-subtitle>
          Description
        </v-card-subtitle>
        <v-card-text v-if="useOrganizationStore().getOrganization?.description">
          {{ useOrganizationStore().getOrganization.description }}
        </v-card-text>
        <v-card-text v-else class="text-caption">
          Empty
        </v-card-text>

        <v-card-actions>
          <v-btn color="primary" @click="initDialogue()">Edit Details
          </v-btn>

          <v-dialog v-model="show">
            <v-col cols="12" md="4" sm="8">
              <v-card>
                <v-card-title>Edit Organization Details</v-card-title>

                <v-form ref="form" v-model="valid" validate-on="submit" @submit.prevent="submit()">
                  <v-card-text>
                    <v-text-field
                      v-model="org.name"
                      :rules="[rules.required]"
                      color="primary"
                      label="Name"
                    ></v-text-field>

                    <v-textarea
                      v-model="org.description"
                      :rules="[rules.required]"
                      color="primary"
                      hint="Any information you consider important to share"
                      label="Description"
                    ></v-textarea>

                    <v-text-field
                      v-model="org.email"
                      :rules="[rules.required, rules.email]"
                      color="primary"
                      hint="email@example.com"
                      label="Email"

                    ></v-text-field>

                    <v-text-field
                      v-model="org.phone"
                      :rules="[rules.required, rules.phone]"
                      color="primary"
                      hint="+37311222333"
                      label="Phone"
                    ></v-text-field>
                  </v-card-text>

                  <v-card-actions>
                    <v-btn color="primary" type="submit" variant="outlined">Save</v-btn>
                    <v-btn color="secondary" variant="plain" @click="show = false">Cancel</v-btn>
                  </v-card-actions>
                </v-form>
              </v-card>
            </v-col>
          </v-dialog>
        </v-card-actions>
      </v-card>

    </v-col>
  </v-row>

</template>
<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useOrganizationStore} from "@/store/app";
import organizationService from "@/services/OrganizationService";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import toastManager from "@/services/ToastManager";

onMounted(() => {
  useOrganizationStore().requestFreshOrganizationData()
})

function initDialogue() {
  show.value = true
  const organization = useOrganizationStore().getOrganization;
  org.value.name = organization?.name ?? '';
  org.value.email = organization?.email ?? '';
  org.value.phone = organization?.phone ?? '';
  org.value.description = organization?.description ?? '';
}

async function submit() {
  if (valid.value === true) {
    await organizationService.update(new OrganizationDetails(
      null,
      org.value.name,
      org.value.description,
      org.value.email,
      org.value.phone,
      null
    ))
    toastManager.showSuccess("Updated!", "Your organization details were updated successfully")
    show.value = false
    await useOrganizationStore().requestFreshOrganizationData();
  }
}

const valid = ref(true);
const show = ref(false)
const rules = {
  required: (value: string) => !!value || 'Required field.',
  email: (value: string) => {
    const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return pattern.test(value) || 'Invalid e-mail.';
  },
  phone: (value: string) => {
    const pattern = /^\+(?:\d ?){6,14}\d$/
    return pattern.test(value) || 'Invalid phone number.';
  }
};

const org = ref({
  name: '',
  description: '',
  email: '',
  phone: '',
});

</script>
