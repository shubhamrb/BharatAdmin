package com.bozobaka.bharatadmin.adapters;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.ClassModel;
import com.bozobaka.bharatadmin.models.NoteModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.NoteViewHolder> {

    private List<NoteModel> notesList;
    private static NotesRecyclerViewAdapter.MyClickListener myClickListener;

    public NotesRecyclerViewAdapter() {
        notesList = new ArrayList<>();
    }

    public void setNotesList(List<NoteModel> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(NotesRecyclerViewAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public NotesRecyclerViewAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_item, parent, false);
        return new NotesRecyclerViewAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteModel model = notesList.get(position);
        holder.tvNoteName.setText(model.getNoteName());
        holder.tvNoteText.setText(model.getNoteText());
        holder.tvNoteText.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_notes_name)
        TextView tvNoteName;
        @BindView(R.id.tv_notes_text)
        TextView tvNoteText;
        @BindView(R.id.iv_edit)
        ImageView editClass;

        public NoteViewHolder(@NonNull View itemView) {
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
