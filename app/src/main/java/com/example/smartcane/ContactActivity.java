package com.example.smartcane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcane.adapter.MainAdapter;
import com.example.smartcane.model.ModelContact;
import com.example.smartcane.model.ModelContact;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContactActivity extends AppCompatActivity implements MainAdapter.FirebaseDataListener{


    private static final int REQUEST_CALL =1;
    Button buttonCall, buttonCall2;


    private ExtendedFloatingActionButton mFloatingActionButton;
    private EditText mEditName;
    private EditText mEditRelation;
    private EditText mEditNumber;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<ModelContact> daftarContact;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;



    private TextView nameTv;
    private ImageButton logoutBtn, editProfileBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


//call

        buttonCall = findViewById(R.id.btnCall);

        buttonCall2 = findViewById(R.id.btnCall2);

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:012348569"));
                startActivity(intent);

            }
        });


        buttonCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:013698455"));
                startActivity(intent);

            }
        });




//header
        nameTv = findViewById(R.id.nameTv);
        logoutBtn = findViewById(R.id.logoutBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make offline
                //sign out
                //go to login activity
                makeMeOffline();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profile activity
                startActivity(new Intent(ContactActivity.this, Profile.class));
            }
        });

//close header

//contactActivity

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("contact");
        mDatabaseReference.child("data_contact").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarContact = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    ModelContact contactt = mDataSnapshot.getValue(ModelContact.class);
                    contactt.setKey(mDataSnapshot.getKey());
                    daftarContact.add(contactt);
                }

                mAdapter = new MainAdapter(ContactActivity.this, daftarContact);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ContactActivity.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        mFloatingActionButton = findViewById(R.id.add_contact);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                dialogTambahContact();
            }
        });



        //initialize and assign variable navBar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.contact);

        //perform itemselectedlisener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext()
                                , Map.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                , Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.contact:
                        return true;
                    case R.id.emergency:
                        startActivity(new Intent(getApplicationContext()
                                , EmergencyActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.sms:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void CallButton() {

        String number = mEditNumber.getText().toString();
        if (number.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + mEditNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }


    }

    private void CallButton2() {

        String number = mEditNumber.getText().toString();
        if (number.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + mEditNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallButton();
        }else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }







    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onDataClick(final ModelContact contactt, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose : ");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateContact(contactt);
            }
        });

        builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                hapusDataContact(contactt);
            }
        });

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogTambahContact() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contacts");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_contact, null);

        mEditName = view.findViewById(R.id.name_contact);
        mEditRelation = view.findViewById(R.id.relation_contact);
        mEditNumber = view.findViewById(R.id.number_contact);
        builder.setView(view);

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String namaContact = mEditName.getText().toString();
                String relationContact = mEditRelation.getText().toString();
                String numberrContact = mEditNumber.getText().toString();

                if (!namaContact.isEmpty() && !relationContact.isEmpty() && !numberrContact.isEmpty()) {
                    submitDataContact(new ModelContact(namaContact, relationContact, numberrContact));
                } else {
                    Toast.makeText(ContactActivity.this, "Fill in contact details", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateContact(final ModelContact contactt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Contacts");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_contact, null);

        mEditName = view.findViewById(R.id.name_contact);
        mEditRelation = view.findViewById(R.id.relation_contact);
        mEditNumber = view.findViewById(R.id.number_contact);

        mEditName.setText(contactt.getName());
        mEditRelation.setText(contactt.getRelation());
        mEditNumber.setText(contactt.getNumber());
        builder.setView(view);

        if (contactt != null) {
            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    contactt.setName(mEditName.getText().toString());
                    contactt.setRelation(mEditRelation.getText().toString());
                    contactt.setNumber(mEditNumber.getText().toString());
                    updateDataContact(contactt);
                }
            });
        }

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }

    private void submitDataContact(ModelContact contactt) {
        mDatabaseReference.child("data_contact").push()
                .setValue(contactt).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(ContactActivity.this, "Contact saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDataContact(ModelContact contactt) {
        mDatabaseReference.child("data_contact").child(contactt.getKey())
                .setValue(contactt).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(ContactActivity.this, "Contact updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hapusDataContact(ModelContact contactt) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("data_contact").child(contactt.getKey())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void mVoid) {
                    Toast.makeText(ContactActivity.this, "Contact deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }



    private void makeMeOffline() {
        //after logging in, make user online
        progressDialog.setMessage("Logging out");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update successfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss();
                        Toast.makeText(ContactActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkUser() {

            startActivity(new Intent(ContactActivity.this, LoginActivity.class));
            finish();

        }
    }







