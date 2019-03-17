package com.semicolon.serverscenter.data.remote;

import com.semicolon.serverscenter.data.remote.TestData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetTestDataService {

    @GET("status")
    Call<TestData> getTestData();
}
