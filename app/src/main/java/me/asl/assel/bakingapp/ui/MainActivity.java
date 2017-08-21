package me.asl.assel.bakingapp.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.ui.adapter.RecipeCardAdapter;
import me.asl.assel.bakingapp.model.Recipe;

public class MainActivity extends Activity {
    @BindView(R.id.recyclerView_main)
    RecyclerView recyclerView;

    List<Recipe> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridLayoutManager manager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager = new GridLayoutManager(this, 3);
        } else {
            manager = new GridLayoutManager(this, 1);
        }
        recyclerView.setLayoutManager(manager);

        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList(SplashActivity.DATA);
        } else {
            list = getIntent().getExtras().getParcelableArrayList(SplashActivity.DATA);
        }
        RecipeCardAdapter adapter = new RecipeCardAdapter(list);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SplashActivity.DATA, (ArrayList<? extends Parcelable>) list);
    }



}