package com.bozobaka.bharatadmin.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.InstituteModel;
import com.bozobaka.bharatadmin.models.UserModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.utils.AppUtil;
import com.bozobaka.bharatadmin.utils.PrefUtil;
import com.bozobaka.bharatadmin.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    @BindView(R.id.tv_welcome)
    TextView welcomeText;
    @BindView(R.id.tv_info_choice)
    TextView pageDescriptionText;
    @BindView(R.id.et_mobile_no)
    EditText mobileNo;
    @BindView(R.id.et_verfication_code)
    EditText verificationCode;
    @BindView(R.id.b_submit)
    Button submitButton;
    @BindView(R.id.b_verify)
    Button verifyButton;
    @BindView(R.id.b_resend)
    Button resendButton;
    @BindView(R.id.text_policy)
    TextView privacyPolicyText;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout progressBarLayout;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    //    private String referralCode = null;
//    private String referralId = null;
    private String userMob = null;

//    AppEventsLogger logger;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Authentication OnBoarding Screen", null /* class override */);
        db = FirebaseRepository.getFirestoreInstance();
        mAuth = FirebaseRepository.getFirebaseAuthInstance();
        currentUser = mAuth.getCurrentUser();

//        if(currentUser != null && currentUser.getPhoneNumber() != null){
//            startMainActivity();
//        }

//        referralCode = getIntent().getStringExtra("REF_CODE");
//        referralId = getIntent().getStringExtra("REF_ID");
        userMob = getIntent().getStringExtra("USER_MOB");

        initUI();

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(currentUser);

        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification("+91" + mobileNo.getText().toString());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);

    }

    private void initUI() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.WHITE);
//        }

        mobileNo.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
        privacyPolicyText.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.GONE);

        if (userMob != null) {
            mobileNo.setText(userMob);
        }
        pageDescriptionText.setText(getResources().getString(R.string.text_info_choice));

        privacyPolicyText.setMovementMethod(LinkMovementMethod.getInstance());


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                mVerificationInProgress = false;

                progressBarLayout.setVisibility(View.GONE);

                updateUI(STATE_VERIFY_SUCCESS, credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                progressBarLayout.setVisibility(View.GONE);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mobileNo.setError(getResources().getString(R.string.text_invalid_number));
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded

                }

                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                progressBarLayout.setVisibility(View.GONE);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                updateUI(STATE_CODE_SENT);
            }
        };

        submitButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        resendButton.setOnClickListener(this);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }


    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                //enableViews(submitButton, mobileNo);
                disableViews(verifyButton, resendButton, verificationCode);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(verifyButton, resendButton, mobileNo, verificationCode);
                disableViews(submitButton, privacyPolicyText);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(submitButton, privacyPolicyText, verificationCode, resendButton, mobileNo,
                        verificationCode);

                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
//                disableViews(submitButton, verificationCode, resendButton, mobileNo,
//                        verificationCode);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        verificationCode.setText(cred.getSmsCode());
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                break;
            case STATE_SIGNIN_SUCCESS:
                if (user != null && user.getPhoneNumber() != null) {
                    progressBarLayout.setVisibility(View.VISIBLE);

                    UserViewModel userViewModel = ViewModelProviders
                            .of(this, new UserViewModel.UserViewModelFactory(getApplication()))
                            .get(UserViewModel.class);
                    userViewModel.getUserData().observe(this, userModel -> {

                        String instituteDefaultName = "Institute" + AppUtil.createRandomCode(6);

                        WriteBatch batch = db.batch();
                        DocumentReference documentReference = db.collection("users")
                                .document(user.getPhoneNumber());
                        DocumentReference documentReference2 = db.collection("institutes")
                                .document(user.getUid());

                        if (userModel == null) {
                            userModel = new UserModel();
                            InstituteModel instituteModel = new InstituteModel();
                            instituteModel.setId(user.getUid());
                            instituteModel.setInstituteName(PrefUtil.getFromPrefs(getApplicationContext(),
                                    "INSTITUTE_NAME", instituteDefaultName));
                            batch.set(documentReference2, instituteModel);
                        } else {
                            batch.update(documentReference2, "instituteName", PrefUtil.getFromPrefs(getApplicationContext(),
                                    "INSTITUTE_NAME", instituteDefaultName));
                        }

                        userModel.setName(PrefUtil.getFromPrefs(getApplicationContext(),
                                "USER_NAME", ""));
                        userModel.setInstituteName(PrefUtil.getFromPrefs(getApplicationContext(),
                                "INSTITUTE_NAME", instituteDefaultName));
                        userModel.setLangPref(PrefUtil.getFromPrefs(getApplicationContext(),
                                "language", ""));
                        userModel.setAppVer(getResources().getString(R.string.app_version));
                        userModel.setMsgToken(PrefUtil.getFromPrefs(getApplicationContext(),
                                "MSG_TOKEN", ""));
                        userModel.setLoginType(PrefUtil.getFromPrefs(getApplicationContext(),
                                "APP_MODE", "admin"));


                        Date c = Calendar.getInstance().getTime();
                        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        String currentFormattedDate = df.format(c);
                        userModel.setLastActiveDate(currentFormattedDate);

                        if (userModel.getReferralCode() == null ||
                                userModel.getReferralCode().isEmpty()) {
                            userModel.setReferralCode(AppUtil.createRandomCode(6));
                        }

                        batch.set(documentReference, userModel);

                        batch.commit()
                                .addOnSuccessListener(this, aVoid -> {
                                    progressBarLayout.setVisibility(View.GONE);
                                    PrefUtil.saveToPrefs(getApplicationContext(), "LOGIN_STATE",
                                            "SIGNED");
                                    startMainActivity();
                                })
                                .addOnFailureListener(this, e -> Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.text_error_saving),
                                        Toast.LENGTH_SHORT).show());
                    });


                }
                break;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthCompleteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
            v.setEnabled(false);
        }
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        progressBarLayout.setVisibility(View.VISIBLE);
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressBarLayout.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBarLayout.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                verificationCode.setError(getResources().getString(R.string.text_invalid_code));
                            }
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
                });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mobileNo.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.trim().length() < 10) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_invalid_number), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_submit:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification("+91" + mobileNo.getText().toString());
                break;
            case R.id.b_verify:
                String code = verificationCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    verificationCode.setError(getResources().getString(R.string.text_cannot_empty));
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.b_resend:
                resendVerificationCode("+91" + mobileNo.getText().toString(), mResendToken);
                break;
        }
    }

}