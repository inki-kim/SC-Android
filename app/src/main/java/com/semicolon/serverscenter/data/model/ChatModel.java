package com.semicolon.serverscenter.data.model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public Map<String, Boolean> users = new HashMap<>(); // chat users
    public Map<String, Comment> comments = new HashMap<>(); // chatroom messages

    public static class Comment {
        public String uid;
        public String message;
        public Object timestamp;
    }
}
