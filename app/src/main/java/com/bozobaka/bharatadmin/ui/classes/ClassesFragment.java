package com.bozobaka.bharatadmin.ui.classes;

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
import com.bozobaka.bharatadmin.adapters.ClassesRecyclerViewAdapter;
import com.bozobaka.bharatadmin.common.EqualSpacingItemDecoration;
import com.bozobaka.bharatadmin.models.ClassModel;
import com.bozobaka.bharatadmin.ui.classdetails.ClassDetailsActivity;
import com.bozobaka.bharatadmin.viewmodels.ClassesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class ClassesFragment extends Fragment {

    private ClassesRecyclerViewAdapter classesRecyclerViewAdapter;
    private List<ClassModel> classList = new ArrayList<>();
    private static final int ADD_CLASS_REQUEST = 1;

    Unbinder unbinder;
    @BindView(R.id.rv_classes)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add_class)
    FloatingActionButton addNewClass;
    @BindView(R.id.no_item_layout)
    RelativeLayout noItemLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_classes, container, false);
        unbinder = ButterKnife.bind(this, root);
        initUI();

        return root;
    }

    private void initUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(48,
                EqualSpacingItemDecoration.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        classList.clear();
        classesRecyclerViewAdapter = new ClassesRecyclerViewAdapter();
        recyclerView.setAdapter(classesRecyclerViewAdapter);

        classesRecyclerViewAdapter.setOnItemClickListener((position, v) -> {
            if (v.getId() == R.id.iv_edit){
                Intent intent = new Intent(getActivity(), EditClassActivity.class);
                intent.putExtra("CLASS", classList.get(position));
                startActivity(intent);
            }else {
                Intent intent = new Intent(getActivity(), ClassDetailsActivity.class);
                intent.putExtra("CLASS_ID", classList.get(position).getId());
                intent.putExtra("CLASS_NAME", classList.get(position).getClassName());
                startActivity(intent);
            }
        });


        addNewClass.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddClassActivity.class);
            startActivityForResult(intent, ADD_CLASS_REQUEST);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ClassesViewModel classesViewModel = ViewModelProviders
                .of(this, new ClassesViewModel.ClassViewModelFactory())
                .get(ClassesViewModel.class);
        classesViewModel.getClasses().observe(getViewLifecycleOwner(), classModels -> {
            classList.clear();
            classList.addAll(classModels);
            classesRecyclerViewAdapter.setClassList(classModels);
            if (classList.isEmpty()) {
                noItemLayout.setVisibility(View.VISIBLE);
            } else {
                noItemLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CLASS_REQUEST && resultCode == RESULT_OK) {

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
