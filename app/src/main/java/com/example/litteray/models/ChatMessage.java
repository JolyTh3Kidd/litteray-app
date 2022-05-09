package com.example.litteray.models;

import android.widget.ImageView;

import com.google.firebase.firestore.SnapshotMetadata;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverId, message, messageTime, messageImage;
    public Date dateObject;
    public String conversationId, conversationName, conversationImage;
}
