package com.semicolon.serverscenter.view.signUp;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignUpPresenter implements SignUpContract.Presenter {

    @NonNull
    private final SignUpContract.View mSignUpView;

    public SignUpPresenter(@NonNull SignUpContract.View signUpView) {
        mSignUpView = checkNotNull(signUpView, "signUpView cannot be null!"); //Google Guava 라이브러리의 Preconditions 클래스를 이용하여 유효성 검사
                                                                                                   //signUpView가 null일경우 errorMessage 출력
        mSignUpView.setPresenter(this);
    }

    @Override
    public void setView(SignUpContract.View view) {

    }


    @Override
    public void start() {

    }
}
