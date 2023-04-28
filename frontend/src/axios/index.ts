import axios from 'axios';
import {useAuthStore} from "@/store/user-app";
import {ToastManager} from "@/services/ToastManager";
import {ApiConstants} from "@/services/ApiConstants";
import router from "@/router";
import {RouterPaths} from "@/services/RouterPaths";

export default function configureAxios() {
  axios.defaults.baseURL = ApiConstants.BACKEND_URL
  axios.defaults.headers.common['Content-Type'] = "application/json"

  axios.interceptors.request.use(
    config => {
      if (config.headers) {
        config.headers['Authorization'] = useAuthStore().getAuthHeaderBearer();
      }
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
      // const router = useRouter()
      const toastManager: ToastManager = new ToastManager()
      if (error.response.config.url !== ApiConstants.BACKEND_URL + '/login')
        if (error.response.status === 403 || error.response.status === 401) {
          toastManager.showInfo('Unauthorized access',
            'Your session could have expired. Sign in, please.')
          useAuthStore().logout()
          await router.push(RouterPaths.ANONYMOUS_LOGIN)
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




