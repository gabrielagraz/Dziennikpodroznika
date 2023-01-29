package com.example.dziennikpodroznika.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.dziennikpodroznika.models.PlaceModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "PlacesDatabase" // Database name
        private const val TABLE_PLACE = "PlacesTable" // Table Name
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val createTable = ("CREATE TABLE " + TABLE_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PLACE")
        onCreate(db)
    }


    fun addPlace(Place: PlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, Place.title) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, Place.image) // HappyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            Place.description
        ) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, Place.date) // HappyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, Place.location) // HappyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, Place.latitude) // HappyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, Place.longitude) // HappyPlaceModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_PLACE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun updatePlace(Place: PlaceModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, Place.title) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, Place.image) // HappyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            Place.description
        ) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, Place.date) // HappyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, Place.location) // HappyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, Place.latitude) // HappyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, Place.longitude) // HappyPlaceModelClass LONGITUDE

        // Inserting Row
        val result = db.update(
            TABLE_PLACE,
            contentValues,
            KEY_ID + "=" + Place.id,
            null
        )

        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun getPlacesList(): ArrayList<PlaceModel> {

        // A list is initialize using the data model class in which we will add the values from cursor.
        val PlaceList: ArrayList<PlaceModel> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE_PLACE" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = PlaceModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE))
                    )
                    PlaceList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return PlaceList
    }


}