package me.asl.assel.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.asl.assel.bakingapp.provider.Contract.RecipeEntrys;

/**
 * Created by assel on 8/21/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "widgetIngredients.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the plants data
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntrys.TABLE_NAME + " (" +
                RecipeEntrys._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecipeEntrys.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeEntrys.COLUMN_INGREDIENT_ID + " INTEGER NOT NULL, " +
                RecipeEntrys.COLUMN_INGREDIENT_SHORT_DESC + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntrys.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
