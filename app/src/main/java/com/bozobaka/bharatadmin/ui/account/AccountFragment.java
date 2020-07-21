package com.bozobaka.bharatadmin.ui.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.utils.PrefUtil;
import com.bozobaka.bharatadmin.viewmodels.AccountViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.tv_name)
    TextView userName;
    @BindView(R.id.tv_institute_name)
    TextView instituteName;

    String userNameVal = "";
    String instituteNameVal = "";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Account Main Screen", null /* class override */);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, root);
        initUI();
        return root;
    }

    private void initUI() {
        userNameVal = PrefUtil.getFromPrefs(getActivity().getApplicationContext(),
                "USER_NAME", "");
        instituteNameVal = PrefUtil.getFromPrefs(getActivity().getApplicationContext(),
                "INSTITUTE_NAME", "");
        userName.setText(userNameVal);
        instituteName.setText(instituteNameVal);
    }

    @OnClick(R.id.view_username)
    public void onUserNameClicked() {
        DialogFragment dialog = UserNameDialog.NewInstance(userNameVal);
        dialog.show(getActivity().getSupportFragmentManager(), "UserNameDialog");
    }

    @OnClick(R.id.view_insitute_name)
    public void onInstituteNameClicked() {
        DialogFragment dialog = InstituteNameDialog.NewInstance(instituteNameVal);
        dialog.show(getActivity().getSupportFragmentManager(), "InstituteNameDialog");
    }

    @OnClick(R.id.view_chat)
    public void onCustomerChatClicked() {
        try {
            String text = "Hi Shubham, I am " + userNameVal + ". I am a user of Bharat Online app.";// Replace with your message.

            String toNumber = "+917974139917";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.view_rate_us)
    public void onRateUsClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
    }

    @OnClick(R.id.view_share)
    public void onShareClicked() {
        String msg = "Download this app to be a part of my institution.\n\n"
                + "https://play.google.com/store/apps/details?id=com.bozobaka.campus";
        // Short link created
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        AccountViewModel accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
//        accountViewModel.getText().observe(getViewLifecycleOwner(), s -> {
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
