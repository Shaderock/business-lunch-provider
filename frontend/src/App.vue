<template>
  <v-app>
    <the-app-bar></the-app-bar>
    <the-side-bar></the-side-bar>
    <v-main>
      <v-container>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>

import TheSideBar from "@/components/side-bar/TheSideBar.vue";
import TheAppBar from "@/components/app-bar/TheAppBar.vue";
</script>
