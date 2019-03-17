package com.semicolon.serverscenter.view.main;

import com.semicolon.serverscenter.view.BasePresenter;
import com.semicolon.serverscenter.view.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void setView(View view);
    }
}
