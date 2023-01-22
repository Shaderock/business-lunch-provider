/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from './App.vue'

// Composables
import {createApp} from 'vue'

// Plugins
import {registerPlugins} from '@/plugins'
import configureAxios from "@/axios";

const app = createApp(App)

registerPlugins(app)
configureAxios()

app.mount('#app')
