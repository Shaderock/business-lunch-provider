<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12">
        <v-data-table v-model:expanded="expanded"
                      :class="`elevation-20`"
                      :headers="optionsHeaders"
                      :items="useSupplierOptionsStore().getFormattedOptions"
                      density="compact"
                      show-expand>
          <template v-slot:top>
            <v-toolbar extended extension-height="1">
              <v-toolbar-title>Menu Options</v-toolbar-title>

              <v-btn append-icon="mdi-plus" color="primary" variant="outlined"
                     @click="onOptionAddDialog">
                Add Option
              </v-btn>

              <template v-slot:extension>
                <v-progress-linear v-if="isLoading" indeterminate/>
              </template>

            </v-toolbar>
          </template>

          <template v-slot:item.isPublic="{ item }">
            <v-checkbox-btn
              v-model="item.raw.isPublic"
              disabled
              false-icon="mdi-eye-off"
              true-icon="mdi-eye"
            />
          </template>

          <template v-slot:item.hasPhoto="{ item }">
            <v-btn
              v-if="item.raw.hasPhoto"
              icon
              @click="onViewPhotoDialog(item.raw)">
              <v-icon color="info" icon="mdi-image"/>
            </v-btn>
          </template>

          <template v-slot:expanded-row="{ columns, item }">
            <tr>
              <td :colspan="columns.length">
                <v-list>
                  <v-list-item subtitle="Description">
                    <v-list-item-title>{{ item.raw.description }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </td>
            </tr>
          </template>

          <template v-slot:item.actions="{ item }">
            <v-btn v-if="!item.raw.isPublished" :disabled="isLoading" icon variant="plain"
                   @click="onOptionEditDialog(item.raw)">
              <v-icon icon="mdi-pencil"/>
            </v-btn>

            <v-btn icon variant="plain" @click="onUploadPhotoDialog(item.raw)">
              <v-icon icon="mdi-image-edit"/>
            </v-btn>

            <v-btn icon variant="plain" @click="onDeletePhoto(item.raw)">
              <v-icon color="error" icon="mdi-image-minus"/>
            </v-btn>

            <v-btn :disabled="isLoading" icon variant="plain">
              <v-icon color="error" icon="mdi-delete"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to delete option {{ item.raw.name }}?</v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="error"
                           @click="onOptionDelete(item.raw)">
                      Delete option
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>

            <v-btn v-if="item.raw.isPublic" :disabled="isLoading" icon variant="plain">
              <v-icon color="secondary" icon="mdi-eye-off"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to hide option {{ item.raw.name }}?</v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="onOptionHide(item.raw)">
                      Hide option
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>

            <v-btn
              v-else-if="useSupplierCategoriesStore().getCategoryByName(item.raw.categoryName)?.isPublic"
              :disabled="isLoading" icon variant="plain">
              <v-icon color="secondary" icon="mdi-eye"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to publish option {{ item.raw.name }}?</v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="onOptionPublish(item.raw)">
                      Publish option
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>

  <v-dialog v-model="isEditDialogShown" :persistent="isDialogLoading">
    <v-row justify="center">
      <v-col lg="3" md="5" sm="8">
        <v-form ref="editForm" v-model="isEditFormValid" @submit.prevent="onOptionEdit()">
          <v-card title="Edit Option">
            <v-card-text>
              <v-select v-model="optionToEdit.categoryName"
                        :items="useSupplierCategoriesStore().getCategoriesNames"
                        :rules="[rules.required]"
                        label="Category"/>

              <v-text-field
                v-model="optionToEdit.name"
                :rules="[rules.required]"
                counter="75"
                label="Option name"
                type="text"
              />

              <v-textarea
                v-model="optionToEdit.description"
                :rules="[rules.required]"
                counter="300"
                label="Option description"
                type="text"
              />

              <v-text-field
                v-model="optionToEdit.gram"
                :rules="[rules.required]"
                counter="35"
                hint="100g/55g/20g"
                label="Option gram"
                type="text"
              />

              <v-text-field
                v-model="optionToEdit.price"
                :rules="[rules.required, rules.greaterThanZero]"
                label="Option price (MDL)"
                step="0.01"
                type="number"
              />
            </v-card-text>

            <v-card-actions>
              <v-btn
                :loading="isDialogLoading"
                block
                color="primary"
                type="submit"
                variant="outlined">
                Edit Option
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
  </v-dialog>

  <v-dialog v-model="isAddDialogShown" :persistent="isDialogLoading">
    <v-row justify="center">
      <v-col lg="3" md="5" sm="8">
        <v-form ref="addForm" v-model="isAddFormValid" @submit.prevent="onOptionAdd()">
          <v-card title="Add Option">
            <v-card-text>
              <v-select v-model="optionToAdd.categoryName"
                        :items="useSupplierCategoriesStore().getCategoriesNames"
                        :rules="[rules.required]"
                        label="Category"/>

              <v-text-field
                v-model="optionToAdd.name"
                :rules="[rules.required]"
                counter="75"
                label="Option name"
                type="text"
              />

              <v-textarea
                v-model="optionToAdd.description"
                :rules="[rules.required]"
                counter="300"
                label="Option description"
                type="text"
              />

              <v-text-field
                v-model="optionToAdd.gram"
                :rules="[rules.required]"
                counter="35"
                hint="100g/55g/20g"
                label="Option gram"
                type="text"
              />

              <v-text-field
                v-model="optionToAdd.price"
                :rules="[rules.required, rules.greaterThanZero]"
                label="Option price (MDL)"
                step="0.01"
                type="number"
              />
            </v-card-text>

            <v-card-actions>
              <v-btn
                :disabled="!isAddFormValid"
                :loading="isDialogLoading"
                block
                color="primary"
                type="submit"
                variant="outlined">
                Add Option
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
  </v-dialog>

  <v-dialog v-model="isViewPhotoDialogShown">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card v-if="useSupplierOptionsStore().getCurrentFormattedOptionPhoto?.isLoadingThumbnail"
                :height="useOrganizationStore().getLogoCardMaxHeight"
                :width="useOrganizationStore().getLogoCardWidth">
          <div class="d-flex align-center justify-center fill-height">
            <v-progress-circular indeterminate/>
          </div>
        </v-card>
        <v-card v-else max-width="1000">
          <v-img
            :lazy-src="`data:image/jpeg;base64,${useSupplierOptionsStore().getCurrentFormattedOptionPhoto?.thumbnail}`"
            :src="`data:image/jpeg;base64,${useSupplierOptionsStore().getCurrentFormattedOptionPhoto?.photo}`"
            aspect-ratio="16/9"
            cover
            width="500">
            <template v-slot:placeholder>
              <div class="d-flex align-center justify-center fill-height">
                <v-progress-circular indeterminate/>
              </div>
            </template>
          </v-img>
        </v-card>
      </v-col>
    </v-row>
  </v-dialog>

  <v-dialog v-model="isUploadPhotoDialogShown" :persistent="isDialogLoading">
    <v-row justify="center">
      <v-col lg="3" md="5" sm="8">
        <v-form ref="uploadForm" v-model="isUploadFormValid" @submit.prevent="onPhotoUpload()">
          <v-card title="Upload a photo">
            <v-card-text>
              <v-file-input
                v-model="optionToEdit.photoToUpload"
                :multiple="false"
                :rules="[rules.fileSize, rules.required]"
                accept="image/*"
                clearable
                hint="Max 500KB"
                label="Upload photo (Max 500KB)"
                prepend-inner-icon="mdi-image"
                show-size/>
            </v-card-text>

            <v-card-actions>
              <v-btn
                :disabled="!isUploadFormValid"
                :loading="isDialogLoading"
                block
                color="primary"
                variant="outlined"
                @click="onPhotoUpload">
                Upload Photo
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
  </v-dialog>
</template>

<script lang="ts" setup>

import {onBeforeMount, Ref, ref} from "vue";
import {
  FormattedOption,
  useSupplierCategoriesStore,
  useSupplierOptionsStore
} from "@/store/supplier-adm-app";
import {useOrganizationStore} from "@/store/employee-or-supplier-app";
import {VForm} from "vuetify/components";

onBeforeMount(() => {
  useSupplierOptionsStore().requestFreshDataIfEmpty().finally(() => isLoading.value = false)
})

const isLoading = ref(true)
const isDialogLoading = ref(false)

const isAddDialogShown = ref(false)
const addForm = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const isAddFormValid = ref(false);

const isEditDialogShown = ref(false)
const editForm = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const isEditFormValid = ref(false);

const isViewPhotoDialogShown = ref(false)
const isUploadPhotoDialogShown = ref(false)
const uploadForm = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const isUploadFormValid = ref(false);

const optionToAdd = ref(useSupplierOptionsStore().generateEmptyFormattedOption())
const optionToEdit = ref(useSupplierOptionsStore().generateEmptyFormattedOption())
const maxFileSize = 1024 * 1024 * 0.5 // 512KB
const expanded = ref([])

const rules = {
  required: (value: any) => !!value || 'Required field.',
  greaterThanZero: (value: number) => {
    return value > 0 || 'Should be greater than zero.';
  },
  fileSize: (file: File[]) => {
    if (file[0] && file[0].size > maxFileSize) {
      return `File size should not exceed ${maxFileSize / 1024 / 1024} MB`
    }
    return true
  }
};

const optionsHeaders = [
  {title: 'Name', key: 'name'},
  {title: 'Category', key: 'categoryName'},
  {title: 'Price (MDL)', key: 'price'},
  {title: 'Gram', key: 'gram'},
  {title: 'Photo', key: 'hasPhoto', sortable: false, align: 'center'},
  {title: 'Created', key: 'createdAt'},
  {title: 'Published', key: 'publishedAt'},
  {title: "Public", key: 'isPublic'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

function onViewPhotoDialog(formattedOption: FormattedOption) {
  useSupplierOptionsStore().downloadCurrentOptionPhoto(formattedOption.id)
  isViewPhotoDialogShown.value = true
}

function onUploadPhotoDialog(formattedOption: FormattedOption) {
  optionToEdit.value.id = formattedOption.id
  optionToEdit.value.photoToUpload = []
  isUploadPhotoDialogShown.value = true
}

function onOptionEditDialog(formattedOption: FormattedOption) {
  optionToEdit.value = formattedOption
  isEditDialogShown.value = true
  editForm.value?.validate()
}

async function onOptionEdit() {
  if (isEditFormValid.value) {
    try {
      isDialogLoading.value = true
      await useSupplierOptionsStore().editOption(optionToEdit.value)
      isEditDialogShown.value = false
    } finally {
      isDialogLoading.value = false
    }
  }
}

function onOptionAddDialog() {
  optionToAdd.value = useSupplierOptionsStore().generateEmptyFormattedOption()
  isAddDialogShown.value = true
}

async function onPhotoUpload() {
  await uploadForm.value?.validate()
  if (isUploadFormValid.value) {
    try {
      isDialogLoading.value = true
      await useSupplierOptionsStore().uploadPhoto(optionToEdit.value)
      isUploadPhotoDialogShown.value = false
    } finally {
      isDialogLoading.value = false
    }
  }
}

async function onDeletePhoto(formattedOption: FormattedOption) {
  try {
    isLoading.value = true
    await useSupplierOptionsStore().deletePhoto(formattedOption)
  } finally {
    isLoading.value = false
  }
}

async function onOptionAdd() {
  await addForm.value?.validate()
  if (isAddFormValid.value) {
    try {
      isDialogLoading.value = true
      await useSupplierOptionsStore().createOption(optionToAdd.value)
      isAddDialogShown.value = false
    } finally {
      isDialogLoading.value = false
    }
  }
}

function onOptionDelete(formattedOption: FormattedOption) {
  try {
    isLoading.value = true
    useSupplierOptionsStore().deleteOption(formattedOption)
  } finally {
    isLoading.value = false
  }
}

function onOptionPublish(formattedOption: FormattedOption) {
  try {
    isLoading.value = true
    useSupplierOptionsStore().publishOption(formattedOption)
  } finally {
    isLoading.value = false
  }
}

function onOptionHide(formattedOption: FormattedOption) {
  try {
    isLoading.value = true
    useSupplierOptionsStore().hideOption(formattedOption)
  } finally {
    isLoading.value = false
  }
}
</script>
