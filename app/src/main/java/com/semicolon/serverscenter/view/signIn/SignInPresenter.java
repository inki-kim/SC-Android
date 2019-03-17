package com.semicolon.serverscenter.view.signIn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.semicolon.serverscenter.data.remote.Member;
import com.semicolon.serverscenter.data.remote.MemberService;
import com.semicolon.serverscenter.data.repository.RetrofitInstance;
import com.semicolon.serverscenter.view.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignInPresenter implements SignInContract.Presenter {

    @NonNull
    private final SignInContract.View mSignInView;

    private boolean loginRequestResult;

    public SignInPresenter(@NonNull SignInContract.View signInView) {
        mSignInView = checkNotNull(signInView, "signInView cannot be null!");

        mSignInView.setPresenter(this);
    }

    @Override
    public void setView(SignInContract.View view) {

    }

    @Override
    public void start() {

    }

    @Override
    public boolean loginRequest(String id, String pw) {
        MemberService service = RetrofitInstance.getRetrofitInstance().create(MemberService.class);
        Call<Member> call = service.getMemberLoginResult(id, pw);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");
        call.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                Log.wtf("Called Value", response.body().getStatus() + "");
                if (response.body().getStatus().equals("success")) {
                    /*Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    .
                    startActivity(intent);*/
                    loginRequestResult = true;
                } else {
                    /*Toast.makeText(SignInActivity.this, "로그인에 실패하였습니다." , Toast.LENGTH_SHORT).show();*/
                    loginRequestResult = false;
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                /*Toast.makeText(SignInActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();*/
                loginRequestResult = false;
            }
        });

        return loginRequestResult;
    }
}
