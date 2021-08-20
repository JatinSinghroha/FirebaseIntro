package com.jatinsinghroha.firebaseintro;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * public class ViewHolder extends Recycler.ViewHolder
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView messageTV, senderDetailsTV, sendTimeTV;
    ImageView messageImageIV;

    private final static int MARGIN = 100;

    public MessageViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        messageTV = itemView.findViewById(R.id.messageTV);
        senderDetailsTV = itemView.findViewById(R.id.senderDetails);
        messageImageIV = itemView.findViewById(R.id.messageImageIV);
        sendTimeTV = itemView.findViewById(R.id.sendTime);
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

        if (isItTheSameUser) {
            senderDetailsTV.setVisibility(View.GONE);
            setMarginLeft(itemView, MARGIN);
            setMarginRight(itemView, 0);
        } else {
            senderDetailsTV.setVisibility(View.VISIBLE);
            String senderDetails = "";
            setMarginRight(itemView, MARGIN);
            setMarginLeft(itemView, 0);
            if (isItTheSameUser) {
                senderDetails = "You";
            } else if (message.senderName != null && !message.senderName.isEmpty()) {
                senderDetails = message.senderName;
            } else if (message.senderPhoneNum != null && !message.senderPhoneNum.isEmpty()) {
                senderDetails = message.senderPhoneNum;
            } else if (message.senderEmail != null && !message.senderEmail.isEmpty()) {
                senderDetails = message.senderEmail;
            }

            senderDetailsTV.setText(senderDetails);
        }
        sendTimeTV.setText(new SimpleDateFormat("d MMM, yyyy").format(message.sendTime));

    }

    public static void setMarginLeft(View view, int left) {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        params.setMargins(left, params.topMargin,
                params.rightMargin, params.bottomMargin);

        if (left > 0) {
            MaterialCardView materialCardView = (MaterialCardView) view;
            materialCardView.setCardBackgroundColor(0xffdcf8c6);
        }

        //setCardBackgroundColor(0xffdcf8c6);
        //setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public static void setMarginRight(View view, int right) {


        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(params.rightMargin, params.topMargin,
                right, params.bottomMargin);

        if (right > 0) {
            MaterialCardView materialCardView = (MaterialCardView) view;
            materialCardView.setCardBackgroundColor(0xffffffff);

            //hex = 6 or 8 characters
            // 6 = only color hex
            // 8 means alpha + color
            //0x -> To indicate Int
            //ff -> alpha
            //ffffff -> white color
        }
    }
}

/**
 * Jatin : 16/08/2021
 * +91 8950121519 : 16/08/21
 * my895012@gmail.com : 16/08/21
 */