package com.asndeveloper.profileActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asndeveloper.todonotes.R;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileView extends AppCompatActivity {
    AppCompatButton savebtn;
    CircleImageView ProfilePic;
    TextView txtname,txtemail;
    Toolbar toolbarHai;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private StorageReference storageReference;
    private Uri filePath; // Variable to store the selected image URI


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        savebtn=findViewById(R.id.uploadbtn);
        txtname=findViewById(R.id.nameshows);
        txtemail=findViewById(R.id.emailshow);
        ProfilePic=findViewById(R.id.profilepic);
        toolbarHai=findViewById(R.id.toolbars);
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //back button
        setSupportActionBar(toolbarHai);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //reference
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //method
        loadProfile();
        LoadUserDetails();

        //click listener
        ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChoose();
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });

    }

    // Method

    //step 4  // Method to load the profile picture from Firebase Storage

    private void loadProfile() {
        StorageReference profileImage=storageReference.child("users/" + mAuth.getCurrentUser().getUid()+".jpg");
        profileImage.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri)
                    .into(ProfilePic);
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Toast.makeText(this, "Currently Not Show Picture", Toast.LENGTH_SHORT).show();
        });
    }


    ///step 3 now show user name and email id from realtime storage
    private void LoadUserDetails() {
        user=mAuth.getCurrentUser();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference root=db.getReference("users").child (user.getUid());


  root.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                // Retrieve the user details from the database
                String userName = snapshot.child("nameRegi").getValue(String.class);

                // Update the TextView with the user's name
                txtname.setText(userName);

                String userEmail = snapshot.child("emailRegi").getValue(String.class);
                txtemail.setText(userEmail);
            } else {
                Toast.makeText(ProfileView.this, "No Data Available Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("ProfileView", "Error loading user details: " + error.getMessage());
        }
    });}





    //step 2 when click on circular image
    private void ImageChoose() {
        ImagePicker.Companion.with(this)
                .crop()
                .start();
    }






    //step 2.1 when image picker open then receive their repsonse using onActivity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK && requestCode==ImagePicker.REQUEST_CODE){
            filePath=data.getData();
            //update profile picture using glide
            Glide.with(this)
                    .load(filePath)
                    .into(ProfilePic);
        }

    }


    //step 1 when click this button image get store in storage
    // make connection with current user with their image using uid and store photo in Storage,image store using link refrence
    private void UploadImage() {
        if (filePath!=null){
            StorageReference profileImage=storageReference.child("users/"+mAuth.getCurrentUser().getUid()+".jpg");
            UploadTask uploadTask=profileImage.putFile(filePath);
            uploadTask.addOnCompleteListener(this,task -> {
                if (task.isSuccessful()){
                    Toast.makeText(this, "Save Successfuly", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


}