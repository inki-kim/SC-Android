package com.semicolon.serverscenter.view.search;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class PasswordResetPresenter implements PasswordResetContract.Presenter {

    @NonNull
    private final PasswordResetContract.View mPasswordResetView;

    public PasswordResetPresenter(@NonNull PasswordResetContract.View passwordResetView) {
        mPasswordResetView = checkNotNull(passwordResetView, "passwordReset cannot be null");

        mPasswordResetView.setPresenter(this);
    }

    @Override
    public void setView(PasswordResetContract.View view) {

    }

    @Override
    public void start() {

    }
}