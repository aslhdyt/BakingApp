package me.asl.assel.bakingapp.Presenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.model.Ingredient;

public class IngredientFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_INGREDIENT = "ingredient";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Ingredient> mIngredient = new ArrayList<Ingredient>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientFragment() {
    }

    // TODO: Customize parameter initialization
    public static IngredientFragment newInstance(int columnCount, ArrayList<Ingredient> listIngredient) {
        Log.d("TEST", "new instance called");
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelableArrayList(ARG_INGREDIENT, listIngredient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mIngredient = getArguments().getParcelableArrayList(ARG_INGREDIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new CustomRecyclerViewAdapter(mIngredient));
        }
        return view;
    }


}
