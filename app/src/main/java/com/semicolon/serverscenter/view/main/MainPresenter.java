package com.semicolon.serverscenter.view.main;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private final MainContract.View mMainView;

    public MainPresenter(@NonNull MainContract.View mainView) {
        mMainView = checkNotNull(mainView, "main cannot be null");

        mMainView.setPresenter(this);
    }

    @Override
    public void setView(MainContract.View view) {

    }

    @Override
    public void start() {

    }
}
