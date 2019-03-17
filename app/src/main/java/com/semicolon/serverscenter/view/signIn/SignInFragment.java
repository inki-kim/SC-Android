package com.semicolon.serverscenter.view.signIn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.view.main.MainActivity;
import com.semicolon.serverscenter.view.search.PasswordResetActivity;
import com.semicolon.serverscenter.view.signUp.SignUpActivity;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignInFragment extends Fragment implements SignInContract.View {

    private SignInContract.Presenter mPresenter;

    private EditText mEditSignInEmail;
    private EditText mEditSignInPw;

    private Button mBtnSignInNext;
    private Button mBtnSignInNotId;
    private Button mBtnSignInForgetPw;

    private CheckBox mCheckSignInRemember;

    private boolean saveSignInData;
    private String signInId;
    private String signInPw;
    private SharedPreferences signInData;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_in_frag, container, false);
        setHasOptionsMenu(true);
        mEditSignInEmail = (EditText) root.findViewById(R.id.editSignInEmail);
        mEditSignInPw = (EditText) root.findViewById(R.id.editSignInPw);
        mBtnSignInNext = (Button) root.findViewById(R.id.btnSignInNext);
        mBtnSignInForgetPw = (Button) root.findViewById(R.id.btnSignInForgetPw);
        mBtnSignInNotId = (Button) root.findViewById(R.id.btnSignInNotId);

        mCheckSignInRemember = (CheckBox) root.findViewById(R.id.checkSignInRemember);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        signInData = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE);
        signInDataLoad();

        if (saveSignInData) {
            mEditSignInEmail.setText(signInId);
            mEditSignInPw.setText(signInPw);
            mCheckSignInRemember.setChecked(saveSignInData);
        }

        mBtnSignInNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mPresenter.loginRequest(mEditSignInEmail.getText().toString(), mEditSignInPw.getText().toString())) {
//                    startActivity(new Intent(getContext(), MainActivity.class));
//                }
                mBtnSignInNext.setEnabled(false);
                mBtnSignInNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey2));
                signInDataSave();
                loginEvent();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // login
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                } else {
                    // logout
                }
            }
        };

        mBtnSignInForgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PasswordResetActivity.class));
            }
        });

        mBtnSignInNotId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignUpActivity.class));
            }
        });

        return root;
    }

    @Override
    public void setPresenter(@NonNull SignInContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(mEditSignInEmail.getText().toString(), mEditSignInPw.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        mBtnSignInNext.setEnabled(true);
                        mBtnSignInNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkPurple1));
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void signInDataSave() {
        SharedPreferences.Editor editor = signInData.edit();

        editor.putBoolean("SAVE_SIGNIN_DATA", mCheckSignInRemember.isChecked());
        editor.putString("ID", mEditSignInEmail.getText().toString().trim());
        editor.putString("PW", mEditSignInPw.getText().toString().trim());

        editor.apply();
    }

    private void signInDataLoad() {
        saveSignInData = signInData.getBoolean("SAVE_SIGNIN_DATA", false);
        signInId = signInData.getString("ID", "");
        signInPw = signInData.getString("PW", "");
    }
}
