<template>
  <v-row justify="center">
    <v-col cols="auto" md="5">
      <v-card elevation="20">
        <v-toolbar>
          <v-toolbar-title>My Organization</v-toolbar-title>

          <v-btn v-if="useProfileStore().isCompanyAdmin || useProfileStore().isSupplier"
                 color="primary"
                 @click="initDialogue()">Edit Details
          </v-btn>
        </v-toolbar>

        <v-list>
          <v-list-item :subtitle="useOrganizationStore().getOrganization.name" title="Name"/>
          <v-divider inset/>
          <v-list-item :subtitle="useOrganizationStore().getOrganization.email"
                       prepend-icon="mdi-email"
                       title="Email"/>
          <v-divider inset/>
          <v-list-item :subtitle="useOrganizationStore().getOrganization.phone"
                       prepend-icon="mdi-phone"
                       title="Phone"/>
          <v-divider inset/>
          <div v-if="useProfileStore().isSupplier">
            <v-list-item :subtitle="useSupAdmSupStore().getSupplier.websiteUrl"
                         prepend-icon="mdi-web"
                         title="Website URL"/>
            <v-divider inset/>
            <v-list-item :subtitle="useSupAdmSupStore().getSupplier.menuUrl" prepend-icon="mdi-web"
                         title="Menu URL"/>
            <v-divider inset/>
            <v-list-item>
              <template v-slot:prepend="{ isPublic }">
                <v-list-item-action start>
                  <v-checkbox-btn v-model="persistedSupplier.isPublic" disabled
                                  v-bind="isPublic"></v-checkbox-btn>
                </v-list-item-action>
              </template>
              <v-list-item-title>Whether supplier is public</v-list-item-title>
              <v-list-item-subtitle>Supplier can be found on main page</v-list-item-subtitle>
            </v-list-item>
            <v-divider inset/>
          </div>
          <v-list-subheader title="Description"/>
          <v-container>
            {{ useOrganizationStore().getOrganization.description }}
          </v-container>
          <v-divider v-if="useProfileStore().isSupplier"/>
        </v-list>

        <v-card-actions v-if="useProfileStore().isSupplier">
          <v-btn v-if="!useSupAdmSupStore().getPublic"
                 :disabled="!useSupAdmSupPrefStore().arePreferencesCompleted ||
                 !useOrganizationStore().areDetailsCompleted"
                 color="primary"
                 variant="outlined"
                 @click="updatePublic(true)">
            Become public
          </v-btn>
          <v-btn v-if="useSupAdmSupStore().getPublic"
                 color="secondary"
                 @click="updatePublic(false)">
            Become private
          </v-btn>

          <v-tooltip
            v-if="!useSupAdmSupPrefStore().arePreferencesCompleted || !useOrganizationStore().areDetailsCompleted">
            <template v-slot:activator="{ props }">
              <v-spacer/>
              <v-icon color="info" icon="mdi-information" v-bind="props"/>
            </template>
            Complete profile and preferences in order to allow users to order from you
          </v-tooltip>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>

  <v-dialog v-model="show">
    <v-row justify="center">
      <v-col md="4" sm="8">
        <v-card>
          <v-card-title>Edit Organization Details</v-card-title>

          <v-form ref="form" v-model="valid" @submit.prevent="submit()">
            <v-card-text>
              <v-text-field
                v-model="organizationToUpdate.name"
                :rules="[rules.required]"
                color="primary"
                label="Name"
              />

              <v-textarea
                v-model="organizationToUpdate.description"
                :rules="[rules.required]"
                color="primary"
                hint="Any information you consider important to share"
                label="Description"
              />

              <v-text-field
                v-model="organizationToUpdate.email"
                :rules="[rules.required, rules.email]"
                color="primary"
                hint="email@example.com"
                label="Email"
              />

              <v-text-field
                v-model="organizationToUpdate.phone"
                :rules="[rules.required]"
                color="primary"
                hint="+37311222333"
                label="Phone"
              />

              <div v-if="useProfileStore().isSupplier">
                <v-text-field
                  v-model="supplierToUpdate.websiteUrl"
                  color="primary"
                  hint="https://my-website.com"
                  label="Website URL"
                  type="url"
                />

                <v-text-field
                  v-model="supplierToUpdate.menuUrl"
                  color="primary"
                  hint="https://my-website.com/menu"
                  label="Menu URL"
                  type="url"
                />
              </div>
            </v-card-text>

            <v-card-actions>
              <v-btn color="primary" type="submit" variant="outlined">Save</v-btn>
              <v-btn color="secondary" variant="plain" @click="show = false">Cancel</v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-dialog>

</template>
<script lang="ts" setup>
import {computed, onMounted, Ref, ref} from "vue";
import {useProfileStore} from "@/store/user-app";
import organizationService from "@/services/OrganizationService";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import toastManager from "@/services/ToastManager";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import {VForm} from "vuetify/components";
import {useSupAdmSupPrefStore, useSupAdmSupStore} from "@/store/supplier-adm-app";

onMounted(() => {
  useOrganizationStore().requestFreshOrganizationData()
  if (useProfileStore().isSupplier) {
    useSupAdmSupStore().requestFreshSupplierData()
    useSupAdmSupPrefStore().requestFreshPreferencesData()
  }
})


const persistedSupplier = computed(() => useSupAdmSupStore().getSupplier)
const form = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const valid = ref(false);
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

function initDialogue() {
  show.value = true
  const organization = useOrganizationStore().getOrganization;
  organizationToUpdate.value.name = organization?.name ?? '';
  organizationToUpdate.value.email = organization?.email ?? '';
  organizationToUpdate.value.phone = organization?.phone ?? '';
  organizationToUpdate.value.description = organization?.description ?? '';

  const supplier = useSupAdmSupStore().getSupplier
  supplierToUpdate.value.menuUrl = supplier.menuUrl
  supplierToUpdate.value.websiteUrl = supplier.websiteUrl
}

async function submit() {
  await form.value?.validate()
  if (valid.value === true) {
    try {
      await organizationService.update(new OrganizationDetails(
        null,
        organizationToUpdate.value.name,
        organizationToUpdate.value.description,
        organizationToUpdate.value.email,
        organizationToUpdate.value.phone,
        null
      ))

      if (useProfileStore().isSupplier) {
        await useSupAdmSupStore().update(supplierToUpdate.value.websiteUrl, supplierToUpdate.value.menuUrl)
      }

      toastManager.showSuccess("Updated!", "Your organization details were updated successfully")
      show.value = false
      await useOrganizationStore().requestFreshOrganizationData();
    } catch (error) {
      console.log('Something wrong happened during organization details update: ' + error)
      toastManager.showDefaultError("There was an error during organization details update")
    }
  }
}

async function updatePublic(isPublic: boolean) {
  useSupAdmSupStore().updatePublic(isPublic)
}

const organizationToUpdate = ref({
  name: '',
  description: '',
  email: '',
  phone: '',
});

const supplierToUpdate = ref({
  websiteUrl: '',
  menuUrl: '',
  isPublic: false
})

</script>
