package me.asl.assel.bakingapp.provider.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by assel on 8/21/17.
 */

public class RecipeContentProvider extends ContentProvider {
    public static final int RECIPES = 100;
    public static final int RECIPE_ID = 101;
    public static final int INGREDIENT = 102;
    public static final int INGREDIENT_ID = 103;


    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = RecipeContentProvider.class.getName();

    // Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add URI matches
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_RECIPES + "/#", RECIPE_ID);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_INGREDIENT + "/#", INGREDIENT_ID);
        return uriMatcher;
    }
    private DBHelper dbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("QUERY", "uri: "+uri.toString()+
            "\nprojection: "+ Arrays.toString(projection)+
            "\n selection: "+ selection+
            "\n selectionArgs: "+ Arrays.toString(selectionArgs) +
            "\n sortOrder: "+sortOrder);

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;
        switch (match) {
            // Query for the plants directory
            case RECIPES:
                retCursor = db.query(Contract.RecipeEntrys.TABLE_NAME_RECIPE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPE_ID:
                id = uri.getPathSegments().get(1);
                retCursor = db.query(Contract.RecipeEntrys.TABLE_NAME_RECIPE,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT:
                retCursor = db.query(Contract.RecipeEntrys.TABLE_NAME_INGREDIENT,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT_ID:
                id = uri.getPathSegments().get(1);
                retCursor = db.query(Contract.RecipeEntrys.TABLE_NAME_INGREDIENT,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        long id;
        switch (match) {
            case RECIPES:
                // Insert new values into the database
                id = db.insert(Contract.RecipeEntrys.TABLE_NAME_RECIPE, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract.RecipeEntrys.RECIPE_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case INGREDIENT:
                id = db.insert(Contract.RecipeEntrys.TABLE_NAME_INGREDIENT, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract.RecipeEntrys.RECIPE_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted plants
        int recipeDeleted; // starts as 0
        String id;
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case RECIPE_ID:
                id = uri.getPathSegments().get(1);
                recipeDeleted = db.delete(Contract.RecipeEntrys.TABLE_NAME_RECIPE, Contract.RecipeEntrys._ID+"=?", new String[]{id});
                break;
            case INGREDIENT_ID:
                id = uri.getPathSegments().get(1);
                recipeDeleted = db.delete(Contract.RecipeEntrys.TABLE_NAME_INGREDIENT, Contract.RecipeEntrys._ID+"=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the resolver of a change and return the number of items deleted
        if (recipeDeleted != 0) {
            // A plant (or more) was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of plant deleted
        return recipeDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        // Get access to underlying database
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated plants
        int recipeUpdated;

        switch (match) {
            case RECIPES:
                recipeUpdated = db.update(Contract.RecipeEntrys.TABLE_NAME_RECIPE, contentValues, s, strings);
                break;
            case RECIPE_ID:
                if (s == null) s = Contract.RecipeEntrys._ID + "=?";
                else s += " AND " + Contract.RecipeEntrys._ID + "=?";
                // Get the place ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Append any existing selection options to the ID filter
                if (strings == null) strings = new String[]{id};
                else {
                    ArrayList<String> stringsList = new ArrayList<String>();
                    stringsList.addAll(Arrays.asList(strings));
                    stringsList.add(id);
                    strings = stringsList.toArray(new String[stringsList.size()]);
                }
                recipeUpdated = db.update(Contract.RecipeEntrys.TABLE_NAME_RECIPE, contentValues, s, strings);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items updated
        if (recipeUpdated != 0) {
            // A place (or more) was updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of places deleted
        return recipeUpdated;
    }
}
