import {createApp} from 'vue'
import App from './App.vue'
import {createPinia} from "pinia";
import 'mdb-vue-ui-kit/css/mdb.min.css';
import router from "./router/index";

const pinia = createPinia();

createApp(App)
    .use(router)
    .use(pinia)
    .mount('#app')
