package com.example.litteray.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litteray.databinding.ContainerForReceivedMessagesBinding;
import com.example.litteray.databinding.ContainerForReceiverImageBinding;
import com.example.litteray.databinding.ContainerForSentMessagesBinding;
import com.example.litteray.models.ChatMessage;

import java.util.List;

public class ChatAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final String senderId;
    private Bitmap receiverProfileImage;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImage(Bitmap bitmap) {
        receiverProfileImage = bitmap;
    }

    public ChatAdapters(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ContainerForSentMessagesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                )
            );
        } else {
            return new ReceivedMessageHolder(
                    ContainerForReceivedMessagesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    private Bitmap getBitmapEncoded(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ContainerForSentMessagesBinding binding;

        SentMessageViewHolder(ContainerForSentMessagesBinding containerForSentMessagesBinding) {
            super(containerForSentMessagesBinding.getRoot());
            binding = containerForSentMessagesBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.messageTime.setText(chatMessage.messageTime);
        }
    }

    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private final ContainerForReceivedMessagesBinding binding;

        ReceivedMessageHolder(ContainerForReceivedMessagesBinding containerForReceivedMessagesBinding) {
            super(containerForReceivedMessagesBinding.getRoot());
            binding = containerForReceivedMessagesBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.messageTime.setText(chatMessage.messageTime);
        }
    }
}
