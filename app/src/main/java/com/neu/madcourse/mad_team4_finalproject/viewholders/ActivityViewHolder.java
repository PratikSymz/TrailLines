package com.neu.madcourse.mad_team4_finalproject.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.apiModels.Activity;

public class ActivityViewHolder extends RecyclerView.ViewHolder {
    private TextView idText;
    private TextView nameText;
    public ActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        idText = itemView.findViewById(R.id.id_text);
        nameText = itemView.findViewById(R.id.name_text);

    }

    public void bindData(Activity activity){
        idText.setText(activity.getRecordId());
        nameText.setText(activity.getName());
    }
}
