package com.bozobaka.bharatadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.AttendanceListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ClassViewHolder> {

    private List<AttendanceListModel> attendanceList;
    private static AttendanceListAdapter.MyClickListener myClickListener;

    public AttendanceListAdapter() {
        attendanceList = new ArrayList<>();
    }

    public void setAttendanceList(List<AttendanceListModel> attendanceList) {
        this.attendanceList = attendanceList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        AttendanceListModel model = attendanceList.get(position);
        holder.tvClassName.setText(model.getLiveClassName());
        String studentNoText = model.getStudentNames().size() > 1 ? model.getStudentNames().size() + " students present"
                : model.getStudentNames().size() + " student present";
        holder.tvNoStudents.setText(studentNoText);
    }


    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_class_name)
        TextView tvClassName;
        @BindView(R.id.tv_no_students)
        TextView tvNoStudents;
//        @BindView(R.id.tv_live_class_in)
//        TextView tvLiveClassIn;
//        @BindView(R.id.iv_edit)
//        ImageView editClass;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
//            editClass.setOnClickListener(this);
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
