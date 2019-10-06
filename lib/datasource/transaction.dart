class SmsTransaction{

  static const SMS_TRANSACTION_TABLE = "sms_transactions";
  static const SMS_TRANSACTION_ID="id";
  static const SMS_TRANSACTION_NAME="name";
  static const SMS_TRANSACTION_DATE="date";
  static const SMS_TRANSACTION_TIME="time";
  static const SMS_TRANSACTION_VALUE="value";

  static const TAG_TABLE = "tags";
  static const TAG_ID = "tag_id";
  static const TAG_VALUE = "tag_value";
  static const TAG_TRANSACTION_ID = "transaction_id";

  final int id;
  final String name;
  final int date;
  final int time;
  final List<String> tags;
  final int value;

  SmsTransaction({this.id, this.name, this.date, this.time, this.tags, this.value});

  Map<String, dynamic> toMap() {
    var map = <String, dynamic>{
      SMS_TRANSACTION_NAME: name,
      SMS_TRANSACTION_DATE: date,
      SMS_TRANSACTION_TIME: time,
      SMS_TRANSACTION_VALUE: value,
    };
    if (id != null) {
      map[SMS_TRANSACTION_ID] = id;
    }
    return map;
  }


}