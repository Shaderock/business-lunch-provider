<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12">
        <v-data-table :class="`elevation-20`"
                      :headers="categoriesHeaders"
                      :items="useSupplierCategoriesStore().getFormattedCategories"
                      density="compact">
          <template v-slot:top>
            <v-toolbar extended extension-height="1">
              <v-toolbar-title>Menu Categories</v-toolbar-title>

              <v-btn append-icon="mdi-folder-plus" color="primary" variant="outlined"
                     @click="onCategoryAddDialog">
                Add Category
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

          <template v-slot:item.actions="{ item }">
            <v-btn v-if="!item.isPublished" :disabled="isLoading" icon variant="plain"
                   @click="onCategoryEditDialog(item.raw)">
              <v-icon icon="mdi-folder-edit"/>
            </v-btn>

            <v-btn :disabled="isLoading" icon variant="plain">
              <v-icon color="error" icon="mdi-folder-remove"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to delete category {{ item.raw.name }}?</v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="error"
                           @click="onCategoryDelete(item.raw)">
                      Delete category
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>

            <v-btn v-if="item.raw.isPublic" :disabled="isLoading" icon variant="plain">
              <v-icon color="secondary" icon="mdi-eye-off"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to hide category {{ item.raw.name }}?</v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="onCategoryHide(item.raw)">
                      Hide category
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-menu>
            </v-btn>

            <v-btn v-else-if="useSupAdmSupStore().getPublic" :disabled="isLoading" icon
                   variant="plain">
              <v-icon color="secondary" icon="mdi-folder-eye"/>
              <v-menu activator="parent">
                <v-card>
                  <v-card-title>Are you sure to publish category {{ item.raw.name }}?</v-card-title>
                  <v-card-actions>
                    <v-btn block
                           color="secondary"
                           @click="onCategoryPublish(item.raw)">
                      Publish category
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
        <v-form ref="editForm" v-model="isEditFormValid" @submit.prevent="onCategoryEdit()">
          <v-card title="Edit Category">
            <v-card-text>
              <v-text-field
                v-model="categoryToEdit.name"
                :rules="[rules.required]"
                counter="75"
                label="Category name"
                type="text"
              />
            </v-card-text>

            <v-card-actions>
              <v-btn
                :loading="isDialogLoading" block
                color="primary"
                type="submit"
                variant="outlined">
                Edit Category
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
        <v-form ref="addForm" v-model="isAddFormValid" @submit.prevent="onCategoryAdd()">
          <v-card title="Add Category">
            <v-card-text>
              <v-text-field
                v-model="categoryToAdd.name"
                :rules="[rules.required]"
                counter="75"
                label="Category name"
                type="text"
              />
            </v-card-text>

            <v-card-actions>
              <v-btn
                :loading="isDialogLoading"
                block
                :disabled="!isAddFormValid"
                color="primary"
                type="submit"
                variant="outlined">
                Add Category
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
  FormattedCategory,
  useSupAdmSupStore,
  useSupplierCategoriesStore
} from "@/store/supplier-adm-app";
import {VForm} from "vuetify/components";

onBeforeMount(() => {
  useSupplierCategoriesStore().requestFreshData().finally(() => isLoading.value = false)
  useSupAdmSupStore().requestFreshDataIfEmpty()
})

const isLoading = ref(true)
const isDialogLoading = ref(false)

const isAddDialogShown = ref(false)
const addForm = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const isAddFormValid = ref(false);

const isEditDialogShown = ref(false)
const editForm = ref(null) as Ref<InstanceType<typeof VForm> | null>;
const isEditFormValid = ref(false);

const categoryToAdd = ref(useSupplierCategoriesStore().generateEmptyFormattedCategory())
const categoryToEdit = ref(useSupplierCategoriesStore().generateEmptyFormattedCategory())


const rules = {
  required: (value: any) => !!value || 'Required field.',
};

const categoriesHeaders = [
  {title: 'Name', key: 'name'},
  {title: 'Options Amount', key: 'categoriesAmount'},
  {title: 'Creation Date', key: 'createdAt'},
  {title: 'Last Publish Date', key: 'publishedAt'},
  {title: "Public", key: 'isPublic'},
  {title: 'Actions', key: 'actions', sortable: false, align: 'center'},
]

function onCategoryEditDialog(formattedCategory: FormattedCategory) {
  categoryToEdit.value = formattedCategory
  isEditDialogShown.value = true
}

async function onCategoryEdit() {
  await editForm.value?.validate()
  if (isEditFormValid.value) {
    try {
      isDialogLoading.value = true
      await useSupplierCategoriesStore().editCategory(categoryToEdit.value)
      isEditDialogShown.value = false
    } finally {
      isDialogLoading.value = false
    }
  }
}

function onCategoryAddDialog() {
  categoryToAdd.value = useSupplierCategoriesStore().generateEmptyFormattedCategory()
  isAddDialogShown.value = true
}

async function onCategoryAdd() {
  await addForm.value?.validate()
  if (isAddFormValid.value) {
    try {
      isDialogLoading.value = true
      await useSupplierCategoriesStore().createCategory(categoryToAdd.value)
      isAddDialogShown.value = false
    } finally {
      isDialogLoading.value = false
    }
  }
}

function onCategoryDelete(formattedCategory: FormattedCategory) {
  try {
    isLoading.value = true
    useSupplierCategoriesStore().deleteCategory(formattedCategory)
  } finally {
    isLoading.value = false
  }
}

function onCategoryPublish(formattedCategory: FormattedCategory) {
  try {
    isLoading.value = true
    useSupplierCategoriesStore().publishCategory(formattedCategory)
  } finally {
    isLoading.value = false
  }
}

function onCategoryHide(formattedCategory: FormattedCategory) {
  try {
    isLoading.value = true
    useSupplierCategoriesStore().hideCategory(formattedCategory)
  } finally {
    isLoading.value = false
  }
}
</script>
