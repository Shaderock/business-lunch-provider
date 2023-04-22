import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class InvitationService {

  async getAllCompAdmInvitations(): Promise<any> {
    return axios.get(`${ApiConstants.COMPANY_ADM_INVITATION}/all`)
  }

  async send(userEmail: string) {
    const params: URLSearchParams = new URLSearchParams([['userEmail', userEmail]])
    return axios.post(ApiConstants.COMPANY_ADM_INVITATION, null, {params})
  }

  async dismissInvitation(userEmail: string) {
    const params: URLSearchParams = new URLSearchParams([['userEmail', userEmail]])
    return axios.delete(ApiConstants.COMPANY_ADM_INVITATION, {params})
  }

  async getAllUserInvitation() {
    return axios.get(`${ApiConstants.INVITATION}/all`)
  }

  async acceptInvitation(companyId: string) {
    const params: URLSearchParams = new URLSearchParams([['companyId', companyId]])
    return axios.post(ApiConstants.INVITATION, null, {params})
  }

  async declineInvitation(companyId: string) {
    const params: URLSearchParams = new URLSearchParams([['companyId', companyId]])
    return axios.delete(ApiConstants.INVITATION, {params})
  }
}

const invitationService: InvitationService = new InvitationService()
export default invitationService
