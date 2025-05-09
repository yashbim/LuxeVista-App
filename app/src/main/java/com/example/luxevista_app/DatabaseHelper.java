package com.example.luxevista_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "LuxeVista.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_BOOKINGS = "bookings";

    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_SERVICE_RESERVATIONS = "service_reservations";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_ROOM_ID = "id";
    private static final String KEY_ROOM_TYPE = "room_type";
    private static final String KEY_ROOM_PRICE = "price";
    private static final String KEY_ROOM_TOTAL = "total_rooms";

    // Booking Table Columns
    private static final String KEY_BOOKING_ID = "id";
    private static final String KEY_BOOKING_USER_ID = "user_id";
    private static final String KEY_BOOKING_ROOM_TYPE = "room_type";
    private static final String KEY_BOOKING_CHECK_IN = "check_in_date";
    private static final String KEY_BOOKING_CHECK_OUT = "check_out_date";
    private static final String KEY_BOOKING_ADULTS = "adults";
    private static final String KEY_BOOKING_CHILDREN = "children";
    private static final String KEY_BOOKING_TOTAL_PRICE = "total_price";

    // Services Table Columns
    private static final String KEY_SERVICE_ID = "id";
    private static final String KEY_SERVICE_NAME = "name";
    private static final String KEY_SERVICE_MAX_CONCURRENT = "max_concurrent";

    // Service Reservations Table Columns
    private static final String KEY_SERVICE_RESERVATION_ID = "id";
    private static final String KEY_SERVICE_RESERVATION_USER_ID = "user_id";
    private static final String KEY_SERVICE_RESERVATION_SERVICE_NAME = "service_name";
    private static final String KEY_SERVICE_RESERVATION_DATE = "reservation_date";
    private static final String KEY_SERVICE_RESERVATION_TIME = "reservation_time";


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

        String CREATE_ROOMS_TABLE = "CREATE TABLE " + TABLE_ROOMS +
                "(" +
                KEY_ROOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ROOM_TYPE + " TEXT UNIQUE," +
                KEY_ROOM_PRICE + " REAL," +
                KEY_ROOM_TOTAL + " INTEGER" +
                ")";

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS +
                "(" +
                KEY_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_BOOKING_USER_ID + " INTEGER," +
                KEY_BOOKING_ROOM_TYPE + " TEXT," +
                KEY_BOOKING_CHECK_IN + " TEXT," +
                KEY_BOOKING_CHECK_OUT + " TEXT," +
                KEY_BOOKING_ADULTS + " INTEGER," +
                KEY_BOOKING_CHILDREN + " INTEGER," +
                KEY_BOOKING_TOTAL_PRICE + " REAL," +
                "FOREIGN KEY(" + KEY_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")" +
                ")";



        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ROOMS_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);

        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES +
                "(" +
                KEY_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_SERVICE_NAME + " TEXT UNIQUE," +
                KEY_SERVICE_MAX_CONCURRENT + " INTEGER" +
                ")";

        String CREATE_SERVICE_RESERVATIONS_TABLE = "CREATE TABLE " + TABLE_SERVICE_RESERVATIONS +
                "(" +
                KEY_SERVICE_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_SERVICE_RESERVATION_USER_ID + " INTEGER," +
                KEY_SERVICE_RESERVATION_SERVICE_NAME + " TEXT," +
                KEY_SERVICE_RESERVATION_DATE + " TEXT," +
                KEY_SERVICE_RESERVATION_TIME + " TEXT," +
                "FOREIGN KEY(" + KEY_SERVICE_RESERVATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")" +
                ")";

        db.execSQL(CREATE_SERVICES_TABLE);
        db.execSQL(CREATE_SERVICE_RESERVATIONS_TABLE);

        // Initialize rooms with 5 of each type
        initializeRooms(db);

        // Initialize available services
        initializeServices(db);
    }

    /**
     * Get all bookings for a specific user
     * @param userId User ID
     * @return Cursor containing all bookings
     */
    public Cursor getUserBookings(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                "b." + KEY_BOOKING_ID + ", " +
                "b." + KEY_BOOKING_ROOM_TYPE + ", " +
                "b." + KEY_BOOKING_CHECK_IN + ", " +
                "b." + KEY_BOOKING_CHECK_OUT + ", " +
                "b." + KEY_BOOKING_ADULTS + ", " +
                "b." + KEY_BOOKING_CHILDREN + ", " +
                "b." + KEY_BOOKING_TOTAL_PRICE + ", " +
                "r." + KEY_ROOM_PRICE + " " +
                "FROM " + TABLE_BOOKINGS + " b " +
                "JOIN " + TABLE_ROOMS + " r ON b." + KEY_BOOKING_ROOM_TYPE + " = r." + KEY_ROOM_TYPE + " " +
                "WHERE b." + KEY_BOOKING_USER_ID + " = ? " +
                "ORDER BY b." + KEY_BOOKING_CHECK_IN + " DESC";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    /**
     * Check if a user has any bookings
     * @param userId User ID
     * @return true if user has bookings, false otherwise
     */
    public boolean hasBookings(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_BOOKING_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(TABLE_BOOKINGS, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();

        return count > 0;
    }

    private void initializeRooms(SQLiteDatabase db) {
        String[] roomTypes = {
                "Ocean View Suite",
                "Deluxe Room",
                "Family Villa",
                "Standard Room",
                "Mountain View Suite",
                "Executive Suite"
        };

        double[] roomPrices = {250.0, 180.0, 320.0, 120.0, 280.0, 350.0};

        for (int i = 0; i < roomTypes.length; i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_ROOM_TYPE, roomTypes[i]);
            values.put(KEY_ROOM_PRICE, roomPrices[i]);
            values.put(KEY_ROOM_TOTAL, 5); // 5 rooms of each type
            db.insert(TABLE_ROOMS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_RESERVATIONS);
            // Create tables again
            onCreate(db);
        }
    }

    /**
     * Check if a room type is available for the given dates
     * @param roomType The type of room to check
     * @param checkInDate Check-in date in MM/dd/yyyy format
     * @param checkOutDate Check-out date in MM/dd/yyyy format
     * @return Number of available rooms for the specified dates
     */
    public int getRoomAvailability(String roomType, String checkInDate, String checkOutDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        // First, get the total number of rooms of this type
        int totalRooms = 0;
        Cursor roomCursor = db.query(
                TABLE_ROOMS,
                new String[]{KEY_ROOM_TOTAL},
                KEY_ROOM_TYPE + " = ?",
                new String[]{roomType},
                null, null, null
        );

        if (roomCursor.moveToFirst()) {
            totalRooms = roomCursor.getInt(0);
        }
        roomCursor.close();

        // Then, count bookings that overlap with the requested dates
        String query = "SELECT COUNT(*) FROM " + TABLE_BOOKINGS +
                " WHERE " + KEY_BOOKING_ROOM_TYPE + " = ?" +
                " AND NOT (" + KEY_BOOKING_CHECK_OUT + " <= ? OR " + KEY_BOOKING_CHECK_IN + " >= ?)";

        Cursor bookingCursor = db.rawQuery(
                query,
                new String[]{roomType, checkInDate, checkOutDate}
        );

        int bookedRooms = 0;
        if (bookingCursor.moveToFirst()) {
            bookedRooms = bookingCursor.getInt(0);
        }
        bookingCursor.close();

        return totalRooms - bookedRooms;
    }

    /**
     * Create a new booking in the database
     * @param userId User ID making the booking
     * @param roomType Type of room being booked
     * @param checkInDate Check-in date in MM/dd/yyyy format
     * @param checkOutDate Check-out date in MM/dd/yyyy format
     * @param adults Number of adults
     * @param children Number of children
     * @param totalPrice Total booking price
     * @return true if booking successful, false otherwise
     */
    public boolean createBooking(int userId, String roomType, String checkInDate, String checkOutDate,
                                 int adults, int children, double totalPrice) {
        // First check if room is available
        if (getRoomAvailability(roomType, checkInDate, checkOutDate) <= 0) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_BOOKING_USER_ID, userId);
        values.put(KEY_BOOKING_ROOM_TYPE, roomType);
        values.put(KEY_BOOKING_CHECK_IN, checkInDate);
        values.put(KEY_BOOKING_CHECK_OUT, checkOutDate);
        values.put(KEY_BOOKING_ADULTS, adults);
        values.put(KEY_BOOKING_CHILDREN, children);
        values.put(KEY_BOOKING_TOTAL_PRICE, totalPrice);

        long id = db.insert(TABLE_BOOKINGS, null, values);
        return id != -1;
    }

    /**
     * Get room price by room type
     * @param roomType Room type
     * @return Room price
     */
    @SuppressLint("Range")
    public double getRoomPrice(String roomType) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ROOMS,
                new String[]{KEY_ROOM_PRICE},
                KEY_ROOM_TYPE + " = ?",
                new String[]{roomType},
                null, null, null
        );

        double price = 0;
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndex(KEY_ROOM_PRICE));
        }
        cursor.close();

        return price;
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
    @SuppressLint("Range")
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

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_PASSWORD, newPassword);

        // Update the password where email matches
        int result = db.update(
                TABLE_USERS,
                values,
                KEY_USER_EMAIL + " = ?",
                new String[]{email}
        );
        db.close();

        return result > 0;  // Returns true if at least one row was updated
    }


    //services

    private void initializeServices(SQLiteDatabase db) {
        String[] services = {"Spa Treatment", "Poolside Cabanas"};
        int[] maxConcurrent = {1, 1}; // Only 1 reservation at a time for each

        for (int i = 0; i < services.length; i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_SERVICE_NAME, services[i]);
            values.put(KEY_SERVICE_MAX_CONCURRENT, maxConcurrent[i]);
            db.insert(TABLE_SERVICES, null, values);
        }
    }

    /**
     * Check if a service is available at the specified date and time
     * @param service Service name
     * @param reservationDate Date in MM/dd/yyyy format
     * @param reservationTime Time in HH:mm format
     * @return true if service is available, false otherwise
     */
    public boolean isServiceAvailable(String service, String reservationDate, String reservationTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get max concurrent bookings for this service
        int maxConcurrent = 0;
        Cursor serviceCursor = db.query(
                TABLE_SERVICES,
                new String[]{KEY_SERVICE_MAX_CONCURRENT},
                KEY_SERVICE_NAME + " = ?",
                new String[]{service},
                null, null, null
        );

        if (serviceCursor.moveToFirst()) {
            maxConcurrent = serviceCursor.getInt(0);
        }
        serviceCursor.close();

        // Count existing reservations for this service, date and time
        String selection = KEY_SERVICE_RESERVATION_SERVICE_NAME + " = ? AND " +
                KEY_SERVICE_RESERVATION_DATE + " = ? AND " +
                KEY_SERVICE_RESERVATION_TIME + " = ?";
        String[] selectionArgs = {service, reservationDate, reservationTime};

        Cursor reservationCursor = db.query(
                TABLE_SERVICE_RESERVATIONS,
                null,
                selection,
                selectionArgs,
                null, null, null
        );

        int currentReservations = reservationCursor.getCount();
        reservationCursor.close();

        return currentReservations < maxConcurrent;
    }

    /**
     * Create a new service reservation
     * @param userId User ID making the reservation
     * @param service Service being reserved
     * @param reservationDate Date in MM/dd/yyyy format
     * @param reservationTime Time in HH:mm format
     * @return true if reservation successful, false otherwise
     */
    public boolean createServiceReservation(int userId, String service, String reservationDate, String reservationTime) {
        // First check if service is available
        if (!isServiceAvailable(service, reservationDate, reservationTime)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVICE_RESERVATION_USER_ID, userId);
        values.put(KEY_SERVICE_RESERVATION_SERVICE_NAME, service);
        values.put(KEY_SERVICE_RESERVATION_DATE, reservationDate);
        values.put(KEY_SERVICE_RESERVATION_TIME, reservationTime);

        long id = db.insert(TABLE_SERVICE_RESERVATIONS, null, values);
        return id != -1;
    }

    /**
     * Get all service reservations for a specific user
     * @param userId User ID
     * @return Cursor containing all service reservations
     */
    public Cursor getUserServiceReservations(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_SERVICE_RESERVATION_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String orderBy = KEY_SERVICE_RESERVATION_DATE + " ASC, " + KEY_SERVICE_RESERVATION_TIME + " ASC";

        return db.query(
                TABLE_SERVICE_RESERVATIONS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );
    }

    /**
     * Check if a user has active bookings on a specific date
     * @param userId User ID
     * @param date Date to check in MM/dd/yyyy format
     * @return true if user has bookings on that date, false otherwise
     */
    public boolean hasActiveBookingOnDate(int userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_BOOKINGS +
                " WHERE " + KEY_BOOKING_USER_ID + " = ?" +
                " AND ? BETWEEN " + KEY_BOOKING_CHECK_IN + " AND " + KEY_BOOKING_CHECK_OUT;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), date});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        return count > 0;
    }

    /**
     * Check if a user already has any service reservation at the specified date and time
     * @param userId User ID
     * @param reservationDate Date in MM/dd/yyyy format
     * @param reservationTime Time in HH:mm format
     * @return true if user already has a reservation, false otherwise
     */
    public boolean userHasServiceReservationAt(int userId, String reservationDate, String reservationTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_SERVICE_RESERVATION_USER_ID + " = ? AND " +
                KEY_SERVICE_RESERVATION_DATE + " = ? AND " +
                KEY_SERVICE_RESERVATION_TIME + " = ?";
        String[] selectionArgs = {String.valueOf(userId), reservationDate, reservationTime};

        Cursor cursor = db.query(
                TABLE_SERVICE_RESERVATIONS,
                null,
                selection,
                selectionArgs,
                null, null, null
        );

        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }




}