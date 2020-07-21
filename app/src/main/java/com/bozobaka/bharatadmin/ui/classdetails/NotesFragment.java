package com.bozobaka.bharatadmin.ui.classdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.NotesRecyclerViewAdapter;
import com.bozobaka.bharatadmin.common.EqualSpacingItemDecoration;
import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.viewmodels.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment {

    private static final int EDIT_NOT_REQUEST = 2;
    private NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private List<NoteModel> notesList = new ArrayList<>();
    private static final int ADD_NOTE_REQUEST = 1;

    Unbinder unbinder;
    @BindView(R.id.rv_notes)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add_note)
    FloatingActionButton addNewNote;
    @BindView(R.id.no_item_layout)
    RelativeLayout noItemLayout;
    private String classId = null;
    LinearLayoutManager mLinearLayout;

    public static NotesFragment newInstance(String classId) {
        NotesFragment notesFragment = new NotesFragment();
        Bundle b = new Bundle();
        b.putString("CLASS_ID", classId);
        notesFragment.setArguments(b);

        return notesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getString("CLASS_ID");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notes_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        initUI();

        return root;
    }

    private void initUI() {
        mLinearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayout);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(48,
                EqualSpacingItemDecoration.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        notesList.clear();
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter();
        recyclerView.setAdapter(notesRecyclerViewAdapter);

        notesRecyclerViewAdapter.setOnItemClickListener((position, v) -> {
            if (v.getId() == R.id.iv_edit) {
                Intent intent = new Intent(getActivity(), EditNotesActivity.class);
                intent.putExtra("NOTE", notesList.get(position));
                intent.putExtra("ClassId", classId);
                startActivityForResult(intent, EDIT_NOT_REQUEST);
            }
        });


        addNewNote.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddNoteActivity.class);
            intent.putExtra("ClassId", classId);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NotesViewModel notesViewModel = ViewModelProviders
                .of(this, new NotesViewModel.NotesViewModelFactory(classId))
                .get(NotesViewModel.class);
        notesViewModel.getNotes().observe(getViewLifecycleOwner(), noteModels -> {
            notesList.clear();
            notesList.addAll(noteModels);
            notesRecyclerViewAdapter.setNotesList(notesList);
            if (notesList.isEmpty()) {
                noItemLayout.setVisibility(View.VISIBLE);
            } else {
                noItemLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            notesRecyclerViewAdapter.notifyDataSetChanged();
        }
        if (requestCode == EDIT_NOT_REQUEST && resultCode == RESULT_OK) {
            // notesRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
