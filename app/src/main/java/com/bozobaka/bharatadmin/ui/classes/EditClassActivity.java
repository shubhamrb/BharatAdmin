package com.bozobaka.bharatadmin.ui.classes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.ClassModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditClassActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_name)
    TextView toolbarText;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.et_class_name)
    EditText etClassName;
    @BindView(R.id.et_category_name)
    EditText etCategoryName;
    @BindView(R.id.et_subcategory_name)
    EditText etSubCategoryName;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;

    String[] classCategories;
    int[] subCategoriesNames = {R.array.category_academics, R.array.category_coding,
            R.array.category_arts, R.array.category_health, R.array.category_others};
    int selectedCategoryPosition = -1;

    FirebaseFirestore db;
    FirebaseAuth auth;
    DocumentReference documentReference;

    private ClassModel classModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);

        classCategories = getResources().getStringArray(R.array.class_categories);

        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();

        classModel = getIntent().getParcelableExtra("CLASS");

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText.setText(getResources().getString(R.string.text_edit_class));
        btnCreateClass.setText(getResources().getString(R.string.text_done));

        String categoryName = classModel.getCategory();
        switch (categoryName) {
            case "Academics":
                selectedCategoryPosition = 0;
                break;
            case "Coding & Tech":
                selectedCategoryPosition = 1;
                break;
            case "Arts & Hobbies":
                selectedCategoryPosition = 2;
                break;
            case "Health & Sports":
                selectedCategoryPosition = 3;
                break;
            case "Others":
                selectedCategoryPosition = 4;
                break;
            default:
                selectedCategoryPosition = -1;
        }
        etClassName.setText(classModel.getClassName());
        etCategoryName.setText(categoryName);
        etSubCategoryName.setText(classModel.getSubCategory());
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

    @OnClick(R.id.btn_create_class)
    public void createClass() {
        String className = etClassName.getText().toString();
        String category = etCategoryName.getText().toString();
        String subCategory = etSubCategoryName.getText().toString();

        if (className.trim().isEmpty() ||
                category.trim().isEmpty() ||
                subCategory.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all entries", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            documentReference = db.collection("classes")
                    .document(classModel.getId());

            batch.update(documentReference, "className", className);
            batch.update(documentReference, "category", category);
            batch.update(documentReference, "subCategory", subCategory);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        loadingView.setVisibility(View.GONE);
                        finish();
                    })
                    .addOnFailureListener(this, e -> {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Some error occured. Please retry!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void onDeletedClicked() {
        loadingView.setVisibility(View.VISIBLE);
        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            documentReference = db.collection("classes")
                    .document(classModel.getId());

            batch.update(documentReference, "deleted", true);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        loadingView.setVisibility(View.GONE);
                        finish();
                    })
                    .addOnFailureListener(this, e -> {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Some error occured. Please retry!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
                materialAlertDialogBuilder.setTitle("Are you sure you want to delete? ");
                materialAlertDialogBuilder.setCancelable(true);
                materialAlertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> onDeletedClicked());
                materialAlertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                materialAlertDialogBuilder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
