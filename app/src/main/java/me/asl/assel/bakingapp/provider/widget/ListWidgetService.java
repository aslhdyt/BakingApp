package me.asl.assel.bakingapp.provider.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import me.asl.assel.bakingapp.R;

import static me.asl.assel.bakingapp.provider.content.Contract.BASE_CONTENT_URI;
import static me.asl.assel.bakingapp.provider.content.Contract.PATH_INGREDIENT;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_ITEM;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_MEASURE;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_INGREDIENT_QTY;
import static me.asl.assel.bakingapp.provider.content.Contract.RecipeEntrys.COLUMN_RECIPE_ID;

/**
 * Created by assel on 8/25/17.
 */
public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int id = intent.getIntExtra("recipeId", 0);
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), id);
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Cursor mCursor;
    private int recipeId;

    ListViewRemoteViewsFactory(Context context, int recipeId) {
        this.context = context;
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate() {
        Uri URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();
        if (mCursor != null) mCursor.close();
        mCursor = context.getContentResolver().query(URI, null, COLUMN_RECIPE_ID+"=?", new String[]{String.valueOf(recipeId)}, null);
        assert mCursor != null;
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(i);

        String content = mCursor.getString(mCursor.getColumnIndex(COLUMN_INGREDIENT_ITEM));
        float qty = mCursor.getFloat(mCursor.getColumnIndex(COLUMN_INGREDIENT_QTY));
        String measure = mCursor.getString(mCursor.getColumnIndex(COLUMN_INGREDIENT_MEASURE));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
        views.setTextViewText(R.id.content, content);
        views.setTextViewText(R.id.qty, qty+" "+measure);


        Log.d("WIDGET LISTVIEW",i+". "+content + " : " + qty +":"+measure);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
