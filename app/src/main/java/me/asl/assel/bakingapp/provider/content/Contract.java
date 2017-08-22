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

    public static final long INVALID_RECIPE_ID = -1;

    public static final class RecipeEntrys implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipeIngredients";
        public static final String _ID = "id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_INGREDIENT_ID = "ingredientId";
        public static final String COLUMN_INGREDIENT_SHORT_DESC = "shortDesc";
    }
}
