package com.semicolon.serverscenter.data.remote;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class Member {

    @Getter @Setter
    @SerializedName("status")
    private String status;

    public Member(String status) {
        this.status = status;
    }
}
