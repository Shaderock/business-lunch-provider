<template>
  <v-app>
    <the-app-bar/>
    <the-side-bar/>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>

import TheSideBar from "@/components/side-bar/TheSideBar.vue";
import TheAppBar from "@/components/app-bar/TheAppBar.vue";
</script>
