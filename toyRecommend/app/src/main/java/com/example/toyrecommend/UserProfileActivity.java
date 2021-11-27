package com.example.toyrecommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    private EditText parentName, childName, childAge, homeAddress;
    private Spinner gender;
    private Button saveDetails;
    private String name,phone,pwd;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");

        parentName = (EditText) findViewById(R.id.parent_name);
        childName = (EditText) findViewById(R.id.child_name);
        gender = (Spinner) findViewById(R.id.child_gender);
        childAge = (EditText) findViewById(R.id.child_age);
        homeAddress = (EditText) findViewById(R.id.user_address);
        saveDetails = (Button) findViewById(R.id.confirm_user_details);

        loadingBar = new ProgressDialog(this);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

    }

    private void saveUserProfile() {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child(" Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", pwd);
                    userdataMap.put("name", name);
                    userdataMap.put("parentName", parentName.getText().toString());
                    userdataMap.put("childName", childName.getText().toString());
                    userdataMap.put("gender", gender.getSelectedItem().toString());
                    userdataMap.put("childAge", childAge.getText().toString());
                    userdataMap.put("address", homeAddress.getText().toString());
                    userdataMap.put("oCount", 0);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(UserProfileActivity.this, "Your account has been created!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(UserProfileActivity.this, "Network Error. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(UserProfileActivity.this, "This "+ phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}