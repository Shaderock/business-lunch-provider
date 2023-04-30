import {defineStore} from "pinia";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {AxiosResponse} from "axios";
import moment, {Duration} from "moment";
import {Supplier} from "@/models/Supplier";
import supplierService from "@/services/SupplierService";
import {OrderType} from "@/models/OrderType";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import {Subscription} from "@/models/Subscription";
import {SubscriptionStatus} from "@/models/SubscriptionStatus";
import {Utils} from "@/models/Utils";
import subscriptionService from "@/services/SubscriptionService";
import organizationService from "@/services/OrganizationService";
import {PublicCompanyPreferences} from "@/models/PublicCompanyPreferences";
import {Company} from "@/models/Company";
import companyService from "@/services/CompanyService";
import companyPreferencesService from "@/services/CompanyPreferencesService";
import {Category} from "@/models/Category";
import categoryService from "@/services/CategoryService";
import {Option} from "@/models/Option";
import optionService from "@/services/OptionService";
import {EmployeeOrder} from "@/models/EmployeeOrder";
import employeeOrderService from "@/services/EmployeeOrderService";
import {CompanyOrderStatus} from "@/models/CompanyOrderStatus";
import {CompanyOrder} from "@/models/CompanyOrder";
import companyOrderService from "@/services/CompanyOrderService";

export const useSupAdmSupPrefStore = defineStore('supplierAdminSupplierPreferences', {
  state: () => ({
    preferences: new SupplierPreferences(null,
      null,
      moment.duration(null),
      new Date(),
      new Date(),
      0,
      0,
      OrderType.OnlyOneOptionPerCategory,
      [],
      null,
      [])
  }),
  getters: {
    getPreferences(): SupplierPreferences {
      return this.preferences
    },
    getRequestOffset(): Duration | null {
      return moment.duration(this.preferences.requestOffset)
    },
    getStartTime(): Date | null {
      if (this.preferences.workDayStart !== null)
        return moment(this.preferences?.workDayStart, "HH:mm:ss").toDate()
      else
        return null
    },
    getEndTime(): Date | null {
      if (this.preferences.workDayEnd !== null)
        return moment(this.preferences.workDayEnd, "HH:mm:ss").toDate()
      else
        return null
    },
    arePreferencesCompleted(): boolean {
      if (!this.preferences) return false
      return !(!this.preferences.requestOffset ||
        !this.preferences.workDayStart ||
        !this.preferences.workDayEnd ||
        !this.preferences.minimumOrdersPerCompanyRequest ||
        !this.preferences.minimumCategoriesForEmployeeOrder ||
        !this.preferences.orderType ||
        !this.preferences.workDayEnd ||
        !this.preferences.categoriesTags ||
        this.preferences.categoriesTags.length == 0);
    },
    hasPreferences(): boolean {
      return this.preferences !== null;
    },
  },
  actions: {
    async requestFreshPreferencesData() {
      const response: AxiosResponse<SupplierPreferences> = await supplierPreferencesService.getSupplierPreferences()
      this.preferences = response.data
    }
  }
})

export const useSupAdmSupStore = defineStore('supplierAdminSupplier', {
  state: () => ({
    supplier: new Supplier("", "", "", "", false, '')
  }),
  getters: {
    getSupplier(): Supplier {
      return this.supplier
    },
    getPublic(): boolean {
      return this.supplier.isPublic || false
    }
  },
  actions: {
    async update(websiteUrl: string, menuUrl: string) {
      const updatedSupplier = {
        ...this.supplier, websiteUrl: websiteUrl, menuUrl: menuUrl
      };

      await supplierService.updateSupplier(updatedSupplier)
      await this.requestFreshSupplierData()
    },
    async updatePublic(isPublic: boolean) {
      const updatedSupplier = {
        ...this.supplier, isPublic: isPublic
      };

      await supplierService.updateSupplier(updatedSupplier)
      await this.requestFreshSupplierData()
    },

    async requestFreshSupplierData() {
      const response: AxiosResponse<Supplier> = await supplierService.getUserSupplier()
      this.supplier = response.data
    },
    async requestFreshDataIfEmpty() {
      if (this.supplier.id == '') {
        await this.requestFreshSupplierData()
      }
    }
  }
})


export interface SubscriberCompany {
  // company
  companyId: string

  // details
  name: string
  phone: string
  email: string

  // preferences
  deliveryAddress: string

  // subscription
  subscriptionId: string
  subscriptionStatus: SubscriptionStatus
  subscriptionDate: string
}

export const useSupplierSubscribersCompaniesStore = defineStore('supplierAdminSubscribersCompanies', {
  state: () => ({
    companies: [] as Company[],
    companiesPreferences: [] as PublicCompanyPreferences[],
    companiesDetails: [] as OrganizationDetails[],
    subscriptions: [] as Subscription[]
  }),
  getters: {
    getCompanies(): Company[] {
      return this.companies
    },
    getCompaniesPreferences(): PublicCompanyPreferences[] {
      return this.companiesPreferences
    },
    getCompaniesDetails(): OrganizationDetails[] {
      return this.companiesDetails
    },
    getSubscribersCompanies(): SubscriberCompany[] {
      return this.companies.map(company => {
        const preferences = this.companiesPreferences.find(p => p.id === company.preferencesId)
        const details = this.companiesDetails.find(d => d.id === company.organizationDetailsId)
        const subscription = this.subscriptions.find(s => s.companyId === company.id)

        return {
          name: details?.name ?? '',
          email: details?.email ?? '',
          phone: details?.phone ?? '',

          companyId: company.id,

          deliveryAddress: preferences?.deliveryAddress ?? '',

          subscriptionId: subscription?.id ?? '',
          subscriptionStatus: subscription?.subscriptionStatus ?? SubscriptionStatus.Pending,
          subscriptionDate: subscription?.createdAt ? Utils.formatDateWithoutTimeWithSlashes(subscription.createdAt) : ''
        }
      })
    }
  },
  actions: {
    async acceptSubscription(companyId: string) {
      try {
        await subscriptionService.acceptSubscription(companyId);
        const subscription = this.subscriptions.find(subscription => subscription.companyId === companyId);
        if (subscription)
          subscription.subscriptionStatus = SubscriptionStatus.Accepted
      } catch (error) {
        console.log("Couldn't accept a subscription")
      }
    },
    async declineSubscription(companyId: string) {
      try {
        await subscriptionService.declineSubscription(companyId)
        this.companies = this.companies.filter(s => s.id !== companyId)
      } catch (error) {
        console.log("Couldn't decline a subscription")
      }
    },
    async requestFreshData() {
      const companiesResponse: AxiosResponse<Company[]> =
        await companyService.requestSupplierSubscribers()
      this.companies = companiesResponse.data

      const preferencesResponse: AxiosResponse<PublicCompanyPreferences[]> =
        await companyPreferencesService.requestSubscribersCompaniesPreferences()
      this.companiesPreferences = preferencesResponse.data

      const detailsResponse: AxiosResponse<OrganizationDetails[]> =
        await organizationService.requestSubscribersCompaniesDetails()
      this.companiesDetails = detailsResponse.data

      const subscriptionsResponse: AxiosResponse<Subscription[]> =
        await subscriptionService.requestSupplierSubscribers()
      this.subscriptions = subscriptionsResponse.data
    },
    async requestFreshDataIfEmpty() {
      if (this.getCompanies.length === 0) {
        await this.requestFreshData()
      }
    }
  }
})

export interface FormattedCategory {
  id: string
  name: string
  categoriesAmount: number
  createdAt: string
  publishedAt: string
  isPublished: boolean
  isPublic: boolean
}

export const useSupplierCategoriesStore = defineStore('supplierAdminCategories', {
  state: () => ({
    categories: [] as Category[],
  }),
  getters: {
    getCategories(): Category[] {
      return this.categories
    },
    getFormattedCategories(): FormattedCategory[] {
      return this.categories.map(category => {
        return {
          id: category.id,
          name: category.name,
          categoriesAmount: category.optionIds.length,
          createdAt: category.createdAt ? Utils.formatDateWithoutTimeWithSlashes(category.createdAt) : '',
          publishedAt: category.publishedAt ? Utils.formatDateWithoutTimeWithSlashes(category.publishedAt) : '',
          isPublished: !!category.publishedAt,
          isPublic: category.isPublic
        }
      })
    },
    getCategoriesNames(): string[] {
      return this.categories.map(c => c.name)
    }
  },
  actions: {
    async createCategory(formattedCategory: FormattedCategory) {
      const response =
        await categoryService.createCategory(this.convertFormattedCategoryToNewCategory(formattedCategory))
      this.categories.push(response.data)
    },
    async editCategory(formattedCategory: FormattedCategory) {
      const categoryIndex: number = this.categories.findIndex(c => c.id === formattedCategory.id);
      const oldName: string = this.categories[categoryIndex].name
      if (categoryIndex !== -1) {
        try {
          this.categories[categoryIndex].name = formattedCategory.name;
          const response: AxiosResponse<Category> = await categoryService.updateCategory(this.categories[categoryIndex]);
          this.categories[categoryIndex] = response.data;
        } catch (err) {
          this.categories[categoryIndex].name = oldName;
        }
      }
    },
    async deleteCategory(formattedCategory: FormattedCategory) {
      await categoryService.deleteCategory(formattedCategory.id)
      this.categories = this.categories.filter(c => c.id !== formattedCategory.id)
    },
    async updateCategoryVisibility(formattedCategory: FormattedCategory, isPublic: boolean) {
      const categoryIndex: number = this.categories.findIndex(c => c.id === formattedCategory.id);
      if (categoryIndex !== -1) {
        try {
          this.categories[categoryIndex].isPublic = isPublic;
          const response: AxiosResponse<Category> = await categoryService.updateCategory(this.categories[categoryIndex]);
          this.categories[categoryIndex] = response.data;
        } catch (err) {
          this.categories[categoryIndex].isPublic = !isPublic;
        }
      }
    },
    async publishCategory(formattedCategory: FormattedCategory) {
      await this.updateCategoryVisibility(formattedCategory, true)
    },
    async hideCategory(formattedCategory: FormattedCategory) {
      await this.updateCategoryVisibility(formattedCategory, false)
    },
    convertFormattedCategoryToNewCategory(formattedCategory: FormattedCategory): Category {
      return new Category(formattedCategory.id, formattedCategory.name, [], new Date(), new Date(), false)
    },
    getCategoryById(categoryId: string): Category | null {
      return this.categories.find(c => c.id === categoryId) || null;
    },
    getCategoryByName(name: string): Category | null {
      return this.categories.find(c => c.name === name) || null;
    },
    generateEmptyFormattedCategory(): FormattedCategory {
      return {
        id: '',
        name: '',
        categoriesAmount: 0,
        createdAt: '',
        publishedAt: '',
        isPublished: false,
        isPublic: false,
      }
    },
    async requestFreshData() {
      const categoriesResponse: AxiosResponse<Category[]> = await categoryService.requestSupplierCategories()
      this.categories = categoriesResponse.data
    },
    async requestFreshDataIfEmpty() {
      if (this.categories.length === 0) await this.requestFreshData()
    }
  }
})


export interface FormattedOption {
  id: string
  name: string
  categoryName: string
  description: string
  gram: string
  price: number
  hasPhoto: boolean
  photoToUpload: File[],
  createdAt: string
  publishedAt: string
  isPublished: boolean
  isPublic: boolean
}

export interface FormattedOptionPhoto {
  optionId: string,
  isLoadingThumbnail: boolean
  hasPhoto: boolean
  photo: string,
  thumbnail: string
}

export const useSupplierOptionsStore = defineStore('supplierAdminOptions', {
  state: () => ({
    options: [] as Option[],
    formattedOptionsPhotos: [] as FormattedOptionPhoto[],
    currentOptionId: '' as string
  }),
  getters: {
    getOptions(): Option[] {
      return this.options
    },
    getFormattedOptions(): FormattedOption[] {
      return this.options.map(option => {
        return {
          id: option.id,
          name: option.name,
          description: option.description,
          gram: option.gram,
          price: option.price,
          hasPhoto: option.hasPhoto,
          photoToUpload: [],
          categoryName: useSupplierCategoriesStore().getCategoryById(option.categoryId)?.name || '',
          createdAt: option.createdAt ? Utils.formatDateWithoutTimeWithSlashes(option.createdAt) : '',
          publishedAt: option.publishedAt ? Utils.formatDateWithoutTimeWithSlashes(option.publishedAt) : '',
          isPublished: !!option.publishedAt,
          isPublic: option.isPublic
        }
      })
    },
    getCurrentFormattedOptionPhoto(): FormattedOptionPhoto | null {
      const photoIndex: number =
        this.formattedOptionsPhotos.findIndex(p => p.optionId === this.currentOptionId)

      if (photoIndex != -1) {
        return this.formattedOptionsPhotos[photoIndex];
      } else {
        return null
      }
    }
  },
  actions: {
    async createOption(formattedOption: FormattedOption) {
      const category: Category | null = useSupplierCategoriesStore().getCategoryByName(formattedOption.categoryName)
      const response =
        await optionService.createOption(this.convertFormattedOptionToNewOption(formattedOption), category?.id || '')
      this.options.push(response.data)
      this.formattedOptionsPhotos.push({
        optionId: response.data.id || '',
        hasPhoto: false,
        isLoadingThumbnail: false,
        photo: '',
        thumbnail: ''
      })
    },
    async editOption(formattedOption: FormattedOption) {
      const category: Category | null = useSupplierCategoriesStore().getCategoryByName(formattedOption.categoryName)
      const optionIndex: number = this.options.findIndex(c => c.id === formattedOption.id);

      const oldName: string = this.options[optionIndex].name
      const oldDescription: string = this.options[optionIndex].description
      const oldPrice: number = this.options[optionIndex].price
      const oldGram: string = this.options[optionIndex].gram

      if (optionIndex !== -1) {
        try {
          this.options[optionIndex].name = formattedOption.name;
          const response: AxiosResponse<Option> =
            await optionService.updateOption(this.options[optionIndex], category?.id || '');
          this.options[optionIndex] = response.data;
        } catch (err) {
          this.options[optionIndex].name = oldName
          this.options[optionIndex].description = oldDescription
          this.options[optionIndex].price = oldPrice
          this.options[optionIndex].gram = oldGram
        }
      }
    },
    async deleteOption(formattedOption: FormattedOption) {
      await optionService.deleteOption(formattedOption.id)
      this.options = this.options.filter(c => c.id !== formattedOption.id)
    },
    async deletePhoto(formattedOption: FormattedOption) {
      await optionService.deletePhoto(formattedOption.id);
      this.updateHasPhoto(formattedOption.id, false);
    },
    async uploadPhoto(formattedOption: FormattedOption) {
      await optionService.updatePhoto(formattedOption.photoToUpload[0], formattedOption.id);
      this.updateHasPhoto(formattedOption.id, true);
    },
    updateHasPhoto(id: string, hasPhoto: boolean) {
      const option = this.options.find(c => c.id === id);
      if (option) {
        option.hasPhoto = hasPhoto;
      }
      const photo = this.formattedOptionsPhotos.find(c => c.optionId === id);
      if (photo) {
        photo.hasPhoto = hasPhoto;
      }
    },
    async updateOptionVisibility(formattedOption: FormattedOption, isPublic: boolean) {
      const optionIndex: number = this.options.findIndex(c => c.id === formattedOption.id);
      const category: Category | null = useSupplierCategoriesStore().getCategoryByName(formattedOption.categoryName)
      if (optionIndex !== -1) {
        try {
          this.options[optionIndex].isPublic = isPublic;
          const response: AxiosResponse<Option> = await optionService.updateOption(this.options[optionIndex], category?.id || '');
          this.options[optionIndex] = response.data;
        } catch (err) {
          this.options[optionIndex].isPublic = !isPublic;
        }
      }
    },
    async publishOption(formattedOption: FormattedOption) {
      await this.updateOptionVisibility(formattedOption, true)
    },
    async hideOption(formattedOption: FormattedOption) {
      await this.updateOptionVisibility(formattedOption, false)
    },
    async downloadCurrentOptionPhoto(optionId: string) {
      this.currentOptionId = optionId
      const photo: FormattedOptionPhoto | null = this.getCurrentFormattedOptionPhoto

      if (photo && photo.hasPhoto && photo.thumbnail === '' && photo.photo === '') {
        photo.isLoadingThumbnail = true
        photo.thumbnail = await optionService.requestOptionPhotoAsSupplier(optionId, true)
        photo.isLoadingThumbnail = false

        photo.photo = await optionService.requestOptionPhotoAsSupplier(optionId, false)
      }
    },
    convertFormattedOptionToNewOption(formattedOption: FormattedOption): Option {
      return new Option(
        formattedOption.id,
        formattedOption.name,
        useSupplierCategoriesStore().getCategoryByName(formattedOption.categoryName)?.id || '',
        formattedOption.description,
        formattedOption.price,
        formattedOption.hasPhoto,
        formattedOption.gram,
        formattedOption.isPublic,
        new Date(),
        new Date())
    },
    generateEmptyFormattedOption(): FormattedOption {
      return {
        id: '',
        name: '',
        description: '',
        gram: '',
        price: 0,
        hasPhoto: false,
        photoToUpload: [],
        categoryName: '',
        createdAt: '',
        publishedAt: '',
        isPublished: false,
        isPublic: false,
      }
    },
    async requestFreshData() {
      this.options = []
      this.formattedOptionsPhotos = []

      await useSupplierCategoriesStore().requestFreshDataIfEmpty()
      const categoriesResponse: AxiosResponse<Option[]> = await optionService.requestSupplierOptions()
      this.options = categoriesResponse.data

      for (const option of this.options) {
        this.formattedOptionsPhotos.push({
          optionId: option.id,
          hasPhoto: option.hasPhoto,
          isLoadingThumbnail: false,
          photo: '',
          thumbnail: ''
        })
      }
    },
    async requestFreshDataIfEmpty() {
      if (this.options.length === 0) await this.requestFreshData()
    }
  }
})

export interface CompanyOrderExtended {
  employeesOrders: EmployeeOrder[]
  companyOrder: CompanyOrder
  subscriberCompany: SubscriberCompany
  orderedOptionsExtended: OptionExtended[]
}

export interface OptionExtended {
  option: Option,
  amount: number
}

export interface CompanyOrderRecord {
  orderId: string
  companyName: string
  email: string
  phone: string
  defaultSupplierPriceSum: number
  supplierDiscountSum: number
  income: number
  status: CompanyOrderStatus
  deliverAt: string
}

export const useSupplierAdmOrdersStore = defineStore('supplierAdminOrders', {
  state: () => ({
    selectedDate: Utils.formatDateWithoutTimeWithDashes(new Date),
    employeesOrders: [] as EmployeeOrder[],
    companiesOrders: [] as CompanyOrder[],
  }),
  getters: {
    getSelectedDate(): string {
      return this.selectedDate
    },
    getEmployeesOrders(): EmployeeOrder[] {
      return this.employeesOrders
    },
    getCompaniesOrders(): CompanyOrder[] {
      return this.companiesOrders
    },
    getCompaniesOrdersExtended(): CompanyOrderExtended[] {
      return this.getCompaniesOrders
      .map(co => {
        const employeesOrders: EmployeeOrder[] = this.getEmployeesOrders
        .filter(eo => eo.companyOrderId === co.id)

        const subscriberCompany: SubscriberCompany | undefined =
          useSupplierSubscribersCompaniesStore().getSubscribersCompanies
          .find(c => c.name === co.companyName)

        const orderedOptionsMap: Record<string, OptionExtended> = {}
        const allOptions = useSupplierOptionsStore().getOptions
        employeesOrders.forEach(eo => {
          eo.optionIds.forEach(optionId => {
            const option = allOptions.find(o => o.id === optionId)
            if (option) {
              if (orderedOptionsMap[optionId]) {
                orderedOptionsMap[optionId].amount += 1
              } else {
                orderedOptionsMap[optionId] = {
                  option: option,
                  amount: 1
                }
              }
            }
          })
        })
        const orderedOptionsExtended = Object.values(orderedOptionsMap)

        return {
          companyOrder: co,
          employeesOrders: employeesOrders,
          orderedOptionsExtended: orderedOptionsExtended,
          subscriberCompany: subscriberCompany ?? {
            companyId: '',
            name: '',
            phone: '',
            email: '',
            deliveryAddress: '',
            subscriptionId: '',
            subscriptionStatus: SubscriptionStatus.Accepted,
            subscriptionDate: '',
          }
        }
      })
      .filter(coe => coe.subscriberCompany.companyId !== '')
    },
    getCompaniesOrdersRecords(): CompanyOrderRecord[] {
      return this.getCompaniesOrdersExtended.map(coe => {
        const defaultSupplierPriceSum: number = coe.employeesOrders
        .reduce((sum: number, order) => sum + Number(order.supplierDefaultPrice), 0);
        const supplierDiscountSum: number = coe.employeesOrders
        .reduce((sum: number, order) => sum + Number(order.supplierDiscount), 0);
        const income: number = defaultSupplierPriceSum - supplierDiscountSum;
        return {
          orderId: coe.companyOrder.id,
          companyName: coe.subscriberCompany.name,
          email: coe.subscriberCompany.email,
          phone: coe.subscriberCompany.phone,
          defaultSupplierPriceSum: defaultSupplierPriceSum,
          supplierDiscountSum: supplierDiscountSum,
          income: income,
          status: coe.companyOrder.status,
          deliverAt: Utils.dateAndTimeAsStrToTimeAsString(coe.companyOrder.deliverAt),
        }
      })
    }
  },
  actions: {
    async setSelectedDate(date: string) {
      this.selectedDate = date
      await this.requestUpdateOrdersForSelectedDate()
    },
    async confirmOrder(record: CompanyOrderRecord) {
      await companyOrderService.supAdmConfirmOrder(record.orderId)
      await this.requestUpdateOrdersForSelectedDate()
    },
    async declineOrder(record: CompanyOrderRecord) {
      await companyOrderService.supAdmDeclineOrder(record.orderId)
      await this.requestUpdateOrdersForSelectedDate()
    },
    async requestUpdateOrdersForSelectedDate() {
      await this.requestOrdersForSelectedDate()
    },
    getCompanyOrderExtended(record: CompanyOrderRecord): CompanyOrderExtended | undefined {
      return this.getCompaniesOrdersExtended.find(coe => coe.companyOrder.id === record.orderId)
    },
    async requestOrdersForSelectedDate() {
      this.employeesOrders = []
      const employeeOrderResponse: AxiosResponse<EmployeeOrder[]> =
        await employeeOrderService.supAdmRequestForDate(this.selectedDate)
      this.employeesOrders = employeeOrderResponse.data

      this.companiesOrders = []
      const companiesOrdersResponse: AxiosResponse<CompanyOrder[]> =
        await companyOrderService.supAdmRequestForDate(this.selectedDate)
      this.companiesOrders = companiesOrdersResponse.data
    },
    async requestFreshData() {
      await useSupplierOptionsStore().requestFreshDataIfEmpty()
      await useSupplierSubscribersCompaniesStore().requestFreshDataIfEmpty()
    },
    async requestFreshDataIfEmpty() {
      if (this.getCompaniesOrders.length === 0 || this.getEmployeesOrders.length === 0)
        await this.requestFreshData()
    }
  }
})
