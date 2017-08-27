package me.asl.assel.bakingapp.provider.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

import java.util.Arrays;


/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("WIDGET UPDATE", "Context: "+context.toString()+
            "WidgetManager: "+appWidgetManager.toString()+
            "WidgetGetIDs: "+ Arrays.toString(appWidgetIds));
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
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

}

