package com.semicolon.serverscenter.view.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.util.ActivityUtils;
import com.semicolon.serverscenter.util.AndroidBug5497Workaround;

import butterknife.ButterKnife;

public class PasswordResetActivity extends AppCompatActivity {

    private PasswordResetPresenter mPasswordResetPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.password_reset_act);

        statusBarTransparent();
        AndroidBug5497Workaround.assistActivity(this);

        ButterKnife.bind(this);

        PasswordResetFragment passwordResetFragment = (PasswordResetFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (passwordResetFragment == null) {
            passwordResetFragment = PasswordResetFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    passwordResetFragment, R.id.contentFrame);
        }

        // create the presenter
        mPasswordResetPresenter = new PasswordResetPresenter(passwordResetFragment);
    }

    private void statusBarTransparent() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
