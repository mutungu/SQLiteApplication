# SQLiteApplication

An Android app written in Java that stores contact records in a local SQLite database.

## Features
- **Add** records (name, contact number, date of birth)
- **Update** a record by tapping it in the list
- **Archive / Restore** records instead of deleting them
- **Show archived** records with a checkbox
- **Permanent delete** with a long-press and confirmation
- Duplicate names are allowed, since each record has its own auto-generated ID

## Built with
- Java
- Android SDK (API 24+)
- SQLite (`SQLiteOpenHelper`)
- `ListView` with `ArrayAdapter`

## Project structure
- `MainActivity.java`: screen logic and list handling
- `DatabaseHelper.java`: database creation and CRUD operations
- `UserRecord.java`: model for one record
- `activity_main.xml`: screen layout

## How to run
1. Clone the repository: `git clone https://github.com/YOUR-USERNAME/SQLiteApplication.git`
2. Open it in Android Studio and let Gradle sync
3. Run on an emulator or an Android device

## Author
wilson gitau
