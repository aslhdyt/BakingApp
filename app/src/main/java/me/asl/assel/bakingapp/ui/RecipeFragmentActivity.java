package me.asl.assel.bakingapp.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.model.Ingredient;
import me.asl.assel.bakingapp.ui.fragment.FragmentInterface;
import me.asl.assel.bakingapp.ui.fragment.IngredientFragment;
import me.asl.assel.bakingapp.ui.fragment.NavFragment;
import me.asl.assel.bakingapp.ui.fragment.StepsFragment;
import me.asl.assel.bakingapp.model.Recipe;

import static me.asl.assel.bakingapp.provider.content.Contract.BASE_CONTENT_URI;
import static me.asl.assel.bakingapp.provider.content.Contract.PATH_RECIPES;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_ITEM;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_MEASURE;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_NUM;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_QTY;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_NAME;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_RECIPE_ID;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.INGREDIENT_URI;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.RECIPE_URI;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys._ID;


public class RecipeFragmentActivity extends FragmentActivity implements FragmentInterface {

    private static final String TAG_RECIPE_NAV = "recipe_nav";
    private NavFragment navFragment;

    private static final String TAG_INGREDIENTS = "ingredients";
    private IngredientFragment ingredientFragment;

    private static final String TAG_STEPS = "steps";
    private StepsFragment[] stepsFragment;

    Recipe recipe;

    private int ORIENTATION_FRAME = R.id.frame_main; //frame_main for portrait



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = getIntent().getExtras().getParcelable("recipe");
        stepsFragment = new StepsFragment[recipe.getSteps().size()];

        if (savedInstanceState != null) {
            fragmentIndex = savedInstanceState.getInt(STATE_TAG, 0);
            Log.i("SAVED_STATE","index = "+fragmentIndex);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                ORIENTATION_FRAME = R.id.frame_main; //frame_main for portrait

                FragmentChange(fragmentIndex);

                Log.i("ORIENTATION", "FRAME CHANGE TO PORTRAIT");
            } else {
                ORIENTATION_FRAME = R.id.frame_content; //frame_content for landscape

                FragmentChange(fragmentIndex);

                if(navFragment == null) navFragment = NavFragment.newInstance(recipe, fragmentIndex);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_main, navFragment, TAG_RECIPE_NAV);
                ft.commit();

                Log.i("ORIENTATION", "FRAME CHANGE TO LANDSCAPE");
            }
        } else {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ORIENTATION_FRAME = R.id.frame_content; //frame_content for landscape
                FragmentChange(fragmentIndex);
            }

            if(navFragment == null) navFragment = NavFragment.newInstance(recipe, fragmentIndex);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_main, navFragment, TAG_RECIPE_NAV);
            ft.commit();
        }

    }


    Menu mOptionsMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_options, menu);
        Uri URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        Cursor cursor = getContentResolver().query(URI,
                null,
                _ID+"=?",
                new String[]{String.valueOf(recipe.getId())},
                null
        );
        //For debug
        Log.i("CURSOR SIZE", "COUNT: "+cursor.getCount());
        swapMenuOptions(cursor.getCount() > 0);
        cursor.close();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:


                ContentValues contentValues = new ContentValues();
                contentValues.put(_ID, recipe.getId());
                contentValues.put(COLUMN_NAME, recipe.getName());
                getContentResolver().insert(RECIPE_URI, contentValues);

                ArrayList<Ingredient> ingredients = recipe.getIngredients();
                int num = 0;
                for (Ingredient ingredient : ingredients) {
                    num++;
                    contentValues = new ContentValues();
                    contentValues.put(COLUMN_RECIPE_ID, recipe.getId());
                    contentValues.put(COLUMN_INGREDIENT_NUM, num);
                    contentValues.put(COLUMN_INGREDIENT_ITEM, ingredient.getIngredient());
                    contentValues.put(COLUMN_INGREDIENT_QTY, ingredient.getQuantity());
                    contentValues.put(COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
                    getContentResolver().insert(INGREDIENT_URI, contentValues);
                }
                Toast.makeText(this,
                        "Recipe & Ingredients Saved",
                        Toast.LENGTH_SHORT).show();
                swapMenuOptions(true);
                break;
            case R.id.action_unFav:
                Uri URI = ContentUris.withAppendedId(BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build(), recipe.getId());
                getContentResolver().delete(URI, null, null);
                swapMenuOptions(false);
                break;
        }
        return true;
    }

    private void swapMenuOptions (boolean isFavorite) {
        MenuItem fav = mOptionsMenu.findItem(R.id.action_fav);
        MenuItem unFav = mOptionsMenu.findItem(R.id.action_unFav);
        if (isFavorite) {
            fav.setVisible(false);
            unFav.setVisible(true);
        } else {
            fav.setVisible(true);
            unFav.setVisible(false);
        }
    }



    @Override
    public void onBackPressed() {
        NavFragment fragment = (NavFragment) getSupportFragmentManager().findFragmentByTag(TAG_RECIPE_NAV);
        if(fragment != null && fragment.isVisible()) {
            finish();
        } else {
            if(navFragment == null) navFragment = NavFragment.newInstance(recipe, fragmentIndex);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.frame_main, navFragment, TAG_RECIPE_NAV);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private static final String STATE_TAG = "currentShownIndex";
    private int fragmentIndex = 0;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_TAG,fragmentIndex);
        super.onSaveInstanceState(outState);
    }

    /*
    fragment interfaces
    */
    @Override
    public void FragmentChange(int position) {
        fragmentIndex = position;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if(position == 0) {
            if(ingredientFragment == null) ingredientFragment = IngredientFragment.newInstance(recipe.getIngredients());
            ft.replace(ORIENTATION_FRAME, ingredientFragment, TAG_INGREDIENTS);
            ft.commit();
        } else {
            fragmentIndex = position;
            if(stepsFragment[position-1] == null) stepsFragment[position-1] = StepsFragment.newInstance(recipe.getSteps().get(position-1));
            ft.replace(ORIENTATION_FRAME, stepsFragment[position-1], TAG_STEPS+position);
            ft.commit();
        }
    }

    @Override
    public void CreateNavButton(View viewReference, final int currentPos) {//currentPos index 0 is always ingredients. steps start from index 1
        Log.i("NAV BTN", "currentPos = "+currentPos);
        Button btnPrev = (Button) viewReference.findViewById(R.id.button_prev);
        Button btnNext = (Button) viewReference.findViewById(R.id.button_next);
        if(currentPos == 0) {
            btnPrev.setEnabled(false);
            btnNext.setEnabled(true);
        } else if (currentPos == recipe.getSteps().size()) {
            btnPrev.setEnabled(true);
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
        }

        if (btnNext.isEnabled()) btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChange(currentPos+1);
            }
        });
        if (btnPrev.isEnabled()) btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChange(currentPos-1);

            }
        });

    }

}
