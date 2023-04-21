import moment from "moment/moment";

export class Utils {
  static dateToTimeAsString(date: Date): string {
    return moment(date).format("HH:mm:ss")
  }

  static stringToTime(timeAsString: string): Date {
    return moment(timeAsString, 'HH:mm:ss').toDate()
  }
}
