package com.example.luxevista_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "LuxeVista.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_EMAIL + " TEXT UNIQUE," +
                KEY_USER_PASSWORD + " TEXT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            // Create tables again
            onCreate(db);
        }
    }

    /**
     * Register a new user with email and password
     * @param email User's email address
     * @param password User's password
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String email, String password) {
        // Check if user already exists
        if (isEmailExists(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_PASSWORD, password); // Consider hashing the password for security

        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(TABLE_USERS, null, values);
        // db.close();

        return id != -1;
    }

    /**
     * Check if the email already exists in the database
     * @param email User's email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        // db.close();

        return count > 0;
    }

    /**
     * Check if user credentials are valid
     * @param email User's email
     * @param password User's password
     * @return true if credentials are valid, false otherwise
     */
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_USER_EMAIL + " = ? AND " + KEY_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        // db.close();

        return count > 0;
    }

    /**
     * Get user ID by email
     * @param email User's email
     * @return user ID if found, -1 otherwise
     */
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        String[] columns = {KEY_USER_ID};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));
        }

        cursor.close();
        // db.close();

        return userId;
    }
}