package me.asl.assel.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.asl.assel.bakingapp.Presenter.fragment.FragmentInterface;
import me.asl.assel.bakingapp.Presenter.fragment.IngredientFragment;
import me.asl.assel.bakingapp.Presenter.fragment.NavFragment;
import me.asl.assel.bakingapp.Presenter.fragment.StepsFragment;
import me.asl.assel.bakingapp.model.Recipe;

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
            Log.d("SAVED_STATE","index = "+fragmentIndex);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                ORIENTATION_FRAME = R.id.frame_main; //frame_main for portrait

                FragmentChange(fragmentIndex);

                Log.d("ORIENTATION", "FRAME CHANGE TO PORTRAIT");
            } else {
                ORIENTATION_FRAME = R.id.frame_content; //frame_content for landscape

                FragmentChange(fragmentIndex);

                if(navFragment == null) navFragment = NavFragment.newInstance(recipe, fragmentIndex);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_main, navFragment, TAG_RECIPE_NAV);
                ft.commit();

                Log.d("ORIENTATION", "FRAME CHANGE TO LANDSCAPE");
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
        Log.d("NAV BTN", "currentPos = "+currentPos);
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
                // TODO: 8/3/17 replace Fragment steps on Next
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
