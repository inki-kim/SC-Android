package com.semicolon.serverscenter.data.remote;

import com.semicolon.serverscenter.data.remote.ChatbotData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatbotService {

    @GET("api/message")
    Call<ChatbotData> getChatbotData(@Query("msg") String message);   // message값으로 Hello를 받앗을경우 http:// ~api/message?msg=Hello 를 호출하는것과 동일 단 Query는 GET에서만 사용가능
}
