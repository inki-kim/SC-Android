package com.semicolon.serverscenter.data.remote;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class TestData {

    @Getter @Setter
    @SerializedName("status")
    private String status;

    public TestData(String status) {
        this.status = status;
    }
}
