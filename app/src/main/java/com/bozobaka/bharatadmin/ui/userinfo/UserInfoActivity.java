package com.bozobaka.bharatadmin.ui.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.ui.auth.AuthActivity;
import com.bozobaka.bharatadmin.ui.auth.AuthCompleteActivity;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.ui.main.MainActivity;
import com.bozobaka.bharatadmin.utils.PrefUtil;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.et_user_name)
    TextInputEditText userName;
    @BindView(R.id.et_institute_name)
    TextInputEditText etInstituteName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        String loginState = PrefUtil.getFromPrefs(getApplicationContext(), "LOGIN_STATE", "UNKNOWN");
        if (loginState.contentEquals("SIGNED")) {
            startMainActivity();
        }
    }

    @OnClick(R.id.btn_next)
    public void onNextClicked() {
        String name = userName.getText().toString();
        String instituteName = etInstituteName.getText().toString();

        if (name.trim().isEmpty() ||
                instituteName.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.text_fill_all_entries),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        PrefUtil.saveToPrefs(getApplicationContext(), "USER_NAME", name);
        PrefUtil.saveToPrefs(getApplicationContext(), "INSTITUTE_NAME", instituteName);

        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
