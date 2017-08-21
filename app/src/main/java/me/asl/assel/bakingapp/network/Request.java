package me.asl.assel.bakingapp.network;

import java.util.List;

import me.asl.assel.bakingapp.model.Recipe;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by assel on 7/18/17.
 */

public class Request {
    private static final String API_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private Call<List<Recipe>> call;


    public Request() {
        Retrofit.Builder retrofitBuild = new Retrofit.Builder();
        retrofitBuild.addConverterFactory(GsonConverterFactory.create());
        retrofitBuild.client(new OkHttpClient.Builder().build());
        retrofitBuild.baseUrl(API_URL);
        Retrofit retrofit = retrofitBuild.build();
        call = (retrofit.create(RequestInterface.class)).getJson();
    }

    public Call<List<Recipe>> getCall () {
        return call;
    }

    interface RequestInterface {
        @GET ("topher/2017/May/59121517_baking/baking.json")
        Call<List<Recipe>> getJson ();
    }

}
