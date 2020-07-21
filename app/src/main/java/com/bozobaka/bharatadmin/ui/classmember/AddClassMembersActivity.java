package com.bozobaka.bharatadmin.ui.classmember;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.adapters.ContactsAdapter;
import com.bozobaka.bharatadmin.models.ContactModel;
import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.views.MaterialSearchView2;
import com.bozobaka.bharatadmin.views.scrollbar.AlphabetIndicator;
import com.bozobaka.bharatadmin.views.scrollbar.DragScrollBar;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddClassMembersActivity extends BaseActivity implements
        AddClassMemberDialog.AddClassMemberDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_contacts)
    RecyclerView contactsRV;
    @BindView(R.id.dragScrollBar)
    DragScrollBar dragScrollBar;
    @BindView(R.id.tv_no_contact)
    TextView numberOfContacts;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout progressBarLayout;
    @BindView(R.id.fab_done)
    FloatingActionButton fabDone;


    private List<ContactModel> contacts = new ArrayList<>();
    private Map<String, ContactModel> selectedContacts = new HashMap<>();
    private List<String> contactNumbers = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private boolean selectStudents = true;
    private ArrayList<String> membersList = new ArrayList<>();
    private String classId = null;
    DocumentReference documentReference;


    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class_members);
        ButterKnife.bind(this);

        ArrayList<String> members = getIntent().getStringArrayListExtra("MEMBERS_MOB_NO");
        classId = getIntent().getStringExtra("CLASS_ID");
        selectStudents = getIntent().getBooleanExtra("SELECT_STUDENT", true);
        if (members != null) {
            membersList.addAll(members);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        initUI();

        if (contactNumbers.size() <= 0) {
            readContacts();
            contactsAdapter.notifyDataSetChanged();
            numberOfContacts.setText(contactNumbers.size() + " contacts");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.rl_add_manually)
    public void onAddManuallyClicked() {
        DialogFragment dialog = AddClassMemberDialog.NewInstance();
        dialog.show(getSupportFragmentManager(), "AddClassMemberDialog");
    }

    @OnClick(R.id.fab_done)
    public void onDoneClicked() {
        if (selectedContacts.size() > 10) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_add_member_limit),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        List<WriteBatch> writeBatchList = new ArrayList<>();
        for (ContactModel contactModel : selectedContacts.values()) {
            WriteBatch batch = db.batch();
            String contactNumber = "+91" + contactModel.getNumber();
            MemberModel memberModel = new MemberModel();
            memberModel.setUserName(contactModel.getName());
            memberModel.setUserMobNo(contactNumber);
            memberModel.setUserType(selectStudents ? "Student" : "Teacher");
            documentReference = db.collection("classes")
                    .document(classId)
                    .collection("classMembers")
                    .document(contactNumber);
            batch.set(documentReference, memberModel);
            writeBatchList.add(batch);
        }

        progressBarLayout.setVisibility(View.VISIBLE);
        fabDone.setEnabled(false);

        WriteMemberAsyncTask writeMemberAsyncTask = new WriteMemberAsyncTask();
        writeMemberAsyncTask.execute(writeBatchList);


//        Tasks.whenAll()
//                .addOnSuccessListener(this, aVoid -> {
//
//                })
//                .addOnFailureListener(this, e -> {
//                    progressBarLayout.setVisibility(View.GONE);
//                    fabDone.setEnabled(true);
//                    Toast.makeText(getApplicationContext(),
//                            "Retry! Something went wrong.", Toast.LENGTH_SHORT).show();
//                });

    }

    @Override
    public void onAddMemberClicked(String userName, String mobileNo) {
        progressBarLayout.setVisibility(View.VISIBLE);
        fabDone.setEnabled(false);

        WriteBatch batch = db.batch();
        String contactNumber = mobileNo;
        MemberModel memberModel = new MemberModel();
        memberModel.setUserName(userName);
        memberModel.setUserMobNo(contactNumber);
        memberModel.setUserType(selectStudents ? "Student" : "Teacher");
        documentReference = db.collection("classes")
                .document(classId)
                .collection("classMembers")
                .document(contactNumber);
        batch.set(documentReference, memberModel);
        batch.commit()
                .addOnSuccessListener(this, aVoid -> {
                    progressBarLayout.setVisibility(View.GONE);
                    fabDone.setEnabled(true);
                    finish();
                })
                .addOnFailureListener(this, e -> {
                    progressBarLayout.setVisibility(View.GONE);
                    fabDone.setEnabled(true);
                    Toast.makeText(getApplicationContext(),
                            "Retry! Something went wrong.", Toast.LENGTH_SHORT).show();
                });
    }

    public class WriteMemberAsyncTask extends AsyncTask<List<WriteBatch>, Void, Void> {

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<WriteBatch>... writeBatches) {
            for (WriteBatch writeBatch : writeBatches[0]) {
                try {
                    Tasks.await(writeBatch.commit());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(() -> {
                progressBarLayout.setVisibility(View.GONE);
                fabDone.setEnabled(true);
                finish();
            });
        }
    }

    private void readContacts() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        if (cursor != null && cursor.getCount() > 0) {
            contacts.clear();
            contactNumbers.clear();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String image_uri = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                if (number == null || name == null){
                    continue;
                }

                String finalnumber = "";
                for (int i = 0; i < number.length(); i++) {
                    if (number.charAt(i) == ' ') ;
                    else finalnumber += number.charAt(i);
                }

                if (finalnumber.length() > 3 && finalnumber.charAt(0) == '+')
                    finalnumber = finalnumber.substring(3);
                else if (finalnumber.length() > 1 && finalnumber.charAt(0) == '0')
                    finalnumber = finalnumber.substring(1);

                if (user != null && user.getPhoneNumber() != null) {
                    if (!contactNumbers.contains(finalnumber) && user.getPhoneNumber().length() > 3 &&
                            !user.getPhoneNumber().substring(3).contains(finalnumber)) {

                        if (membersList.contains(finalnumber)) {
                            continue;
                        }

                        contactNumbers.add(finalnumber);
                        ContactModel contactModel = new ContactModel();
                        contactModel.setName(name);
                        contactModel.setNumber(finalnumber);
                        contacts.add(contactModel);
                    }
                } else {
                    if (!contactNumbers.contains(finalnumber)) {
                        if (membersList.contains(finalnumber)) {
                            continue;
                        }
                        contactNumbers.add(finalnumber);
                        ContactModel contactModel = new ContactModel();
                        contactModel.setName(name);
                        contactModel.setNumber(finalnumber);
                        contacts.add(contactModel);
                    }
                }

            }
            cursor.close();
            contactsAdapter.setData(contacts);
        }
    }

    private void initUI() {
        selectedContacts.clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fabDone.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
        contactsAdapter = new ContactsAdapter(this, contacts, "");
        contactsRV.setHasFixedSize(true);
        contactsRV.setLayoutManager(new LinearLayoutManager(this));
        contactsRV.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();
        dragScrollBar.setIndicator(new AlphabetIndicator(this), true);

        contactsAdapter.setOnItemClickListener((position, v) -> {
            if (contacts.get(position).isSelected()) {
                contacts.get(position).setSelected(false);
            } else {
                contacts.get(position).setSelected(true);

            }
            if (selectedContacts.containsKey(contacts.get(position).getNumber())) {
                selectedContacts.remove(contacts.get(position).getNumber());
            } else {
                selectedContacts.put(contacts.get(position).getNumber(), contacts.get(position));
            }

            boolean doneEnabled = false;
            if (selectedContacts.size() > 0) {
                doneEnabled = true;
            }
            if (doneEnabled) {
                fabDone.setVisibility(View.VISIBLE);
            } else {
                fabDone.setVisibility(View.GONE);
            }
            contactsAdapter.notifyDataSetChanged();

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_class_members, menu);

        MaterialSearchView2 searchView = (MaterialSearchView2) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView2.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactsAdapter.filter(newText);
                return false;
            }
        });

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

