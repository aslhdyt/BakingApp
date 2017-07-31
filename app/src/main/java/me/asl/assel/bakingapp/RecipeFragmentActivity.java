package me.asl.assel.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asl.assel.bakingapp.Presenter.fragment.IngredientFragment;
import me.asl.assel.bakingapp.model.Recipe;
import me.asl.assel.bakingapp.model.Step;

public class RecipeFragmentActivity extends FragmentActivity {
    @BindView(R.id.frame_layout) FrameLayout fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        Recipe recipe = getIntent().getExtras().getParcelable("recipe");
        if(savedInstanceState != null) {
            return;
        }

        IngredientFragment frag = IngredientFragment.newInstance(1, recipe.getIngredients());

        getSupportFragmentManager().beginTransaction().add(fragment_container.getId(), frag).commit();

        // TODO: 7/28/17 this code below only for checking and testing
        List<String> stepDesc = new ArrayList<>();
        stepDesc.add("Ingredient = "+recipe.getIngredients().size());
        for (Step step : recipe.getSteps()) {
            stepDesc.add(step.getShortDescription());
        }

    }


}
