import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class InvitationService {

  async getAllCompAdmInvitations(): Promise<any> {
    return axios.get(`${ApiConstants.COMPANY_ADM_INVITATION}/all`)
  }
}

const invitationService: InvitationService = new InvitationService()
export default invitationService
