import 'package:flutter_family_finance_manager/datasource/transaction.dart';
import 'package:sqflite/sqflite.dart';

class TransactionDao {
  static const CreateTagTable =
      "CREATE TABLE ${SmsTransaction.SMS_TRANSACTION_TABLE}(${SmsTransaction.SMS_TRANSACTION_ID} INTEGER PRIMARY KEY, ${SmsTransaction.SMS_TRANSACTION_NAME} TEXT, ${SmsTransaction.SMS_TRANSACTION_DATE} INTEGER,${SmsTransaction.SMS_TRANSACTION_TIME} INTEGER,${SmsTransaction.SMS_TRANSACTION_VALUE} INTEGER)";
  static const CreateSmsTransactionTable =
      "CREATE TABLE ${SmsTransaction.TAG_TABLE}(${SmsTransaction.TAG_ID} INTEGER PRIMARY KEY, ${SmsTransaction.TAG_VALUE} TEXT, ${SmsTransaction.TAG_TRANSACTION_ID} INTEGER);";
  final Future<Database> _db;

  TransactionDao(this._db);

  Future<int> CreateTransaction(SmsTransaction transaction) async {
    return await (await _db).transaction<int>((databaseTransaction) async {
      var id = await databaseTransaction.insert(
          SmsTransaction.SMS_TRANSACTION_TABLE, transaction.toMap());
      var tagsId = transaction.tags.map((tag) async {
        return await databaseTransaction.insert(SmsTransaction.TAG_TABLE, {
          SmsTransaction.TAG_TRANSACTION_ID: id,
          SmsTransaction.TAG_VALUE: tag
        });
      }).toList();
      return id;
    });
  }

  Future<List<SmsTransaction>> loadTransactions() async {
    var rows = await (await _db).rawQuery(
        "SELECT * FROM ${SmsTransaction.SMS_TRANSACTION_TABLE} LEFT JOIN ${SmsTransaction.TAG_TABLE} ON ${SmsTransaction.SMS_TRANSACTION_ID} = ${SmsTransaction.TAG_TRANSACTION_ID}");
    List<SmsTransaction> result = List<SmsTransaction>();
    SmsTransaction tempSmsTransaction;
    rows.forEach((Map<String,dynamic> row) {
      var smsTransactionId = row[SmsTransaction.SMS_TRANSACTION_ID];
      if(tempSmsTransaction!= null && smsTransactionId == tempSmsTransaction.id )
        {
          tempSmsTransaction.tags.add(row[SmsTransaction.TAG_VALUE]);
        }
      else{
        tempSmsTransaction = SmsTransaction(
          id: smsTransactionId,
          name: row[SmsTransaction.SMS_TRANSACTION_NAME],
          date: row[SmsTransaction.SMS_TRANSACTION_DATE],
          time: row[SmsTransaction.SMS_TRANSACTION_TIME],
          tags: List<String>(),
          value: row[SmsTransaction.SMS_TRANSACTION_VALUE],
        );
        result.add(tempSmsTransaction);
      }
    });
    return result;
  }
}
