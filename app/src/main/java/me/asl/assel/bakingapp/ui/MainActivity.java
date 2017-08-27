package me.asl.assel.bakingapp.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.provider.widget.ListWidgetService;
import me.asl.assel.bakingapp.ui.adapter.RecipeCardAdapter;
import me.asl.assel.bakingapp.model.Recipe;

import static me.asl.assel.bakingapp.provider.content.Contract.BASE_CONTENT_URI;
import static me.asl.assel.bakingapp.provider.content.Contract.PATH_RECIPES;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_NAME;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys._ID;

public class MainActivity extends Activity implements RecipeCardAdapter.AdapterInterface{
    @BindView(R.id.recyclerView_main)
    RecyclerView recyclerView;

    List<Recipe> list;
    private int mAppWidgetId;

    RecipeCardAdapter adapter;

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
            mAppWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                getLoaderManager().initLoader(0, null, loaderCallback);
            } else {
                list = getIntent().getExtras().getParcelableArrayList(SplashActivity.DATA);
                adapter = new RecipeCardAdapter(list);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SplashActivity.DATA, (ArrayList<? extends Parcelable>) list);
    }


    Recipe recipe;
    @Override
    public void onItemClick(int position) {
        recipe = list.get(position);
        if (mAppWidgetId == 0) {
            Intent i = new Intent(this, RecipeFragmentActivity.class);
            i.putExtra("recipe", recipe);
            startActivity(i);
        } else {
            RemoteViews remoteViews = new RemoteViews(getBaseContext().getPackageName(), R.layout.recipe_widget);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

            //setup icon for launch the app
            Intent intent = new Intent(getBaseContext(), SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_img, pendingIntent);

            //naming the widget
            remoteViews.setTextViewText(R.id.widget_recipe_name,recipe.getName()+" Ingredients");

            //listView widget
            intent = new Intent(this, ListWidgetService.class);
            intent.putExtra("recipeId", recipe.getId());
            remoteViews.setRemoteAdapter(R.id.widget_listView, intent);

            appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

            intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, intent);
            finish();

        }
    }


    LoaderManager.LoaderCallbacks<Cursor> loaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
            return new CursorLoader(getBaseContext(), URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
            list = new ArrayList<>();
            while (data.moveToNext()) {
                Recipe recipe = new Recipe();
                recipe.setId(data.getInt(data.getColumnIndex(_ID)));
                recipe.setName(data.getString(data.getColumnIndex(COLUMN_NAME)));
                list.add(recipe);
            }
            data.close();
            if (list.size() != 0) {
                adapter = new RecipeCardAdapter(list);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getBaseContext(), "No saved Recipe Found, \nPlease Favorite your recipe first from the Action Bar menu...", Toast.LENGTH_SHORT).show();
                finish();
            }


        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {

        }

    };

}
