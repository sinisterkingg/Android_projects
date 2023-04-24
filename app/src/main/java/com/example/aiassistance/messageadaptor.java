package com.example.aiassistance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class messageadaptor extends RecyclerView.Adapter<messageadaptor.MyViewHolder> {
    List<message> messageList;
    public messageadaptor(List<message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatview = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(chatview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        message message = messageList.get(position);
        if(message.getSend_by().equals(message.SENT_BY_ME)){
            holder.leftchatview.setVisibility(View.GONE);
            holder.rightchatview.setVisibility(View.VISIBLE);
            holder.righttextview.setText(message.getMessage());
        }else{
            holder.rightchatview.setVisibility(View.GONE);
            holder.leftchatview.setVisibility(View.VISIBLE);
            holder.lefttextview.setText(message.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftchatview, rightchatview;
        TextView lefttextview, righttextview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftchatview = itemView.findViewById(R.id.left_chat_view);
            rightchatview = itemView.findViewById(R.id.right_chat_view);
            lefttextview = itemView.findViewById(R.id.left_chat_textview);
            righttextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
