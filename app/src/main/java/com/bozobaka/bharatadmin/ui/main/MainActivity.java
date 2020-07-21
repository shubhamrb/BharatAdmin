package com.bozobaka.bharatadmin.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.CheckUpdateModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.account.AccountFragment;
import com.bozobaka.bharatadmin.ui.account.InstituteNameDialog;
import com.bozobaka.bharatadmin.ui.account.UserNameDialog;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.ui.classes.ClassesFragment;
import com.bozobaka.bharatadmin.utils.PrefUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        UserNameDialog.UserNameDialogListener, InstituteNameDialog.InstituteNameDialogListener,
        AppUpdateDialog.AppUpdateDialogListener{

    @BindView(R.id.nav_view)
    BottomNavigationView bottomNavigationView;
    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_name)
    TextView toolbarName;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout progressBarLayout;

    FirebaseFirestore db;
    FirebaseAuth auth;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();

        checkUpdate();

        initUI();
    }

    private void initUI() {
//        setSupportActionBar(toolbar);
        progressBarLayout.setVisibility(View.GONE);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_classes);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_classes:
                toolbarName.setText(PrefUtil.getFromPrefs(getApplicationContext(),
                        "INSTITUTE_NAME", "Bharat Online"));
                fragment = new ClassesFragment();
                break;
            case R.id.navigation_account:
                toolbarName.setText(getResources().getString(R.string.title_account));
                fragment = new AccountFragment();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onDoneClicked(String userName) {
        if (auth.getCurrentUser() != null){
            progressBarLayout.setVisibility(View.VISIBLE);
            WriteBatch batch = db.batch();
            DocumentReference docRef = db.collection("users")
                    .document(auth.getCurrentUser().getPhoneNumber());
            batch.update(docRef, "name", userName);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        PrefUtil.saveToPrefs(getApplicationContext(), "USER_NAME",
                                userName);
                        progressBarLayout.setVisibility(View.GONE);
                        loadFragment(new AccountFragment());
                    })
                    .addOnFailureListener(this, e -> {
                        progressBarLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.text_update_name_error), Toast.LENGTH_SHORT)
                                .show();
                    });
        }
    }

    @Override
    public void onInstituteNameDone(String instituteName) {
        if (auth.getCurrentUser() != null){
            progressBarLayout.setVisibility(View.VISIBLE);
            WriteBatch batch = db.batch();
            DocumentReference docRef = db.collection("users")
                    .document(auth.getCurrentUser().getPhoneNumber());
            DocumentReference documentReference2 = db.collection("institutes")
                    .document(auth.getCurrentUser().getUid());
            batch.update(docRef, "instituteName", instituteName);
            batch.update(documentReference2, "instituteName", instituteName);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        PrefUtil.saveToPrefs(getApplicationContext(), "INSTITUTE_NAME",
                                instituteName);
                        progressBarLayout.setVisibility(View.GONE);
                        loadFragment(new AccountFragment());
                    })
                    .addOnFailureListener(this, e -> {
                        progressBarLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.text_update_name_error), Toast.LENGTH_SHORT)
                                .show();
                    });
        }
    }

    private void checkUpdate() {
        db.collection("appMetaData")
                .document("checkUpdate")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CheckUpdateModel checkUpdateModel = documentSnapshot.toObject(CheckUpdateModel.class);
                        if (checkUpdateModel.getAppLatestVersion() != null &&
                                !checkUpdateModel.getAppLatestVersion().isEmpty()) {
                            int apkVersion = Integer.parseInt(getResources().getString(R.string.apk_version));
                            if (Integer.parseInt(checkUpdateModel.getAppLatestVersion()) > apkVersion) {
                                DialogFragment dialog = AppUpdateDialog.NewInstance(checkUpdateModel.getAppLatestVersion());
                                dialog.setCancelable(false);
                                dialog.show(getSupportFragmentManager(), "AppUpdateDialog");
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.text_press_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void onUpdateAppClicked(boolean doUpdate) {
        if (doUpdate) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            finish();
        } else {
            finish();
        }
    }
}
