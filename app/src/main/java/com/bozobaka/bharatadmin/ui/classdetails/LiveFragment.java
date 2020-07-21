package com.bozobaka.bharatadmin.ui.classdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.LiveItemRecyclerViewAdapter;
import com.bozobaka.bharatadmin.common.EqualSpacingItemDecoration;
import com.bozobaka.bharatadmin.models.LiveModel;
import com.bozobaka.bharatadmin.viewmodels.LiveClassesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


public class LiveFragment extends Fragment {
    private LiveItemRecyclerViewAdapter liveItemRecyclerViewAdapter;
    private List<LiveModel> liveList = new ArrayList<>();
    private static final int ADD_CLASS_REQUEST = 1;

    Unbinder unbinder;
    @BindView(R.id.rv_live_classes)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add_live_class)
    FloatingActionButton addNewLiveClass;
    @BindView(R.id.no_item_layout)
    RelativeLayout noItemLayout;

    private String classId = null;

    public static LiveFragment newInstance(String classId) {
        LiveFragment liveFragment = new LiveFragment();
        Bundle b = new Bundle();
        b.putString("CLASS_ID", classId);
        liveFragment.setArguments(b);

        return liveFragment;
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
        View root = inflater.inflate(R.layout.fragment_live, container, false);
        unbinder = ButterKnife.bind(this, root);
        initUI();

        return root;
    }


    private void initUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(48,
                EqualSpacingItemDecoration.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        liveList.clear();
        liveItemRecyclerViewAdapter = new LiveItemRecyclerViewAdapter();
        recyclerView.setAdapter(liveItemRecyclerViewAdapter);
//        liveItemRecyclerViewAdapter.setOnItemClickListener((position, v) -> {
//                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireActivity());
//                materialAlertDialogBuilder.setTitle("Are you sure you want to delete? ");
//                materialAlertDialogBuilder.setCancelable(true);
//                materialAlertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> deleteLiveClass(liveList.get(position)));
//                materialAlertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
//                materialAlertDialogBuilder.create().show();
//        });

        liveItemRecyclerViewAdapter.setOnItemClickListener((position, v) -> {
            if (v.getId() == R.id.iv_edit) {
                Intent intent = new Intent(getActivity(), EditLiveClassActivity.class);
                intent.putExtra("LIVE_CLASS", liveList.get(position));
                intent.putIntegerArrayListExtra("scheduleDay", (ArrayList<Integer>) liveList.get(position).getScheduleDay());
                intent.putExtra("ClassId", classId);
                startActivity(intent);
            } else if (v.getId() == R.id.btn_join_class) {
                openMeetingUrl(liveList.get(position).getMeetingUrl());
            }
        });


        addNewLiveClass.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddNewLiveClassActivity.class);
            intent.putExtra("ClassId", classId);
            startActivityForResult(intent, ADD_CLASS_REQUEST);
        });
    }

    private void openMeetingUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveClassesViewModel liveClassesViewModel = ViewModelProviders
                .of(this, new LiveClassesViewModel.LiveClassViewModelFactory(classId))
                .get(LiveClassesViewModel.class);

        liveClassesViewModel.getLiveClasses().observe(getViewLifecycleOwner(), new Observer<List<LiveModel>>() {
            @Override
            public void onChanged(List<LiveModel> liveModels) {
                liveList.clear();
                liveList.addAll(liveModels);
                liveItemRecyclerViewAdapter.setLiveClassList(liveModels);
                if (liveList.isEmpty()) {
                    noItemLayout.setVisibility(View.VISIBLE);
                } else {
                    noItemLayout.setVisibility(View.GONE);
                }
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
