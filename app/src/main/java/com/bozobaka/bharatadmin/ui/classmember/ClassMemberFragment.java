package com.bozobaka.bharatadmin.ui.classmember;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.ClassMemberAdapter;
import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.viewmodels.MembersViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ClassMemberFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.rv_members)
    RecyclerView membersRV;
    @BindView(R.id.tv_filter_student)
    TextView filterStudentText;
    @BindView(R.id.tv_filter_teacher)
    TextView filterTeacherText;

    @BindView(R.id.no_item_layout)
    RelativeLayout noItemLayout;

    private String classId = null;

    //    private ClassMemberPagedAdapter classMemberAdapter;
    private ClassMemberAdapter classMemberAdapter;
    private ArrayList<MemberModel> classMemberList = new ArrayList<>();
    private ArrayList<MemberModel> filterMemberList = new ArrayList<>();
    private ArrayList<String> membersMobNo = new ArrayList<>();

    public static ClassMemberFragment newInstance(String classId) {
        ClassMemberFragment classMemberFragment = new ClassMemberFragment();
        Bundle b = new Bundle();
        b.putString("CLASS_ID", classId);
        classMemberFragment.setArguments(b);

        return classMemberFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getString("CLASS_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_member, container, false);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingView.setVisibility(View.VISIBLE);
        MembersViewModel membersViewModel = ViewModelProviders
                .of(this, new MembersViewModel.ClassViewModelFactory(classId))
                .get(MembersViewModel.class);
        membersViewModel.getClasses().observe(getViewLifecycleOwner(), classMemberModel -> {
            loadingView.setVisibility(View.GONE);
            if (classMemberModel != null) {
                classMemberList.clear();
                filterMemberList.clear();
                membersMobNo.clear();
                for (int i = 0; i < classMemberModel.size(); i++) {
                    MemberModel memberModel = new MemberModel();
                    memberModel.setUserName(classMemberModel.get(i).getUserName());
                    memberModel.setUserMobNo(classMemberModel.get(i).getUserMobNo());
                    memberModel.setUserType(classMemberModel.get(i).getUserType());
                    classMemberList.add(memberModel);
                    if (classMemberModel.get(i).getUserType()
                            .toLowerCase(Locale.ENGLISH).trim().contentEquals("student")) {
                        filterMemberList.add(memberModel);
                    }

                    membersMobNo.add(classMemberModel.get(i).getUserMobNo().substring(3));
                }

                classMemberAdapter.setData(classMemberList);
                classMemberAdapter.notifyDataSetChanged();
                if (classMemberList.isEmpty()) {
                    noItemLayout.setVisibility(View.VISIBLE);
                } else {
                    noItemLayout.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initUI() {
        classMemberAdapter = new ClassMemberAdapter(getActivity(), filterMemberList);
        membersRV.setHasFixedSize(false);
        membersRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        membersRV.setAdapter(classMemberAdapter);
        classMemberAdapter.notifyDataSetChanged();

        classMemberAdapter.setOnItemClickListener((position, v) -> {
            if (v.getId() == R.id.iv_send_msg) {
                try {
                    String text = "";
                    String toNumber = "91" + filterMemberList.get(position).getUserMobNo().substring(3);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (v.getId() == R.id.iv_call) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + filterMemberList.get(position).getUserMobNo().substring(3)));
                startActivity(intent);
            } else {
                DialogFragment dialog = DeleteClassMemberDialog.NewInstance(filterMemberList.get(position).getUserName(),
                        filterMemberList.get(position).getUserMobNo());
                dialog.show(getActivity().getSupportFragmentManager(), "DeleteClassMemberDialog");
            }
        });
    }

    @OnClick(R.id.fab_add)
    public void onAddMembersClicked() {
        requestContactPermission();
    }

    @OnClick(R.id.tv_filter_student)
    public void onStudentsClicked() {
        if (classMemberList.size() > 0) {
            classMemberAdapter.filter("student");
        }
        filterStudentText.setTextColor(getResources().getColor(R.color.colorGrey));
        filterTeacherText.setTextColor(getResources().getColor(R.color.colorCard));
    }

    @OnClick(R.id.tv_filter_teacher)
    public void onTeachersClicked() {
        if (classMemberList.size() > 0) {
            classMemberAdapter.filter("teacher");
        }
        filterTeacherText.setTextColor(getResources().getColor(R.color.colorGrey));
        filterStudentText.setTextColor(getResources().getColor(R.color.colorCard));
    }

//    @OnClick(R.id.iv_filter)
//    public void onFilterMembersClicked(){
//
//    }

    private void requestContactPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            DialogFragment dialog = SelectClassMemberTypeDialog.NewInstance(membersMobNo);
                            dialog.show(getChildFragmentManager(), "SelectMemberTypeDialog");
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(
                        error -> Toast.makeText(getActivity().getApplicationContext(),
                                getResources().getString(R.string.text_error_saving), Toast.LENGTH_SHORT)
                                .show())
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(getActivity(), R.style.DatePickerTheme)
                .setTitle(getResources().getString(R.string.text_permission_required))
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.text_contact_permission_desc))
                .setPositiveButton(getResources().getString(R.string.text_app_settings), (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",
                            getActivity().getApplicationContext().getPackageName(),
                            null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialogInterface.dismiss();
                })
                .setNegativeButton(getResources().getString(R.string.text_cancel), (dialog, which) -> dialog.dismiss()).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
