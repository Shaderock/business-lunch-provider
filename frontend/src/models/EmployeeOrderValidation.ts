export class EmployeeOrderValidation {
  valid: boolean;
  errors: string[];
  supplierId: string;
  userDetailsId: string;
  orderId: string;


  constructor(valid: boolean, errors: string[], supplierId: string, userDetailsId: string, orderId: string) {
    this.valid = valid;
    this.errors = errors;
    this.supplierId = supplierId;
    this.userDetailsId = userDetailsId;
    this.orderId = orderId;
  }
}
