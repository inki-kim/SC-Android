package com.semicolon.serverscenter.data.remote;

import lombok.Getter;
import lombok.Setter;

public class ChatMessage {
    @Getter @Setter
    private long id;

    @Getter @Setter
    private boolean isMe;

    @Getter @Setter
    private String message;

    @Getter @Setter
    private Long userId;

    @Getter @Setter
    private String dateTime;
}
