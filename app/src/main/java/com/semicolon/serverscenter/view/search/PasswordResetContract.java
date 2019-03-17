package com.semicolon.serverscenter.view.search;

import com.semicolon.serverscenter.view.BasePresenter;
import com.semicolon.serverscenter.view.BaseView;

public interface PasswordResetContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void setView(View view);

    }
}
