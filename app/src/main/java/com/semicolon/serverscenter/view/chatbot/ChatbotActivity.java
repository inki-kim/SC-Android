package com.semicolon.serverscenter.view.chatbot;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.data.remote.ChatMessage;
import com.semicolon.serverscenter.data.remote.ChatbotData;
import com.semicolon.serverscenter.data.remote.ChatbotService;
import com.semicolon.serverscenter.data.repository.RetrofitInstance;
import com.semicolon.serverscenter.util.ActivityUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotActivity extends AppCompatActivity {

    private ChatbotPresenter mChatbotPresenter;

    @BindView(R.id.editMessage) EditText editMessage;
    @BindView(R.id.messagesContainer) ListView messagesContainer;
    @BindView(R.id.btnChatSend) ImageView btnChatSend;

    private ChatbotAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatbot_act);

        ButterKnife.bind(this);
        initControls();



//        ChatbotFragment chatbotFragment = (ChatbotFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.contentFrame);
//
//        if (chatbotFragment == null) {
//            chatbotFragment = ChatbotFragment.newInstance();
//
//            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
//                    chatbotFragment, R.id.contentFrame);
//        }
//
//        // Create the presenter
//        mChatbotPresenter = new ChatbotPresenter(chatbotFragment);
    }

    @OnClick(R.id.btnChatSend)
    public void chatSend() {
        String messageText = editMessage.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(122);//dummy
        chatMessage.setMessage(messageText);
        chatMessage.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
        chatMessage.setMe(true);

        editMessage.setText("");

        displayMessage(chatMessage);

        ChatbotService service = RetrofitInstance.getRetrofitInstance().create(ChatbotService.class);
        Call<ChatbotData> call = service.getChatbotData(messageText);
        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");
        call.enqueue(new Callback<ChatbotData>() {
            @Override
            public void onResponse(Call<ChatbotData> call, Response<ChatbotData> response) {
                ChatMessage message = new ChatMessage();
                message.setId(2);
                message.setMe(false);
                message.setMessage(response.body().getResult());
                message.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
                Log.wtf("Called Value", response.body().getResult() + "");
                displayMessage(message);
            }

            @Override
            public void onFailure(Call<ChatbotData> call, Throwable t) {
                Toast.makeText(ChatbotActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initControls() {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.chatContainer);
        loadDummyHistory();
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory() {
        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("ServersCenter 챗봇입니다.");
        msg.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);

        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("교내 정보에 대해 질문하세요.");
        msg1.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatbotAdapter(ChatbotActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }
}
