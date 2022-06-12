package com.example.litteray.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litteray.databinding.RecentUserContainBinding;
import com.example.litteray.listeners.ConversationListener;
import com.example.litteray.models.ChatMessage;
import com.example.litteray.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.conversationViewHolder> implements Filterable {
    private final List<ChatMessage> chatMessages;
    private final ConversationListener conversationListener;
    List<ChatMessage> chatListAll;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversationListener conversationListener) {
        this.chatMessages = chatMessages;
        this.chatListAll = new ArrayList<>(chatMessages);
        this.conversationListener = conversationListener;
    }

    @NonNull
    @Override
    public conversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new conversationViewHolder(
                RecentUserContainBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull conversationViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ChatMessage> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filterList.addAll(chatListAll);
            } else {
                for (ChatMessage chat: chatListAll) {
                    if (chat.toString().toLowerCase(Locale.ROOT).contains(charSequence.toString().toLowerCase(Locale.ROOT))) {
                        filterList.add(chat);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            chatMessages.clear();
            chatMessages.addAll((Collection<? extends ChatMessage>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class conversationViewHolder extends RecyclerView.ViewHolder {
        RecentUserContainBinding binding;

        conversationViewHolder(RecentUserContainBinding recentUserContainBinding) {
            super(recentUserContainBinding.getRoot());
            binding = recentUserContainBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.profilePic.setImageBitmap(getConversationImage(chatMessage.conversationImage));
            binding.textName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.conversationId;
                user.username = chatMessage.conversationName;
                user.image = chatMessage.conversationImage;
                conversationListener.onConversationClicked(user);
            });
        }
    }

    private Bitmap getConversationImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
