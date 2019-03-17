package com.semicolon.serverscenter.view.signUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.util.ActivityUtils;
import com.semicolon.serverscenter.util.AndroidBug5497Workaround;

import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    private SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_act);

        statusBarTransparent();
        AndroidBug5497Workaround.assistActivity(this);
        ButterKnife.bind(this);

        SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (signUpFragment == null) {
            signUpFragment = SignUpFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    signUpFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSignUpPresenter = new SignUpPresenter(signUpFragment);
    }

    private void statusBarTransparent() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
