package com.bozobaka.bharatadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.ClassModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassesRecyclerViewAdapter extends RecyclerView.Adapter<ClassesRecyclerViewAdapter.ClassViewHolder> {

    private List<ClassModel> classList;
    private static ClassesRecyclerViewAdapter.MyClickListener myClickListener;

    public ClassesRecyclerViewAdapter() {
        classList = new ArrayList<>();
    }

    public void setClassList(List<ClassModel> classList) {
        this.classList = classList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_list_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassModel model = classList.get(position);
        holder.tvClassName.setText(model.getClassName());
        String studentsText = model.getNumberOfStudents() + " students";
        holder.tvClassStudents.setText(studentsText);
        String liveClassText = "Live class at " + model.getNextClassTiming();
        holder.tvLiveClassIn.setText(liveClassText);
    }


    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_class_name)
        TextView tvClassName;
        @BindView(R.id.tv_class_students)
        TextView tvClassStudents;
        @BindView(R.id.tv_live_class_in)
        TextView tvLiveClassIn;
        @BindView(R.id.iv_edit)
        ImageView editClass;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            editClass.setOnClickListener(this);
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
