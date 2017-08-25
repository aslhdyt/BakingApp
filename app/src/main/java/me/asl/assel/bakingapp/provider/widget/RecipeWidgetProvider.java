package me.asl.assel.bakingapp.provider.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Arrays;

import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.ui.MainActivity;
import me.asl.assel.bakingapp.ui.SplashActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("WIDGET UPDATE", "Context: "+context.toString()+
            "WidgetManager: "+appWidgetManager.toString()+
            "WidgetGetIDs: "+ Arrays.toString(appWidgetIds));
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//    public static RemoteViews getListViewRemoteView(Context context) {
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
//        Intent intent = new Intent(context, ListWidgetService.class);
//        views.setRemoteAdapter(R.id.widget_listView, intent);
//        return views;
//    }
}

