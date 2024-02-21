package com.asndeveloper.todonotes.LogIn;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.asndeveloper.todonotes.MainActivity;
import com.asndeveloper.todonotes.R;
import com.asndeveloper.todonotes.signup.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    TextInputLayout LoginEmail,LoginPassword;
    AppCompatButton LoginBtn,NewAccount,ForgotPass;
FirebaseAuth mAuth;
 public static String Shared_Pref="sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //full screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        LoginEmail=findViewById(R.id.lgemail);
        LoginPassword=findViewById(R.id.loginpass);
        LoginBtn=findViewById(R.id.loginbtn);
        NewAccount=findViewById(R.id.createbtn);
        ForgotPass=findViewById(R.id.forgetpass);

        mAuth=FirebaseAuth.getInstance();


        //click on create new account button intent pass
        NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this, Signup.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickonLogin();
            }
        });

    }



    // click on login button method
    public void ClickonLogin() {
        String Email=LoginEmail.getEditText().getText().toString();
        String Password=LoginPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(Email)||TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Enter Your Login Details", Toast.LENGTH_SHORT).show();
        }else {
            //sigin ka method
            mAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Save user state in SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_email", Email);  // userEmail is the user's email
                                editor.putBoolean("is_logged_in", true);    // Set a flag indicating that the user is logged in
                                editor.apply();

                                Log.d(TAG, "signinsuccess");
                                Intent sentni = new Intent(login.this, MainActivity.class);
                                sentni.putExtra("user_name", mAuth.getCurrentUser().getDisplayName());
                                sentni.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                startActivity(sentni);
                                finish();
                            } else {
                      LoginEmail.getEditText().setText("");
                      LoginPassword.getEditText().setText("");
                                Toast.makeText(login.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }
    }
    @Override
    protected void onStart() {
//
     super.onStart();
    // Here check if the user is already logged in
    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
    boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);
    if (isLoggedIn) {
        // User is already logged in, navigate to MainActivity
        Intent intent = new Intent(login.this, MainActivity.class);
        Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();


        }
        else {
            Toast.makeText(this, "please login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}