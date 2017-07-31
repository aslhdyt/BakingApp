package me.asl.assel.bakingapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asl.assel.bakingapp.Presenter.RecipeCardAdapter;
import me.asl.assel.bakingapp.network.Request;
import me.asl.assel.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    @BindView(R.id.recyclerView_main)
    RecyclerView recyclerView;
    static String DATA = "data";

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
            list = savedInstanceState.getParcelableArrayList(DATA);
            RecipeCardAdapter adapter = new RecipeCardAdapter(list);
            recyclerView.setAdapter(adapter);
        } else {
            Request request = new Request();
            request.getCall().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    list = response.body();
                    if (list != null) {
                        for (Recipe recipe : list) {
                            Log.d("RESPONSE", new Gson().toJson(recipe));
                        }
                        RecipeCardAdapter adapter = new RecipeCardAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DATA, (ArrayList<? extends Parcelable>) list);
    }
}
