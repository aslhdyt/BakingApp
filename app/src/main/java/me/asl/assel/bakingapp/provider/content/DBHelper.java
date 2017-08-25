package me.asl.assel.bakingapp.provider.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_RECIPE_ID;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.TABLE_NAME_INGREDIENT;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.TABLE_NAME_RECIPE;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys._ID;

/**
 * Created by assel on 8/21/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "widgetIngredients.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 8;

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("DB", "CREATE DATABASE VER: "+DATABASE_VERSION);
        // Create a table to hold the plants data
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_NAME_RECIPE + " (" +
                Contract.RecipeEntrys._ID + " INTEGER PRIMARY KEY, " +
                Contract.RecipeEntrys.COLUMN_NAME + " TEXT NOT NULL" +
                ")";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_NAME_INGREDIENT + " (" +
                Contract.RecipeEntrys._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.RecipeEntrys.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                Contract.RecipeEntrys.COLUMN_INGREDIENT_NUM + " INTEGER, " +
                Contract.RecipeEntrys.COLUMN_INGREDIENT_ITEM + " TEXT, " +
                Contract.RecipeEntrys.COLUMN_INGREDIENT_QTY + " INTEGER, " +
                Contract.RecipeEntrys.COLUMN_INGREDIENT_MEASURE + " TEXT, " +
                "FOREIGN KEY("+COLUMN_RECIPE_ID+") " +
                "REFERENCES "+TABLE_NAME_RECIPE+"("+_ID+") " +
                "ON DELETE CASCADE" +
                ")";

        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RECIPE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INGREDIENT);
        onCreate(sqLiteDatabase);
    }
}
