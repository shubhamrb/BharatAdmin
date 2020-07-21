package com.bozobaka.bharatadmin.ui.classdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.SectionsPagerAdapter;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.ui.classdetails.studyMaterial.StudyMaterialOptionDialog;
import com.bozobaka.bharatadmin.ui.classmember.AddClassMembersActivity;
import com.bozobaka.bharatadmin.ui.classmember.DeleteClassMemberDialog;
import com.bozobaka.bharatadmin.ui.classmember.SelectClassMemberTypeDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassDetailsActivity extends BaseActivity implements
        SelectClassMemberTypeDialog.SelectClassMemberTypeDialogListener,
        DeleteClassMemberDialog.DeleteClassMemberDialogListener,
        StudyMaterialOptionDialog.StudyMaterialOptionDialogListener {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.title)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout progressBarLayout;

    private FirebaseFirestore db;
    private FirebaseUser user;


    private String classId = null;
    private String className = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        ButterKnife.bind(this);

        db = FirebaseRepository.getFirestoreInstance();
        user = FirebaseRepository.getFirebaseAuthInstance().getCurrentUser();

        classId = getIntent().getStringExtra("CLASS_ID");
        className = getIntent().getStringExtra("CLASS_NAME");

        initUI();

    }

    private void initUI() {
        progressBarLayout.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleText.setText(className);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), classId);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onMemberTypeClicked(boolean isTypeStudent, ArrayList<String> memberList) {
        Intent intent = new Intent(this, AddClassMembersActivity.class);
        intent.putStringArrayListExtra("MEMBERS_MOB_NO", memberList);
        intent.putExtra("CLASS_ID", classId);
        intent.putExtra("SELECT_STUDENT", isTypeStudent);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onDeleteMemberClicked(String userName, String mobileNo) {
        if (mobileNo.contentEquals(user.getPhoneNumber())) {
            Toast.makeText(getApplicationContext(), "You cannot remove yourself!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        progressBarLayout.setVisibility(View.VISIBLE);
        DocumentReference documentReference = db.collection("classes")
                .document(classId)
                .collection("classMembers")
                .document(mobileNo);
        WriteBatch batch = db.batch();
        batch.delete(documentReference);
        batch.commit()
                .addOnSuccessListener(this, aVoid -> {
                    progressBarLayout.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Member is successfully deleted!",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(this, e -> {
                    progressBarLayout.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.text_error_saving), Toast.LENGTH_SHORT)
                            .show();
                });
    }

    @Override
    public void onDone(String instituteName) {

    }
}