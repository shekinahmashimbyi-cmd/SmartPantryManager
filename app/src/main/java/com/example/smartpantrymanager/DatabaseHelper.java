package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_CATEGORY = "category";
    public static final String COL_EXPIRY = "expiry_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_QUANTITY + " INTEGER, " +
                COL_CATEGORY + " TEXT, " +
                COL_EXPIRY + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    public boolean addPantryItem(String name, int quantity,
                                 String category, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_CATEGORY, category);
        values.put(COL_EXPIRY, expiryDate);

        long result = db.insert(TABLE_PANTRY, null, values);

        return result != -1;
    }

public Cursor getAllPantryItems() {

    SQLiteDatabase db = this.getReadableDatabase();

    return db.rawQuery(
            "SELECT * FROM " + TABLE_PANTRY,
            null
    );
}
    public Cursor getPantryItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_PANTRY + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
    public boolean updatePantryItem(int id, String name, int quantity,
                                    String category, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_CATEGORY, category);
        values.put(COL_EXPIRY, expiryDate);

        int rowsAffected = db.update(
                TABLE_PANTRY,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsAffected > 0;
    }
    public boolean deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_PANTRY,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsDeleted > 0;
    }
}