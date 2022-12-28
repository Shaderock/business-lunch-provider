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
            const toastManager: ToastManager = new ToastManager()
            if (error.response.config.url !== AppSettings.API_URL + '/login')
                if (error.response.status === 403 || error.response.status === 401) {
                    toastManager.showInfo('Unauthorized access',
                        'Your session could have expired. Sign in, please.')
                    let authStore = useAuthStore()
                    authStore.logout()
                    await router.push("/login")
                }
            if (error.response.status === 500) {
                toastManager.showError('Server error', 'It looks like there was a server-side problem. ' +
                    'Please, try again later')
            }
            if (error.response.status === 400)
                if (error.response.data && error.response.data.displayToUser === true && error.response.data.errMessage)
                    toastManager.showErrorFromErrorResponse(error.response)
            return Promise.reject(error);
        }
    );
}




