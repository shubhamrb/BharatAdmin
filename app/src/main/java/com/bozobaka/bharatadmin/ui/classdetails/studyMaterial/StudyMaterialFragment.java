package com.bozobaka.bharatadmin.ui.classdetails.studyMaterial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.NotesRecyclerViewAdapter;
import com.bozobaka.bharatadmin.adapters.StudyMaterialRecyclerViewAdapter;
import com.bozobaka.bharatadmin.common.EqualSpacingItemDecoration;
import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
import com.bozobaka.bharatadmin.ui.classdetails.AddNoteActivity;
import com.bozobaka.bharatadmin.ui.classdetails.EditNotesActivity;
import com.bozobaka.bharatadmin.viewmodels.NotesViewModel;
import com.bozobaka.bharatadmin.viewmodels.StudyMaterialViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class StudyMaterialFragment extends Fragment {

    private static final int EDIT_FOLDER_REQUEST = 2;
    private static final int EDIT_DOCUMENT_REQUEST = 3;
    private StudyMaterialRecyclerViewAdapter studyMaterialRecyclerViewAdapter;
    private List<StudyMaterialModel> studyMaterialList = new ArrayList<>();
    private static final int ADD_NOTE_REQUEST = 1;

    Unbinder unbinder;
    @BindView(R.id.rv_study_material)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add_study_material)
    FloatingActionButton addNewStudyMaterial;
    @BindView(R.id.no_item_layout)
    RelativeLayout noItemLayout;
    private String classId = null;
    LinearLayoutManager mLinearLayout;

    public static StudyMaterialFragment newInstance(String classId) {
        StudyMaterialFragment notesFragment = new StudyMaterialFragment();
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
        View root = inflater.inflate(R.layout.study_material_fragment, container, false);
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

        studyMaterialList.clear();
        studyMaterialRecyclerViewAdapter = new StudyMaterialRecyclerViewAdapter(getContext(),classId);
        recyclerView.setAdapter(studyMaterialRecyclerViewAdapter);

        studyMaterialRecyclerViewAdapter.setOnItemClickListener((position, v) -> {
            if (v.getId() == R.id.iv_edit) {
                if (studyMaterialList.get(position).getStudyMaterialType().equalsIgnoreCase("folder")){
                    Intent intent = new Intent(getActivity(), EditFolderActivity.class);
                    intent.putExtra("STUDY_MATERIAL", studyMaterialList.get(position));
                    intent.putExtra("ClassId", classId);
                    startActivityForResult(intent, EDIT_FOLDER_REQUEST);
                }else if (studyMaterialList.get(position).getStudyMaterialType().equalsIgnoreCase("document")){
                    Intent intent = new Intent(getActivity(), EditDocumentActivity.class);
                    intent.putExtra("STUDY_MATERIAL", studyMaterialList.get(position));
                    intent.putExtra("ClassId", classId);
                    startActivityForResult(intent, EDIT_DOCUMENT_REQUEST);
                }else {
                    //-----VIDEO EDIT
                }

            }
        });


        addNewStudyMaterial.setOnClickListener(view -> {
            DialogFragment dialog = StudyMaterialOptionDialog.NewInstance(classId);
            dialog.show(getActivity().getSupportFragmentManager(), "StudyMaterialOptionDialog");
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StudyMaterialViewModel studyMaterialViewModel = ViewModelProviders
                .of(this, new StudyMaterialViewModel.StudyMaterialViewModelFactory(classId))
                .get(StudyMaterialViewModel.class);
        studyMaterialViewModel.getStudyMaterial().observe(getViewLifecycleOwner(), studyMaterialModels -> {
            studyMaterialList.clear();
            studyMaterialList.addAll(studyMaterialModels);
            studyMaterialRecyclerViewAdapter.setNotesList(studyMaterialList);
            if (studyMaterialList.isEmpty()) {
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
            studyMaterialRecyclerViewAdapter.notifyDataSetChanged();
        }
        if (requestCode == EDIT_FOLDER_REQUEST && resultCode == RESULT_OK) {
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
