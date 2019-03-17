package com.semicolon.serverscenter.view.chatbot;

import com.semicolon.serverscenter.view.BasePresenter;
import com.semicolon.serverscenter.view.BaseView;

public interface ChatbotContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void setView(View view);
    }
}
