package com.asndeveloper.todonotes.signup;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.asndeveloper.todonotes.LogIn.login;
import com.asndeveloper.todonotes.R;
import com.asndeveloper.todonotes.dataholder.dataholder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import androidx.core.content.PermissionChecker;

import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;
import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
//    CircleImageView ProfilePic;

    TextInputLayout NameRegi,EmailRegi,PasswordRegi;
    AppCompatButton SignUpBtn,AlreadyAccount;
//    Button UploadBtn;
    FirebaseAuth mAuth;
    Uri filepath;
    Bitmap bitmap;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //full screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



//        ProfilePic=findViewById(R.id.profilepic);
//        UploadBtn=findViewById(R.id.uploadbtn);
        NameRegi=findViewById(R.id.name);
        EmailRegi=findViewById(R.id.email);
        PasswordRegi=findViewById(R.id.pswd);
        SignUpBtn=findViewById(R.id.singupbtn);
        AlreadyAccount=findViewById(R.id.signinbtn);




           //step 1
        //intent for go to log in page
        AlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents=new Intent(Signup.this, login.class);
                startActivity(intents);
                   finish();

            }
        });
        //step 3
        //click on signup button
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentication();
            }
        });
    }
    // method create

    //step 5
    //click on signup button then


    public   void authentication(){
        String names=NameRegi.getEditText().getText().toString().trim();
        String EmailMe=EmailRegi.getEditText().getText().toString().trim();
        String PasswordMe=PasswordRegi.getEditText().getText().toString().trim();



        //check field is empty
        if (TextUtils.isEmpty(EmailMe)||TextUtils.isEmpty(PasswordMe)){
            Toast.makeText(this, "Please Enter Your Details", Toast.LENGTH_SHORT).show();
        }else{
            //avobe method call here N
            mAuth=FirebaseAuth.getInstance();
//

            //conncection
            mAuth.createUserWithEmailAndPassword(EmailMe,PasswordMe)
                    .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                           //     datastorage();
                                firebaseUser=mAuth.getCurrentUser();
                                dataholder obj=new dataholder(names,EmailMe,PasswordMe);
                                FirebaseDatabase db=FirebaseDatabase.getInstance();
                                DatabaseReference root=db.getReference("users").child (firebaseUser.getUid());
                                root.setValue(obj);


                                NameRegi.getEditText().setText("");
                                EmailRegi.getEditText().setText("");
                                PasswordRegi.getEditText().setText("");
                                Toast.makeText(Signup.this, "Account Created", Toast.LENGTH_SHORT).show();



                            }else{
                                NameRegi.getEditText().setText("");
                                EmailRegi.getEditText().setText("");
                                PasswordRegi.getEditText().setText("");
                                Toast.makeText(Signup.this, "Provide Wrong Details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}