package com.semicolon.serverscenter.view.chatbot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.serverscenter.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatbotFragment extends Fragment implements ChatbotContract.View {

    private ChatbotContract.Presenter mPresenter;

    public static ChatbotFragment newInstance() {
        ChatbotFragment fragment = new ChatbotFragment();
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
        View root = inflater.inflate(R.layout.chatbot_frag, container, false);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void setPresenter(ChatbotContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
