import axios from 'axios';
import {AppSettings} from "../services/AppSettings";
import {useAuthStore} from "../stores/AuthStore";
import {ToastManager} from "../services/ToastManager";
import {useRouter} from "vue-router";

export default function configureAxios() {
    axios.defaults.baseURL = AppSettings.API_URL
    axios.defaults.headers.common['Content-Type'] = "application/json"

    axios.interceptors.request.use(
        config => {
            let authStore = useAuthStore()
            if (config.headers)
                config.headers['Authorization'] = authStore.getAuthHeaderBearer();
            return config;
        },
        error => {
            return Promise.reject(error);
        }
    );

    axios.interceptors.response.use(
        (response) => {
            return response;
        },
        async (error) => {
            const router = useRouter()
            if (error.response.config.url !== AppSettings.API_URL + '/login')
                if (error.response.status === 401 || error.response.status === 403) {
                    const toastManager: ToastManager = new ToastManager()
                    toastManager.showInfo('Unauthorized access', 'Your session has expired. Sign in, please.')
                    let authStore = useAuthStore()
                    authStore.logout()

                    await router.push("/login")
                }

            return Promise.reject(error);
        }
    );
}




