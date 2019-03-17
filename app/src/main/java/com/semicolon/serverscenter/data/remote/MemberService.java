package com.semicolon.serverscenter.data.remote;

import com.semicolon.serverscenter.data.remote.Member;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MemberService {
    @FormUrlEncoded
    @POST("api/member/login")
    Call<Member> getMemberLoginResult(@Field("email") String email, @Field("password") String password);    //@Field 형식의 경우에는 주로 POST 방식의 통신을 할때 사용

}
