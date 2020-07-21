package com.bozobaka.bharatadmin.ui.classes;

import android.os.Bundle;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.ClassModel;
import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.utils.PrefUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddClassActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.et_class_name)
    TextInputEditText etClassName;
    @BindView(R.id.et_category_name)
    TextInputEditText etCategoryName;
    @BindView(R.id.et_subcategory_name)
    TextInputEditText etSubCategoryName;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;

    String[] classCategories;
    int[] subCategoriesNames = {R.array.category_academics, R.array.category_coding,
            R.array.category_arts, R.array.category_health, R.array.category_others};
    int selectedCategoryPosition = -1;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);

        classCategories = getResources().getStringArray(R.array.class_categories);

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCreateClass.setOnClickListener(view -> saveNewClass());
    }

    @OnClick(R.id.et_category_name)
    public void categoryNameClicked() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Please select category ");
        materialAlertDialogBuilder.setSingleChoiceItems(classCategories, -1, (dialog, which) -> {
            selectedCategoryPosition = which;
            String selectedClassCategory = classCategories[which];
            etCategoryName.setText(selectedClassCategory);
            etSubCategoryName.setText("");
        });
        materialAlertDialogBuilder.setCancelable(true);
        materialAlertDialogBuilder.setPositiveButton("Done", (dialogInterface, which) -> {
            dialogInterface.dismiss();
        });
        AlertDialog mDialog = materialAlertDialogBuilder.create();
        mDialog.show();
    }

    @OnClick(R.id.et_subcategory_name)
    public void onSubCategoryClicked() {
        if (selectedCategoryPosition == -1) {
            Toast.makeText(getApplicationContext(), "Please select category first!", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] subCategories = getResources().getStringArray(subCategoriesNames[selectedCategoryPosition]);
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Please select subcategory ");
        materialAlertDialogBuilder.setSingleChoiceItems(subCategories, -1, (dialog, which) -> {
            String selectedSubCategory = subCategories[which];
            etSubCategoryName.setText(selectedSubCategory);
        });
        materialAlertDialogBuilder.setCancelable(true);
        materialAlertDialogBuilder.setPositiveButton("Done", (dialogInterface, which) -> {
            dialogInterface.dismiss();
        });
        AlertDialog mDialog = materialAlertDialogBuilder.create();
        mDialog.show();


    }

    private void saveNewClass() {
        String className = etClassName.getText().toString().trim();
        String category = etCategoryName.getText().toString().trim();
        String subCategory = etSubCategoryName.getText().toString().trim();

        if (className.isEmpty() ||
                category.isEmpty() ||
                subCategory.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_fill_all_entries), Toast.LENGTH_SHORT).show();
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
            DocumentReference documentReference = db.collection("classes")
                    .document();

            ClassModel classModel = new ClassModel();
            classModel.setId(documentReference.getId());
            classModel.setClassName(className);
            classModel.setNextClassTiming(null);
            classModel.setNumberOfStudents(0);
            classModel.setCreatedBy(auth.getCurrentUser().getUid());
            classModel.setCreatedAt(currentFormattedDate);
            classModel.setCategory(category);
            classModel.setSubCategory(subCategory);

            batch.set(documentReference, classModel);

            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        WriteBatch batch1 = db.batch();
                        MemberModel memberModel = new MemberModel();
                        memberModel.setUserName(PrefUtil.getFromPrefs(getApplicationContext(),
                                "USER_NAME", ""));
                        memberModel.setUserMobNo(auth.getCurrentUser().getPhoneNumber());
                        memberModel.setUserType("Teacher");
                        DocumentReference documentReference2 = db.collection("classes")
                                .document(documentReference.getId())
                                .collection("classMembers")
                                .document(auth.getCurrentUser().getPhoneNumber());
                        batch1.set(documentReference2, memberModel);
                        batch1.commit().addOnSuccessListener(AddClassActivity.this, aVoid1 -> {
                            loadingView.setVisibility(View.GONE);
                            finish();
                        });
                    })
                    .addOnFailureListener(AddClassActivity.this, e -> {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Some error occured. Please retry!", Toast.LENGTH_SHORT).show();
                    });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
