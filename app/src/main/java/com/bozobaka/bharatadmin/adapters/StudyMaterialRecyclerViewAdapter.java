package com.bozobaka.bharatadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
import com.bozobaka.bharatadmin.ui.classdetails.studyMaterial.PdfViewerActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyMaterialRecyclerViewAdapter extends RecyclerView.Adapter<StudyMaterialRecyclerViewAdapter.StudyMaterialViewHolder> {

    private List<StudyMaterialModel> studyMaterialList;
    private static StudyMaterialRecyclerViewAdapter.MyClickListener myClickListener;
    private Context context;
    private String classId;

    public StudyMaterialRecyclerViewAdapter(Context context, String classId) {
        studyMaterialList = new ArrayList<>();
        this.context=context;
        this.classId=classId;
    }

    public void setNotesList(List<StudyMaterialModel> studyMaterialList) {
        this.studyMaterialList = studyMaterialList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(StudyMaterialRecyclerViewAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public StudyMaterialRecyclerViewAdapter.StudyMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.study_material_list_item, parent, false);
        return new StudyMaterialRecyclerViewAdapter.StudyMaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyMaterialViewHolder holder, int position) {
        StudyMaterialModel model = studyMaterialList.get(position);
        holder.tvStudyMaterialName.setText(model.getStudyMaterialName());
        if (model.getStudyMaterialType().equalsIgnoreCase("video")){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.video_icon);
        }else if (model.getStudyMaterialType().equalsIgnoreCase("document")){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.doc_icon);
        }else {
            holder.icon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getStudyMaterialType().equalsIgnoreCase("video")){

                }else if (model.getStudyMaterialType().equalsIgnoreCase("document")){
                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("STUDY_MATERIAL", studyMaterialList.get(position));
                    intent.putExtra("ClassId", classId);
                    context.startActivity(intent);
                }else {

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return studyMaterialList.size();
    }

    static class StudyMaterialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_study_material_name)
        TextView tvStudyMaterialName;
        @BindView(R.id.iv_edit)
        ImageView editClass;
        @BindView(R.id.iv_icon)
        ImageView icon;

        public StudyMaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
