package com.bozobaka.bharatadmin.ui.classdetails;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.LiveModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewLiveClassActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.et_live_class_name)
    TextInputEditText etLiveClassName;
    @BindView(R.id.et_meeting_url)
    TextInputEditText etMeetingUrl;
    @BindView(R.id.et_meeting_id)
    TextInputEditText etMeetingId;
    @BindView(R.id.et_teacher_name)
    TextInputEditText etTeacherName;
    @BindView(R.id.spin_schedule_day)
    TextView spinScheduleDay;
    @BindView(R.id.spin_schedule_time)
    TextView spinScheduleTime;

    @BindView(R.id.btn_create_class)
    Button btnCreateClass;

    String[] days;
    boolean[] checkedDays;
    List<Integer> userSelectedDays = new ArrayList<>();
    int mHour, mMin;
    String classId;
    FirebaseFirestore db;
    FirebaseAuth auth;
    DocumentReference documentReference;
    String realTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_live_class);
        ButterKnife.bind(this);
        classId = getIntent().getStringExtra("ClassId");

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        days = getResources().getStringArray(R.array.days);
        checkedDays = new boolean[days.length];
        spinScheduleDay.setOnClickListener(view -> onDayClicked());
        spinScheduleTime.setOnClickListener(view -> onTimeClicked());
        btnCreateClass.setOnClickListener(view -> saveNewClass());
    }

    private void onTimeClicked() {
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, (timePicker, hourOfDay, min) -> {
//            Toast.makeText(this, "" + hourOfDay + " : " + min, Toast.LENGTH_SHORT).show();
            String time = "";
            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, min);
            datetime.clear(Calendar.DAY_OF_WEEK);
            datetime.clear(Calendar.YEAR);
            datetime.clear(Calendar.MONTH);
            String am_pm = "";
            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";
            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
            if (min >= 10) {
                time = strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm;
                realTime = datetime.getTime().toString().split(" ")[3];
            } else {
                time = strHrsToShow + ":0" + datetime.get(Calendar.MINUTE) + " " + am_pm;
                realTime = datetime.getTime().toString().split(" ")[3];
            }


            spinScheduleTime.setText(time);
        }, mHour, mMin, false);
        timePickerDialog.show();
    }

    private void onDayClicked() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Please select days ");
        materialAlertDialogBuilder.setMultiChoiceItems(days, checkedDays, (dialogInterface, pos, isChecked) -> {
            if (isChecked) {
                if (!userSelectedDays.contains(pos)) {
                    userSelectedDays.add(pos);
                } else {
                    userSelectedDays.remove(Integer.valueOf(pos));
                }
            } else if (userSelectedDays.contains(pos)) {
                userSelectedDays.remove(Integer.valueOf(pos));
            }
        });
        materialAlertDialogBuilder.setCancelable(true);
        materialAlertDialogBuilder.setPositiveButton("Done", (dialogInterface, which) -> {
            String item = "";
            for (int i = 0; i < userSelectedDays.size(); i++) {
                item = item + days[userSelectedDays.get(i)];
            }
            if (userSelectedDays.size() == 0) {
                spinScheduleDay.setText("Schedule Day");
            } else if (userSelectedDays.size() == 1) {
                spinScheduleDay.setText(userSelectedDays.size() + " day selected");
            } else {
                spinScheduleDay.setText(userSelectedDays.size() + " days selected");
            }

        });
        materialAlertDialogBuilder.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());
        materialAlertDialogBuilder.setNeutralButton("Clear All", (dialogInterface, w) -> {
            for (int i = 0; i < checkedDays.length; i++) {
                checkedDays[i] = false;
            }
            userSelectedDays.clear();
            spinScheduleDay.setText("Schedule Day");
        });
        AlertDialog mDialog = materialAlertDialogBuilder.create();
        mDialog.show();

    }

    private void saveNewClass() {
        String liveClassName = etLiveClassName.getText().toString();
        String meetingUrl = etMeetingUrl.getText().toString();
        String meetingId = etMeetingId.getText().toString();
        String teacherName = etTeacherName.getText().toString();
        String scheduledTime = spinScheduleTime.getText().toString();

        if (!Patterns.WEB_URL.matcher(meetingUrl).matches()) {
            Toast.makeText(this, "Please enter a valid url", Toast.LENGTH_SHORT).show();
            return;
        } else if (liveClassName.trim().isEmpty() ||
                meetingUrl.trim().isEmpty() ||
                teacherName.trim().isEmpty() || scheduledTime.trim().isEmpty() || userSelectedDays.size() == 0) {
            Toast.makeText(getApplicationContext(), "Please fill all entries", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String currentFormattedDate = df.format(c);
        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            documentReference = db.collection("classes")
                    .document(classId).collection("liveClasses").document();

            LiveModel liveModel = new LiveModel();
            liveModel.setId(documentReference.getId());
            liveModel.setLiveClassName(liveClassName);
            liveModel.setMeetingUrl(meetingUrl);
            liveModel.setMeetingId(meetingId);
            liveModel.setCreatedBy(auth.getCurrentUser().getUid());
            liveModel.setCreatedAt(currentFormattedDate);
            liveModel.setTeacherName(teacherName);
            liveModel.setMeetingId(meetingId);
            liveModel.setScheduleDay(userSelectedDays);
            liveModel.setScheduleRealTime(realTime);
            liveModel.setScheduleTime(scheduledTime);
//
            batch.set(documentReference, liveModel);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        loadingView.setVisibility(View.GONE);
                        finish();
                    })
                    .addOnFailureListener(this, e -> {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Some error occurred. Please retry!", Toast.LENGTH_SHORT).show();
                    });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
