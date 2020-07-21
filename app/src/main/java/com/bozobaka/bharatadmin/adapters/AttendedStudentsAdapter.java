package com.bozobaka.bharatadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.ContactModel;
import com.bozobaka.bharatadmin.views.TextDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendedStudentsAdapter extends RecyclerView
        .Adapter<AttendedStudentsAdapter
        .DataObjectHolder> {
    static int fixed = 0;
    private static AttendedStudentsAdapter.MyClickListener myClickListener;
    private static Context context;
    private List<ContactModel> students;

    public AttendedStudentsAdapter(Context context,
                                   List<ContactModel> students) {
        this.context = context;
        this.students = students;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attended_students_row_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {
        ContactModel contactModel = students.get(position);
        holder.name.setText(contactModel.getName());
        holder.time.setText("Joined at " + contactModel.getNumber());

        TextDrawable textDrawable;
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().endConfig().round();
        if (position % 5 == 0) {
            textDrawable = builder.build(contactModel.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color4));
        } else if (position % 5 == 1) {
            textDrawable = builder.build(contactModel.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color5));
        } else if (position % 5 == 2) {
            textDrawable = builder.build(contactModel.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color6));
        } else if (position % 5 == 3) {
            textDrawable = builder.build(contactModel.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color7));
        } else {
            textDrawable = builder.build(contactModel.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color8));
        }
        holder.textDrawableImg.setImageDrawable(textDrawable);


    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.iv_text_drawable)
        ImageView textDrawableImg;
        @BindView(R.id.tv_time)
        TextView time;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

}
