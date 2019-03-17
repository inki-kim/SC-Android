package com.semicolon.serverscenter.view.chatbot;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatbotPresenter implements ChatbotContract.Presenter {

    @NonNull
    private final ChatbotContract.View mChatbotView;

    public ChatbotPresenter(@NonNull ChatbotContract.View chatbotView) {
        mChatbotView = checkNotNull(chatbotView, "chatbotView cannot be null!");

        mChatbotView.setPresenter(this);
    }

    @Override
    public void setView(ChatbotContract.View view) {

    }

    @Override
    public void start() {

    }
}
