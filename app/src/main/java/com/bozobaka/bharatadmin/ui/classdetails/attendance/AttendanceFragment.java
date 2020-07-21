package com.bozobaka.bharatadmin.ui.classdetails.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.AttendanceListAdapter;
import com.bozobaka.bharatadmin.common.EqualSpacingItemDecoration;
import com.bozobaka.bharatadmin.models.AttendanceListModel;
import com.bozobaka.bharatadmin.models.MarkAttendanceModel;
import com.bozobaka.bharatadmin.viewmodels.AttendanceViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.rv_attendance)
    RecyclerView attendanceRV;
    @BindView(R.id.tv_date)
    TextView dateText;

    @BindView(R.id.no_item_layout)
    RelativeLayout noItemLayout;

    private DatePickerDialog datePickerDialog;
    private String[] monthsName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private Date c;
    private Map<String, AttendanceListModel> attendanceList = new HashMap<>();
    private List<AttendanceListModel> listAttendance = new ArrayList<>();
    private AttendanceListAdapter attendanceListAdapter;
    private String classId = null;
    private AttendanceViewModel classesViewModel;
    private final Observer<List<MarkAttendanceModel>> observer = new Observer<List<MarkAttendanceModel>>() {
        @Override
        public void onChanged(List<MarkAttendanceModel> attendanceModels) {
            attendanceList.clear();
            for (MarkAttendanceModel model : attendanceModels) {
                if (attendanceList.containsKey(model.getLiveClassId())) {
                    attendanceList.get(model.getLiveClassId()).getStudentNames().add(model.getStudentName() + ":" + model.getAttendedUnixTime());
                } else {
                    AttendanceListModel attendanceListModel = new AttendanceListModel();
                    attendanceListModel.setDate(model.getDate());
                    attendanceListModel.setLiveClassId(model.getLiveClassId());
                    attendanceListModel.setLiveClassName(model.getLiveClassName());
                    List<String> studentNames = new ArrayList<>();
                    studentNames.add(model.getStudentName() + ":" + model.getAttendedUnixTime());
                    attendanceListModel.setStudentNames(studentNames);
                    attendanceList.put(model.getLiveClassId(), attendanceListModel);
                }
            }


            listAttendance.clear();
            for (Map.Entry<String, AttendanceListModel> entry : attendanceList.entrySet()) {
                listAttendance.add(entry.getValue());
            }

            attendanceListAdapter.setAttendanceList(listAttendance);
            if (attendanceList.isEmpty()) {
                noItemLayout.setVisibility(View.VISIBLE);
            } else {
                noItemLayout.setVisibility(View.GONE);
            }
        }
    };

    public static AttendanceFragment newInstance(String classId) {
        AttendanceFragment attendanceFragment = new AttendanceFragment();
        Bundle b = new Bundle();
        b.putString("CLASS_ID", classId);
        attendanceFragment.setArguments(b);

        return attendanceFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getString("CLASS_ID");
        }

        c = Calendar.getInstance().getTime();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classesViewModel = ViewModelProviders
                .of(this, new AttendanceViewModel.AttendanceViewModelFactory(classId, df.format(c)))
                .get(AttendanceViewModel.class);
        classesViewModel.setDateId(dateText.getText().toString().trim());
        classesViewModel.getAttendance().observe(getViewLifecycleOwner(), observer);
    }

    private void initUI() {
        dateText.setText(df.format(c));

        attendanceListAdapter = new AttendanceListAdapter();
        attendanceRV.setHasFixedSize(false);
        attendanceRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        attendanceRV.addItemDecoration(new EqualSpacingItemDecoration(48,
                EqualSpacingItemDecoration.VERTICAL, false));
        attendanceRV.setAdapter(attendanceListAdapter);
        attendanceListAdapter.notifyDataSetChanged();

        attendanceListAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(getActivity(), AttendedStudentsActivity.class);
            intent.putExtra("className", listAttendance.get(position).getLiveClassName());
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.addAll(listAttendance.get(position).getStudentNames());
            intent.putStringArrayListExtra("studentNames", arrayList);
            startActivity(intent);
        });
    }

    @OnClick(R.id.tv_date)
    public void onDateClicked() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String month1 = monthsName[monthOfYear];
                    String dayVal = "";
                    if (dayOfMonth < 10) {
                        dayVal = "0" + dayOfMonth;
                    } else {
                        dayVal = "" + dayOfMonth;
                    }
                    dateText.setText(dayVal + " " + month1 + " " + year1);

                    String dateVal = dateText.getText().toString().trim();
                    classesViewModel.setDateId(dateVal);
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
