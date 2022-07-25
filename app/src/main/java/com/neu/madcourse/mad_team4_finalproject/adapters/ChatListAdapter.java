package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.ChatModel;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ChatListViewHolder;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
    private List<ChatModel> chatModelList;
    private Context mContext;

    public ChatListAdapter(List<ChatModel> chatModelList, Context mContext) {
        this.chatModelList = chatModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.chat_list_layout_file, parent
                ,false);
        return new ChatListViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatModel chatModel = chatModelList.get(position);
        holder.bindData(chatModel);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL + "/" + chatModel.getProfileUrl());
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(mContext).load(uri).placeholder(R.drawable.blank_profile_picture)
                .error(R.drawable.blank_profile_picture).into((ImageView) holder.itemView.findViewById(R.id.chat_profile_picture)));

    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }
}
