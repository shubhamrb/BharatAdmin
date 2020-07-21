package com.bozobaka.bharatadmin.ui.classdetails.attendance;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.AttendedStudentsAdapter;
import com.bozobaka.bharatadmin.models.ContactModel;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendedStudentsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_live_class_name)
    TextView liveClassName;
    @BindView(R.id.rv_students)
    RecyclerView studentsRV;

    private DateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
    private AttendedStudentsAdapter attendedStudentsAdapter;
    private List<String> studentNames = new ArrayList<>();
    private List<ContactModel> contactModels = new ArrayList<>();
    private String className;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attended_students);
        ButterKnife.bind(this);

        studentNames.clear();
        className = getIntent().getStringExtra("className");
        if (getIntent().getStringArrayListExtra("studentNames") != null) {
            studentNames.addAll(getIntent().getStringArrayListExtra("studentNames"));
        }

        contactModels.clear();
        for (String name : studentNames){
            ContactModel contactModel = new ContactModel();
            contactModel.setName(name.split(":")[0]);
            contactModel.setNumber(dateFormat.format(Long.parseLong(name.split(":")[1])));
            contactModels.add(contactModel);
        }

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        liveClassName.setText(className);

        attendedStudentsAdapter = new AttendedStudentsAdapter(this, contactModels);
        studentsRV.setHasFixedSize(false);
        studentsRV.setLayoutManager(new LinearLayoutManager(this));
        studentsRV.setAdapter(attendedStudentsAdapter);
        attendedStudentsAdapter.notifyDataSetChanged();

        attendedStudentsAdapter.setOnItemClickListener((position, v) -> {

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
