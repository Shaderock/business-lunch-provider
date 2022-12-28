import {AppSettings} from "./AppSettings";
import {OrganizationService} from "./OrganizationService";
import axios, {AxiosResponse} from "axios";

export class CompanyService extends OrganizationService {
    private companyUrl = AppSettings.API_URL + '/company'
    private registerUrl = this.companyUrl + "/register"
    private userCompanyUrl = this.companyUrl + '/my'

    public register(email: string, name: string, phone: string): Promise<any> {
        return axios.post(this.registerUrl, {
            email: email,
            name: name,
            phone: phone
        });
    }

    public getUserCompany(): Promise<AxiosResponse<any, any>> {
        return axios.get(this.userCompanyUrl)
    }
}
