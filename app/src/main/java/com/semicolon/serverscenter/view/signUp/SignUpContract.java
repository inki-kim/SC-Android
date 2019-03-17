package com.semicolon.serverscenter.view.signUp;

import com.semicolon.serverscenter.view.BasePresenter;
import com.semicolon.serverscenter.view.BaseView;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void setView(View view);


    }
}
