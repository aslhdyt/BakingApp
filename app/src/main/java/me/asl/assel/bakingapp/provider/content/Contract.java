package me.asl.assel.bakingapp.provider.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by assel on 8/21/17.
 */

public class Contract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "me.asl.assel.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "plants" directory
    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENT = "ingredient";

    public static final long INVALID_RECIPE_ID = -1;

    public static final class RecipeEntrys implements BaseColumns {

        public static final Uri RECIPE_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        public static final Uri INGREDIENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        //simple table for recipe
        public static final String TABLE_NAME_RECIPE = "recipe";
        public static final String _ID = "id";
        public static final String COLUMN_NAME = "name";

        // complete data for ingredient
        public static final String TABLE_NAME_INGREDIENT = "ingredient";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_INGREDIENT_NUM = "num";
        public static final String COLUMN_INGREDIENT_ITEM = "item";
        public static final String COLUMN_INGREDIENT_QTY = "qty";
        public static final String COLUMN_INGREDIENT_MEASURE = "measure";


    }
}
