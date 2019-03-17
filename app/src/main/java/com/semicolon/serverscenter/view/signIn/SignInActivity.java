package com.semicolon.serverscenter.view.signIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.data.remote.Member;
import com.semicolon.serverscenter.data.remote.MemberService;
import com.semicolon.serverscenter.data.repository.RetrofitInstance;
import com.semicolon.serverscenter.util.ActivityUtils;
import com.semicolon.serverscenter.util.AndroidBug5497Workaround;
import com.semicolon.serverscenter.view.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private SignInPresenter mSignInPresenter;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in_act);

        statusBarTransparent();
        AndroidBug5497Workaround.assistActivity(this);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        ButterKnife.bind(this);

        SignInFragment signInFragment = (SignInFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (signInFragment == null) {
            signInFragment = SignInFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    signInFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSignInPresenter = new SignInPresenter(signInFragment);
    }

    private void statusBarTransparent() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}