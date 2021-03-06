package me.asl.assel.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.asl.assel.bakingapp.model.Recipe;
import me.asl.assel.bakingapp.network.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*  source:
    https://www.bignerdranch.com/blog/splash-screens-the-right-way/
*/
public class SplashActivity extends AppCompatActivity {
    public static String DATA = "theMainData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (true) { // true for network json, false for local json. //todo delete this before production
            Request request = new Request();
            request.getCall().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    List<Recipe> list = response.body();
                    if (list != null) {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.putParcelableArrayListExtra(DATA, (ArrayList<? extends Parcelable>) list);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    List<Recipe> list = localJson(SplashActivity.this);
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.putParcelableArrayListExtra(DATA, (ArrayList<? extends Parcelable>) list);
                    startActivity(i);
                    finish();
                }
            });
        } else {
            List<Recipe> list = localJson(this);
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            i.putParcelableArrayListExtra(DATA, (ArrayList<? extends Parcelable>) list);
            startActivity(i);
            finish();
        }
    }




    //placeholder
    public static List<Recipe> localJson(Context context) {
        Log.d("DATA", "load from localJson");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Recipe>>(){}.getType();
        try {
            InputStream is = context.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            return gson.fromJson(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
