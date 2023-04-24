import moment from "moment/moment";

export class Utils {

  static dateToDateString(date: Date): string {
    return moment(date).format("YYYY/MM/DD")
  }

  static dateToTimeAsString(date: Date): string {
    return moment(date).format("HH:mm:ss")
  }

  static dateToTimeAsStringWithoutSeconds(date: Date): string {
    return moment(date).format("HH:mm")
  }

  static stringToTime(timeAsString: string): Date {
    return moment(timeAsString, 'HH:mm:ss').toDate()
  }

  static stringToTimeAsStringWithoutSeconds(timeAsString: string): string {
    return this.dateToTimeAsStringWithoutSeconds(moment(timeAsString, 'HH:mm').toDate())
  }

  static byteArrayToBase64String(array: ArrayBuffer): string {
    return btoa(new Uint8Array(array).reduce((data, byte) => data + String.fromCharCode(byte), ''));
  }
}
