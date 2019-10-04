
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class AppDataBase {
  static final _instance = AppDataBase._internal();
  factory AppDataBase() => _instance;
  AppDataBase._internal();

  Database _database;

  Future<Database> _init() async {
     _database = await openDatabase(
      // Set the path to the database. Note: Using the `join` function from the
      // `path` package is best practice to ensure the path is correctly
      // constructed for each platform.
      join(await getDatabasesPath(), 'family_finance.db'),
      // When the database is first created, create a table to store dogs.
      onCreate: (db, version) {
       return db.execute(
         // TransactionDao.CreateQuery
         "Create Table test(id INTEGER PRIMARY KEY autoincrement,msg Text)"
        );
      },
      // Set the version. This executes the onCreate function and provides a
      // path to perform database upgrades and downgrades.
      version: 1,
    );
     return _database;

  }


  Future<Database> get db async {
    if (_database != null)
      return _database;

    // if _database is null we instantiate it
    _database = await _init();
    return _database;
  }



}
