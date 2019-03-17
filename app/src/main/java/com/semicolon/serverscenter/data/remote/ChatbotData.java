package com.semicolon.serverscenter.data.remote;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class ChatbotData {

    @Getter
    @Setter
    @SerializedName("result")
    private String result;

    public ChatbotData(String result) {
        this.result = result;
    }
}
