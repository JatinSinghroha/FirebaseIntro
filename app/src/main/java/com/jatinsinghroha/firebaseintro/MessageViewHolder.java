package com.jatinsinghroha.firebaseintro;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * public class ViewHolder extends Recycler.ViewHolder
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView messageTV, senderDetailsTV;
    ImageView messageImageIV;

    public MessageViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        messageTV = itemView.findViewById(R.id.messageTV);
        senderDetailsTV = itemView.findViewById(R.id.senderDetails);
        messageImageIV = itemView.findViewById(R.id.messageImageIV);
    }

    public void bind(MessageModel message, boolean isItTheSameUser) {

        if (message.message != null && !message.message.isEmpty()) {
            messageImageIV.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);
            messageTV.setText(message.message);
        } else {
            messageTV.setVisibility(View.GONE);
            messageImageIV.setVisibility(View.VISIBLE);
            Glide.with(messageImageIV).load(message.picURL).into(messageImageIV);
        }

        String senderDetails = "";

        if (isItTheSameUser) {
            senderDetails = "You";
        } else if (message.senderName != null && !message.senderName.isEmpty()) {
            senderDetails = message.senderName;
        } else if (message.senderPhoneNum != null && !message.senderPhoneNum.isEmpty()) {
            senderDetails = message.senderPhoneNum;
        } else if (message.senderEmail != null && !message.senderEmail.isEmpty()) {
            senderDetails = message.senderEmail;
        }

        senderDetailsTV.setText(itemView.getContext().getResources().getString(R.string.sender_details_text, senderDetails, new SimpleDateFormat("d MMM, yyyy").format(message.sendTime)));
    }
}

/**
 * Jatin : 16/08/2021
 * +91 8950121519 : 16/08/21
 * my895012@gmail.com : 16/08/21
 */