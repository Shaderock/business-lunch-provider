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

import TheAppBar from "@/components/TheAppBar.vue";
import TheSideBar from "@/components/TheSideBar.vue";</script>
