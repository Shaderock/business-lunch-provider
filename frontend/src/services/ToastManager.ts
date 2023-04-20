import {createToast} from "mosha-vue-toastify";
import 'mosha-vue-toastify/dist/style.css'

export class ToastManager {
  showDefaultError(message: string) {
    createToast({
        title: 'Error',
        description: 'Whoops! ' + message + '. Please, try again!'
      },
      {
        showIcon: true,
        showCloseButton: true,
        type: 'danger',
        position: 'bottom-right',
        timeout: 10000,
        transition: 'bounce',
      })
  }

  showError(title: string, message: string) {
    createToast({
        title: title,
        description: message
      },
      {
        showIcon: true,
        showCloseButton: true,
        type: 'danger',
        position: 'bottom-right',
        timeout: 10000,
        transition: 'bounce',
      })
  }

  showErrorFromErrorResponse(response: any) {
    if (response.data && response.data.displayToUser === true && response.data.errMessage)
      this.showDefaultError(response.data.errMessage)
    else
      this.showDefaultError("Something went wrong")
  }

  showSuccess(title: string, message: string) {
    createToast({
        title: title,
        description: message
      },
      {
        showIcon: true,
        showCloseButton: true,
        type: 'success',
        position: 'bottom-right',
        timeout: 10000,
        transition: 'bounce',
      })
  }

  showInfo(title: string, message: string) {
    createToast({
        title: title,
        description: message
      },
      {
        showIcon: true,
        showCloseButton: true,
        type: 'info',
        position: 'bottom-right',
        timeout: 10000,
        transition: 'bounce',
      })
  }
}

const toastManager: ToastManager = new ToastManager()
export default toastManager
