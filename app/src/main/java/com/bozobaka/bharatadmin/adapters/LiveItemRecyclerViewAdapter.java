package com.bozobaka.bharatadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.LiveModel;
import com.bozobaka.bharatadmin.utils.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class LiveItemRecyclerViewAdapter extends RecyclerView.Adapter<LiveItemRecyclerViewAdapter.LiveItemViewHolder> {

    private List<LiveModel> liveList;
    private static LiveItemRecyclerViewAdapter.MyClickListener myClickListener;


    public LiveItemRecyclerViewAdapter() {
        liveList = new ArrayList<>();
    }

    public void setLiveClassList(List<LiveModel> liveList) {
        this.liveList = liveList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    @NonNull
    @Override
    public LiveItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_list_item, parent, false);
        return new LiveItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveItemViewHolder holder, int position) {
        LiveModel model = liveList.get(position);
        holder.tvLiveClassName.setText(model.getLiveClassName());
        holder.tvLiveClassTeacherName.setText("by " + model.getTeacherName());

        if (AppUtil.isTimeWith_in_Interval(AppUtil.getCurrentTimeInHrs(), model.getScheduleRealTime(),
                AppUtil.getHourLaterCurrentTimeInHrs(model.getScheduleRealTime()))) {
            holder.joinClass.setVisibility(View.VISIBLE);
            holder.tvLiveClassStartIn.setText(R.string.text_class_started);
        } else {
            holder.joinClass.setVisibility(View.GONE);
            if (model.getScheduleDay().contains(AppUtil.getCurrentDay()))
                holder.tvLiveClassStartIn.setText(new StringBuilder().append("starts at ").append(model.getScheduleTime()).append(" today").toString());
            else {
                holder.tvLiveClassStartIn.setText(new StringBuilder().append("starts at ").append(model.getScheduleTime())
                        .append(" on ").append(AppUtil.getNextDay(model.getScheduleDay())).toString());
            }
        }

    }


    @Override
    public int getItemCount() {
        return liveList.size();
    }

    class LiveItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_live_class_name)
        TextView tvLiveClassName;
        @BindView(R.id.tv_live_class_teacher_name)
        TextView tvLiveClassTeacherName;
        @BindView(R.id.tv_live_class_start_in)
        TextView tvLiveClassStartIn;

        @BindView(R.id.iv_edit)
        ImageView ivEdit;

        @BindView(R.id.btn_join_class)
        Button joinClass;

        public LiveItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivEdit.setOnClickListener(this);
            joinClass.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}