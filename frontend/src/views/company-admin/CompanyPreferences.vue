<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="6" sm="8">
        <v-card class="mx-auto" elevation="20" variant="tonal">
          <v-toolbar>
            <v-toolbar-title>Organization Preferences</v-toolbar-title>
            <v-btn color="primary" @click="initDialogue()">Edit Preferences</v-btn>
          </v-toolbar>

          <v-list>
            <v-list-subheader title="Discounts"/>

            <v-list-item :subtitle="useCompAdmCompPrefStore().getPreferences.companyDiscountType"
                         prepend-icon="mdi-sale" title="Employees' discount configuration"/>
            <v-divider inset/>

            <v-list-item>
              <v-list-item-title>
                <span>Discount </span>
                <span class="text-info font-weight-bold">Percentage (%)</span>
                <span> for the first employee order</span>
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ useCompAdmCompPrefStore().getPreferences.discountPercentageFirstOrder }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-divider inset/>

            <v-list-item>
              <v-list-item-title>
                <span class="text-info font-weight-bold">Fix</span>
                <span> discount for the first employee order</span>
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ useCompAdmCompPrefStore().getPreferences.discountFixFirstOrder }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-divider inset/>

            <v-list-item>
              <v-list-item-title>
                <span>Maximum </span>
                <span class="text-info font-weight-bold">Fix</span>
                <span> discount for the first employee order</span>
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ useCompAdmCompPrefStore().getPreferences.maxDiscountFixFirstOrder }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-divider inset/>

            <v-list-item>
              <v-list-item-title>
                <span class="text-info font-weight-bold">Fix</span>
                <span> discount for employee daily</span>
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ useCompAdmCompPrefStore().getPreferences.discountPerDay }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-divider inset/>

            <v-list-subheader title="Delivery"/>

            <v-list-item :subtitle="useCompAdmCompPrefStore().getPreferences.deliverAt?.toString()"
                         prepend-icon="mdi-timer-music" title="Default lunch deliver time"/>
            <v-divider inset/>

            <v-list-item :subtitle="useCompAdmCompPrefStore().getPreferences.deliveryAddress"
                         prepend-icon="mdi-map-marker title=" title="Delivery address"/>
            <v-divider inset/>

          </v-list>
        </v-card>
      </v-col>

      <v-dialog v-model="show">
        <v-row justify="center">
          <v-col md="4" sm="8">
            <v-card class="mx-auto">
              <v-card-title>Edit Organization Preferences</v-card-title>

              <v-form ref="form" v-model="valid" @submit.prevent="submit()">
                <v-card-text>
                  <v-row>
                    <v-col>
                      <v-select v-model="updatePreferences.companyDiscountType"
                                :items="[CompanyDiscountType.PercentageFirst, CompanyDiscountType.SpecificAmountFirst,
                            CompanyDiscountType.SpecificOverPercentageFirst, CompanyDiscountType.SpecificPerDay]"
                                :rules="[companyPreferencesRules.required]"
                                hint="Select the way discount is applied for employees"
                                label="Select discount type"/>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col
                      v-if="updatePreferences.companyDiscountType === CompanyDiscountType.PercentageFirst ||
                  updatePreferences.companyDiscountType === CompanyDiscountType.SpecificOverPercentageFirst">
                      <v-text-field
                        v-model="updatePreferences.discountPercentageFirstOrder"
                        :rules="[companyPreferencesRules.minimumZero, companyPreferencesRules.isSafeInteger]"
                        color="primary"
                        hint="Only first employee's order will get a discount"
                        label="Discount for the first order"
                        prepend-inner-icon="mdi-percent"
                        type="number"/>
                    </v-col>
                    <v-col
                      v-if="updatePreferences.companyDiscountType !== CompanyDiscountType.SpecificPerDay">
                      <v-text-field
                        v-model="updatePreferences.discountFixFirstOrder"
                        :rules="[companyPreferencesRules.minimumZero]"
                        color="primary"
                        hint="Only first employee's order will get a discount"
                        label="Discount (fix) for the first order"
                        min="0"
                        step="0.01"
                        type="number"/>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col
                      v-if="updatePreferences.companyDiscountType !== CompanyDiscountType.SpecificPerDay">
                      <v-text-field
                        v-model="updatePreferences.maxDiscountFixFirstOrder"
                        :rules="[companyPreferencesRules.minimumZero]"
                        color="primary"
                        hint="Only first employee's order will get a discount"
                        label="Max Discount (fix) for the first order"
                        step="0.01"
                        type="number"/>
                    </v-col>
                    <v-col
                      v-if="updatePreferences.companyDiscountType === CompanyDiscountType.SpecificPerDay">
                      <v-text-field
                        v-model="updatePreferences.discountPerDay"
                        :rules="[companyPreferencesRules.minimumZero]"
                        color="primary"
                        hint="Spend on any order until zero"
                        label="Employee's cash"
                        step="0.01"
                        type="number"/>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.deliverAt"
                        :rules="[companyPreferencesRules.required]"
                        color="primary"
                        label="Lunch delivery time"
                        type="time"/>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col>
                      <v-text-field
                        v-model="updatePreferences.deliveryAddress"
                        :rules="[companyPreferencesRules.required]"
                        color="primary"
                        hint="Where should supplier deliver lunch?"
                        label="Delivery Address"
                        prepend-inner-icon="mdi-map-marker"/>
                    </v-col>
                  </v-row>

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
    </v-row>
  </v-container>
</template>
<script lang="ts" setup>
import {onMounted, Ref, ref} from "vue";
import toastManager from "@/services/ToastManager";
import {Utils} from "@/models/Utils";
import {VForm} from "vuetify/components";
import {CompanyDiscountType} from "@/models/CompanyDiscountType";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import companyPreferencesService from "@/services/CompanyPreferencesService";
import {useCompAdmCompPrefStore} from "@/store/company-adm-app";

onMounted(() => {
  useCompAdmCompPrefStore().requestFreshPreferencesData()
})

const form = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const valid = ref(false);
const show = ref(false)
const updatePreferences = ref({
  discountPercentageFirstOrder: 0,
  discountFixFirstOrder: 0.0,
  maxDiscountFixFirstOrder: 0.0,
  discountPerDay: 0.0,
  deliverAt: '',
  deliveryAddress: '',
  companyDiscountType: CompanyDiscountType.PercentageFirst,
});

const companyPreferencesRules = {
  required: (value: any) => !!value || 'Required field.',
  minimumZero: (value: number) => {
    return value >= 0 || 'Can\'t be negative.';
  },
  isSafeInteger: (value: any) => {
    return Number.isInteger(Number.parseFloat(value)) || 'Should be a valid number.';
  }
};

function initDialogue() {
  show.value = true
  valid.value = false
  const persistedPreferences = useCompAdmCompPrefStore().getPreferences

  updatePreferences.value.companyDiscountType =
    persistedPreferences?.companyDiscountType ?? CompanyDiscountType.SpecificAmountFirst
  updatePreferences.value.discountPercentageFirstOrder = persistedPreferences?.discountPercentageFirstOrder ?? 0
  updatePreferences.value.discountFixFirstOrder = persistedPreferences?.discountFixFirstOrder ?? 0.0
  updatePreferences.value.maxDiscountFixFirstOrder = persistedPreferences?.maxDiscountFixFirstOrder ?? 0.0
  updatePreferences.value.discountPerDay = persistedPreferences?.discountPerDay ?? 0.0

  const deliveryDate: Date | null = useCompAdmCompPrefStore().getDeliveryTime

  updatePreferences.value.deliverAt = deliveryDate != null ? Utils.formatDateToTimeAsString(deliveryDate) : '13:00:00'
  updatePreferences.value.deliveryAddress = persistedPreferences?.deliveryAddress ?? ''
}

async function submit() {
  await form.value?.validate()
  if (valid.value === true) {
    try {
      await companyPreferencesService.update(new CompanyPreferences(
        null,
        null,
        updatePreferences.value.companyDiscountType,
        updatePreferences.value.discountPercentageFirstOrder,
        updatePreferences.value.discountFixFirstOrder,
        updatePreferences.value.maxDiscountFixFirstOrder,
        updatePreferences.value.discountPerDay,
        Utils.stringToTime(updatePreferences.value.deliverAt),
        updatePreferences.value.deliveryAddress
      ))
      toastManager.showSuccess("Updated!", "Your organization preferences were updated successfully")
      show.value = false
      await useCompAdmCompPrefStore().requestFreshPreferencesData()
    } catch (error) {
      console.log('Something wrong happened during preferences update: ' + error)
      toastManager.showDefaultError("There was an error during preferences update")
    }
  }
}
</script>
