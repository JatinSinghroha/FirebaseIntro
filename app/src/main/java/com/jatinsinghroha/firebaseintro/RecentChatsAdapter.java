package com.jatinsinghroha.firebaseintro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsAdapter.ViewHolder> {

    List<RecentChat> mRecentChatList;

    GoToChatScreen mGoToChatScreen;

    interface GoToChatScreen {
        void openChatScreen (RecentChat recentChat);
    }

    public RecentChatsAdapter(List<RecentChat> listOfRecentChats, GoToChatScreen goToChatScreen) {
        this.mRecentChatList = listOfRecentChats;
        this.mGoToChatScreen = goToChatScreen;
    }

    @NonNull
    @NotNull
    @Override
    public RecentChatsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_char_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentChatsAdapter.ViewHolder holder, int position) {
        RecentChat recentChat = mRecentChatList.get(position);

        String name = "";

        if (recentChat.chatName == null || recentChat.chatName.trim().isEmpty()) {
            name = recentChat.chatPhoneNum;
        } else{
            name = recentChat.chatName;
        }

        holder.contactNamePhone.setText(name);
        holder.recentMessageTV.setText(recentChat.chatMessage);
        holder.recentChatTime.setText(new SimpleDateFormat("dd MMM, hh:mm").format(recentChat.chatTime));

        holder.itemView.setOnClickListener(v -> mGoToChatScreen.openChatScreen(recentChat));
    }

    @Override
    public int getItemCount() {
        return mRecentChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contactNamePhone, recentChatTime, recentMessageTV;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            contactNamePhone = itemView.findViewById(R.id.contactNamePhone);
            recentChatTime = itemView.findViewById(R.id.recentChatTime);
            recentMessageTV = itemView.findViewById(R.id.recentMessageTV);
        }
    }
}
