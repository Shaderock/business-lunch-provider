<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="auto" md="8">
        <v-card elevation="20">
          <v-toolbar extended extension-height="1">
            <v-toolbar-title>My Organization</v-toolbar-title>

            <v-btn v-if="useProfileStore().isCompanyAdmin || useProfileStore().isSupplier"
                   color="primary"
                   @click="initDialogue()">
              Edit Details
            </v-btn>

            <template v-slot:extension>
              <v-progress-linear v-if="isLoadingLogo" indeterminate/>
            </template>
          </v-toolbar>

          <v-img v-if="useOrganizationStore().hasLogo"
                 aspect-ratio="16/9"
                 :lazy-src="`data:image/jpeg;base64,${useOrganizationStore().getLogoAsBase64Thumbnail}`"
                 cover
                 :max-height="useOrganizationStore().getLogoCardMaxHeight + 200"
                 :src="`data:image/jpeg;base64,${useOrganizationStore().getLogoAsBase64}`"/>

          <v-btn v-else :size="useOrganizationStore().getLogoCardMaxHeight + 200" block disabled
                 icon variant="plain">
            <v-icon :size="useOrganizationStore().getLogoCardMaxHeight + 300"
                    icon="mdi-image-area"/>
          </v-btn>

          <v-divider/>

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
              <v-list-item :subtitle="useSupAdmSupStore().getSupplier.menuUrl"
                           prepend-icon="mdi-web"
                           title="Menu URL"/>
              <v-divider inset/>
              <v-list-item>
                <template v-slot:prepend="{ isPublic }">
                  <v-list-item-action start>
                    <v-checkbox-btn v-model="persistedSupplier.isPublic" disabled
                                    false-icon="mdi-eye-off"
                                    true-icon="mdi-eye"
                                    v-bind="isPublic"/>
                  </v-list-item-action>
                </template>
                <v-list-item-title>Whether supplier is public</v-list-item-title>
                <v-list-item-subtitle>Supplier can be found on main page</v-list-item-subtitle>
              </v-list-item>
              <v-divider inset/>
            </div>
            <v-list-subheader title="Description"/>
            <v-container>
              <v-sheet class="overflow-auto" color="surface" height="100">
                {{ useOrganizationStore().getOrganization.description }}
              </v-sheet>
            </v-container>
            <v-divider v-if="useProfileStore().isSupplier"/>
          </v-list>

          <v-card-actions v-if="useProfileStore().isSupplier">
            <v-btn v-if="!useSupAdmSupStore().getPublic"
                   :disabled="!useSupAdmSupPrefStore().arePreferencesCompleted ||
                 !useOrganizationStore().areDetailsCompleted"
                   color="primary"
                   variant="outlined"
                   prepend-icon="mdi-eye"
                   @click="updatePublic(true)">
              Become public
            </v-btn>
            <v-btn v-if="useSupAdmSupStore().getPublic"
                   color="secondary"
                   prepend-icon="mdi-eye-off"
                   @click="updatePublic(false)">
              Become private
            </v-btn>

            <v-tooltip
              v-if="!useSupAdmSupPrefStore().arePreferencesCompleted || !useOrganizationStore().areDetailsCompleted">
              <template v-slot:activator="{ props }">
                <v-spacer/>
                <v-icon color="info" icon="mdi-information" v-bind="props"/>
              </template>
              Complete profile/preferences/logo in order to allow users to order from you
            </v-tooltip>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

  <v-dialog v-model="show" persistent>
    <v-row justify="center">
      <v-col md="4" sm="8">
        <v-card>
          <v-card-title>Edit Organization Details</v-card-title>

          <v-form ref="form" v-model="valid" @submit.prevent="submit()">
            <v-card-text>
              <v-file-input
                v-model="organizationToUpdate.logo"
                :multiple="false"
                :rules="[rules.fileSize, rules.required]"
                accept="image/*"
                :loading="isUpdateInProgress"
                clearable
                hint="Max 2MB"
                label="Upload logo (Max 2MB)"
                prepend-inner-icon="mdi-camera"
                show-size/>

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
                prepend-inner-icon="mdi-email"
              />

              <v-text-field
                v-model="organizationToUpdate.phone"
                :rules="[rules.required]"
                color="primary"
                hint="+37311222333"
                label="Phone"
                prepend-inner-icon="mdi-phone"
              />

              <div v-if="useProfileStore().isSupplier">
                <v-text-field
                  v-model="supplierToUpdate.websiteUrl"
                  color="primary"
                  hint="https://my-website.com"
                  label="Website URL"
                  prepend-inner-icon="mdi-web"
                  type="url"
                />

                <v-text-field
                  v-model="supplierToUpdate.menuUrl"
                  color="primary"
                  hint="https://my-website.com/menu"
                  label="Menu URL"
                  prepend-inner-icon="mdi-web"
                  type="url"
                />
              </div>
            </v-card-text>

            <v-card-actions>
              <v-btn :disabled="isUpdateInProgress" color="primary" type="submit"
                     variant="outlined">Save
              </v-btn>
              <v-btn :disabled="isUpdateInProgress" color="secondary" variant="plain"
                     @click="show = false">Cancel
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-dialog>
</template>
<script lang="ts" setup>
import {computed, ComputedRef, onMounted, Ref, ref, UnwrapRef} from "vue";
import {useProfileStore} from "@/store/user-app";
import organizationService from "@/services/OrganizationService";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import toastManager from "@/services/ToastManager";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import {VForm} from "vuetify/components";
import {useSupAdmSupPrefStore, useSupAdmSupStore} from "@/store/supplier-adm-app";
import {Supplier} from "@/models/Supplier";

onMounted(() => {
  useOrganizationStore().requestFreshOrganizationData()
  useOrganizationStore().requestLogo().finally(() => isLoadingLogo.value = false)
  if (useProfileStore().isSupplier) {
    useSupAdmSupStore().requestFreshSupplierData()
    useSupAdmSupPrefStore().requestFreshPreferencesData()
  }
})


const persistedSupplier: ComputedRef<Supplier> = computed(() => useSupAdmSupStore().getSupplier)
const form = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const valid: Ref<UnwrapRef<boolean>> = ref(false);
const show: Ref<UnwrapRef<boolean>> = ref(false)
const maxFileSize = 1024 * 1024 * 2 // 2MB
const isLoadingLogo = ref(true)
const isUpdateInProgress = ref(false)

const rules = {
  required: (value: string) => !!value || 'Required field.',
  email: (value: string) => {
    const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return pattern.test(value) || 'Invalid e-mail.';
  },
  phone: (value: string) => {
    const pattern = /^\+(?:\d ?){6,14}\d$/
    return pattern.test(value) || 'Invalid phone number.';
  },
  fileSize: (file: File[]) => {
    if (file[0] && file[0].size > maxFileSize) {
      return `File size should not exceed ${maxFileSize / 1024 / 1024} MB`
    }
    return true
  }
};

function initDialogue() {
  show.value = true
  const organization = useOrganizationStore().getOrganization;
  organizationToUpdate.value.name = organization?.name ?? '';
  organizationToUpdate.value.email = organization?.email ?? '';
  organizationToUpdate.value.phone = organization?.phone ?? '';
  organizationToUpdate.value.description = organization?.description ?? '';
  organizationToUpdate.value.logo = []

  const supplier = useSupAdmSupStore().getSupplier
  supplierToUpdate.value.menuUrl = supplier.menuUrl
  supplierToUpdate.value.websiteUrl = supplier.websiteUrl
}

async function submit() {
  await form.value?.validate()
  if (valid.value === true) {
    try {
      isUpdateInProgress.value = true

      if (organizationToUpdate.value.logo[0]?.size > 0) {
        await useOrganizationStore().updateLogo(organizationToUpdate.value.logo[0])
      }

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

      await useOrganizationStore().requestFreshOrganizationData();
      show.value = false
    } catch (error) {
      console.log('Something wrong happened during organization details update: ' + error)
      toastManager.showDefaultError("There was an error during organization details update")
    } finally {
      isUpdateInProgress.value = false
    }
  }
}

async function updatePublic(isPublic: boolean) {
  await useSupAdmSupStore().updatePublic(isPublic)
}

const organizationToUpdate = ref({
  name: '',
  description: '',
  email: '',
  phone: '',
  logo: [new File([], '')],
});

const supplierToUpdate = ref({
  websiteUrl: '',
  menuUrl: '',
  isPublic: false
})

</script>
