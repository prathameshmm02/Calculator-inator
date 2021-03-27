package com.example.calculator.Model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceholderApi {
    @GET("latest")
    Call<Currency> getCurrency();
}
