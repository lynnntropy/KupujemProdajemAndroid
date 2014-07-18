package com.omegavesko.kupujemprodajem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "appDatabase";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_LOCATIONS = "locations";

    // TABLE KEYS

    private static final String KEY_ID = "id";

    private static final String KEY_CATEGORYNAME = "categoryName";
    private static final String KEY_CATEGORYID = "categoryId";

    private static final String KEY_LOCATIONNAME = "locationName";
    private static final String KEY_LOCATIONID = "locationId";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORYNAME + " TEXT,"
                + KEY_CATEGORYID + " TEXT" + ")";

        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOCATIONNAME + " TEXT,"
                + KEY_LOCATIONID + " TEXT" + ")";

        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }

    public void addCategory(WebsiteHandler.Category category)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORYNAME, category.nameString);
        values.put(KEY_CATEGORYID, category.idString);

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void addLocation(WebsiteHandler.Location location)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIONNAME, location.nameString);
        values.put(KEY_LOCATIONID, location.idString);

        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    public WebsiteHandler.Category getCategory (int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { KEY_ID, KEY_CATEGORYNAME, KEY_CATEGORYID }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new WebsiteHandler.Category(cursor.getString(1), cursor.getString(2));
    }

    public WebsiteHandler.Location getLocation (int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { KEY_ID, KEY_LOCATIONNAME, KEY_LOCATIONID }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new WebsiteHandler.Location(cursor.getString(1), cursor.getString(2));
    }

    public List<WebsiteHandler.Category> getAllCategories()
    {
        List<WebsiteHandler.Category> categoryList = new ArrayList<WebsiteHandler.Category>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do
            {
                WebsiteHandler.Category currentCategory = new WebsiteHandler.Category(cursor.getString(1), cursor.getString(2));
                categoryList.add(currentCategory);

            }
            while (cursor.moveToNext());
        }

        return categoryList;
    }

    public List<WebsiteHandler.Location> getAllLocations()
    {
        List<WebsiteHandler.Location> locationList = new ArrayList<WebsiteHandler.Location>();

        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do
            {
                WebsiteHandler.Location currentLocation = new WebsiteHandler.Location(cursor.getString(1), cursor.getString(2));
                locationList.add(currentLocation);

            }
            while (cursor.moveToNext());
        }

        return locationList;
    }

    public int getCategoryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int getLocationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void ClearTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, null, null);
        db.delete(TABLE_LOCATIONS, null, null);
    }
}
