import {defineStore} from 'pinia';
import {Category} from "@/models/Category";
import {Supplier} from "@/models/Supplier";
import {PublicOrganizationDetails} from "@/models/PublicOrganizationDetails";
import {Option} from "@/models/Option";
import {PublicSupplierPreferences} from "@/models/PublicSupplierPreferences";
import {EmployeeOrderValidation} from "@/models/EmployeeOrderValidation";
import {EmployeeOrder} from "@/models/EmployeeOrder";
import {EmployeeOrderStatus} from "@/models/EmployeeOrderStatus";
import employeeOrderService from "@/services/EmployeeOrderService";
import {AxiosResponse} from "axios";
import {Utils} from "@/models/Utils";
import toastManager from "@/services/ToastManager";
import optionService from "@/services/OptionService";

export interface CartOption {
  id: string,
  option: Option,
  category: Category,
  supplier: Supplier,
  supplierDetails: PublicOrganizationDetails,
  supplierPreferences: PublicSupplierPreferences
}

export interface CartSupplierInfo {
  supplier: Supplier,
  supplierDetails: PublicOrganizationDetails,
  supplierPreferences: PublicSupplierPreferences
}

export const useCartStore = defineStore('employeeCartStore', {
  state: () => ({
    shouldBlink: false as boolean,
    blinkInterval: undefined as number | undefined,
    currentCartSupplier: null as CartSupplierInfo | null,
    currentEmployeeOrderValidation: null as EmployeeOrderValidation | null,
    currentEmployeeOrderPrices: null as EmployeeOrder | null,
    selectedDate: Utils.formatDateWithoutTimeWithDashes(new Date),
    cartOptions: [] as CartOption[],
  }),
  getters: {
    isBlinking(): boolean {
      return this.shouldBlink
    },
    getCurrentSupplierInfo(): CartSupplierInfo | null {
      return this.currentCartSupplier
    },
    getCartOptions(): CartOption[] {
      return this.cartOptions
    },
    getCartSupplierCartOptions(): CartOption[] {
      return this.getCartOptions.filter(o => o.supplier.id === this.getCurrentSupplierInfo?.supplier.id) || []
    },
    getCartSuppliersInfo(): CartSupplierInfo[] {
      const suppliersMap = new Map<string, CartSupplierInfo>();

      this.getCartOptions.forEach(o => {
        if (o.supplier.id && !suppliersMap.has(o.supplier.id)) {
          suppliersMap.set(o.supplier.id, {
            supplier: o.supplier,
            supplierDetails: o.supplierDetails,
            supplierPreferences: o.supplierPreferences,
          });
        }
      });

      return Array.from(suppliersMap.values());
    },
    getSuppliersDetails(): PublicOrganizationDetails[] {
      return this.getCartOptions
      .map(o => o.supplierDetails)
      .filter((details, index, array) => {
        return array.findIndex(d => d.id === details.id) === index;
      });
    },
    getCurrentOrderValidation(): EmployeeOrderValidation | null {
      return this.currentEmployeeOrderValidation
    },
    getCurrentOrderWithPrices(): EmployeeOrder | null {
      return this.currentEmployeeOrderPrices
    }
  },
  actions: {
    async setSelectedDate(date: string) {
      this.selectedDate = date
      await this.reValidateCurrentSupplier()
    },
    async setCurrentSupplier(cartSupplierInfo: CartSupplierInfo): Promise<void> {
      this.currentCartSupplier = cartSupplierInfo;
      const optionsIds: string[] = this.getCartSupplierCartOptions.map(o => o.option.id);
      if (optionsIds.length > 0) {
        const employeeOrder = new EmployeeOrder('', '', '', 0, 0, 0, 0,
          EmployeeOrderStatus.PendingAdminConfirmation, optionsIds, this.selectedDate);
        const validationResponse: AxiosResponse<EmployeeOrderValidation> =
          await employeeOrderService.requestOrderValidation(employeeOrder);
        this.currentEmployeeOrderValidation = validationResponse.data;

        if (this.currentEmployeeOrderValidation.valid) {
          const validationResponse: AxiosResponse<EmployeeOrder> =
            await employeeOrderService.requestOrderCalculation(employeeOrder);
          this.currentEmployeeOrderPrices = validationResponse.data;
        }
      } else {
        this.currentCartSupplier = null;
      }
    },
    async reValidateCurrentSupplier(): Promise<void> {
      if (this.getCartSupplierCartOptions.length == 0) {
        await this.setCurrentSupplier(this.getCartSuppliersInfo[0]);
      } else if (this.getCurrentSupplierInfo) {
        await this.setCurrentSupplier(this.getCurrentSupplierInfo);
      }
    },
    async sendCurrentOptions(): Promise<void> {
      const optionsIds: string[] = this.getCartSupplierCartOptions.map(o => o.option.id);
      await employeeOrderService.createOrder(new EmployeeOrder(
        '', '', '', 0, 0, 0, 0,
        EmployeeOrderStatus.PendingAdminConfirmation, optionsIds, this.selectedDate));
      toastManager.showSuccess("Order sent", "New order has just been sent. Waiting confirmation")
      await this.removeCartOptionsForCurrentSupplier()
    },
    addCartOption(cartOption: CartOption): void {
      this.getCartOptions.push(cartOption);
      localStorage.setItem('cartOptions', JSON.stringify(this.getCartOptions));
      this.triggerCartBlink();
    },
    async removeCartOption(cartOption: CartOption): Promise<void> {
      this.cartOptions = this.cartOptions.filter(o => o.id !== cartOption.id);
      localStorage.setItem('cartOptions', JSON.stringify(this.cartOptions));
      await this.reValidateCurrentSupplier();
    },
    async removeCartOptionsForCurrentSupplier(): Promise<void> {
      this.cartOptions = this.cartOptions
      .filter(o => o.supplier.id !== this.getCurrentSupplierInfo?.supplier.id);
      localStorage.setItem('cartOptions', JSON.stringify(this.cartOptions));

      await this.reValidateCurrentSupplier();
    },
    triggerCartBlink(): void {
      if (this.blinkInterval) {
        clearInterval(this.blinkInterval);
      }
      this.shouldBlink = true
      let blinkCount = 0;
      this.blinkInterval = window.setInterval(() => {
        this.shouldBlink = !this.shouldBlink
        blinkCount++
        if (blinkCount >= 6) {
          window.clearInterval(this.blinkInterval)
          this.shouldBlink = false
        }
      }, 500);
    },
    initializeFromLocalStorage() {
      const storedCartOptionsString: string | null = localStorage.getItem('cartOptions');
      this.cartOptions = storedCartOptionsString ? JSON.parse(storedCartOptionsString) as CartOption[] : []
    }
  }
})

export const useEmployeeOrderStore = defineStore('employeeOrderStore', {
  state: () => ({
    employeeOrders: [] as EmployeeOrder[],
    selectedDate: Utils.formatDateWithoutTimeWithDashes(new Date),
    options: [] as Option[],
    // todo fetch validations
  }),
  getters: {
    getEmployeeOrders(): EmployeeOrder[] {
      return this.employeeOrders
    },
    getOptions(): Option[] {
      return this.options
    },
    employeeHasOrders(): boolean {
      return this.employeeOrders.length > 0
    },
    getSelectedDate(): string {
      return this.selectedDate
    }
  },
  actions: {
    async deleteOrder(employeeOrder: EmployeeOrder) {
      await employeeOrderService.deleteOrder(employeeOrder.id)
      this.employeeOrders = this.employeeOrders.filter(o => o.id !== employeeOrder.id)
      this.options = this.options.filter(o => !employeeOrder.optionIds.includes(o.id));
    },
    async requestUpdateOrdersForSelectedDate() {
      const ordersResponse: AxiosResponse<EmployeeOrder[]> = await employeeOrderService.requestForDate(this.selectedDate)
      this.employeeOrders = ordersResponse.data

      const optionsResponse: AxiosResponse<Option[]> = await optionService.requestByOrderForDate(this.selectedDate)
      this.options = optionsResponse.data
    },
    async setSelectedDate(date: string) {
      this.selectedDate = date
      await this.requestUpdateOrdersForSelectedDate()
    },
  }
})
