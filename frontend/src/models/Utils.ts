import moment from "moment/moment";

export class Utils {
  static formatDateWithoutTimeWithDashes(date: Date): string {
    return moment(date).format("YYYY-MM-DD")
  }

  static formatDateWithoutTimeWithSlashes(date: Date): string {
    return moment(date).format("YYYY/MM/DD")
  }

  static formatDateToTimeAsString(date: Date): string {
    return moment(date).format("HH:mm:ss")
  }

  static dateToTimeAsStringWithoutSeconds(date: Date): string {
    return moment(date).format("HH:mm")
  }

  static stringToTime(timeAsString: string): Date {
    return moment(timeAsString, 'HH:mm:ss').toDate()
  }

  static stringDateToDateWithSlashesAsString(dateAsString: string) {
    return moment(dateAsString, 'YYYY/MM/DD').format('YYYY/MM/DD')
  }

  static stringToTimeAsStringWithoutSeconds(timeAsString: string): string {
    return this.dateToTimeAsStringWithoutSeconds(moment(timeAsString, 'HH:mm').toDate())
  }

  static dateAsStrAndTimeAsStrToDate(dateString: string, timeString: string): Date {
    return moment(dateString + ' ' + timeString, 'YYYY/MM/DD HH:mm:ss').toDate();
  }

  static byteArrayToBase64String(array: ArrayBuffer): string {
    return btoa(new Uint8Array(array).reduce((data, byte) => data + String.fromCharCode(byte), ''));
  }

  static uuid(): string {
    let uuidValue: string = "", k, randomValue;
    for (k = 0; k < 32; k++) {
      randomValue = Math.random() * 16 | 0;

      if (k == 8 || k == 12 || k == 16 || k == 20) {
        uuidValue += "-"
      }
      uuidValue += (k == 12 ? 4 : (k == 16 ? (randomValue & 3 | 8) : randomValue)).toString(16);
    }
    return uuidValue;
  }
}
