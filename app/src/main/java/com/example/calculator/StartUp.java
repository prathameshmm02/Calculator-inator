package com.example.calculator;

import android.app.Application;

import com.example.calculator.Model.Currency;
import com.example.calculator.Model.JsonPlaceholderApi;
import com.example.calculator.Model.Rates;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartUp extends Application {
    public static Rates currentRates;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.exchangeratesapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<Currency> call = jsonPlaceholderApi.getCurrency();
        call.enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(@NotNull Call<Currency> call, @NotNull Response<Currency> response) {
                Currency currencyData = response.body();
                currentRates = currencyData.getRates();
            }

            @Override
            public void onFailure(@NotNull Call<Currency> call, @NotNull Throwable t) {
            }
        });
    }


}
