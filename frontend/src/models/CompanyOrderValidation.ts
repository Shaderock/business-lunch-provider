export class CompanyOrderValidation {
  valid: boolean
  errors: string[]

  constructor(valid: boolean, errors: string[]) {
    this.valid = valid;
    this.errors = errors;
  }
}
