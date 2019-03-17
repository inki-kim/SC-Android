package com.semicolon.serverscenter.view.signIn;

import com.semicolon.serverscenter.view.BasePresenter;
import com.semicolon.serverscenter.view.BaseView;

public interface SignInContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void setView(View view);

        boolean loginRequest(String id, String pw);

    }
}
