package com.asndeveloper.todonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asndeveloper.DatabaseHelper;
import com.asndeveloper.Notes;
import com.asndeveloper.RecyclerNotesAdapter;
import com.asndeveloper.profileActivity.ProfileView;
import com.asndeveloper.todonotes.LogIn.login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  Toolbar toolbars;
FirebaseAuth mFirebase;
//
Button btncreateN;
    FloatingActionButton fabAdd;
    RecyclerView recyclerView;

    DatabaseHelper databaseHelper;
    LinearLayout llnotes;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbars=findViewById(R.id.toolbar);
        //set back pressed button on toolbar
      setSupportActionBar(toolbars);
//      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //
//// database ka suru hai
        //id find method call
        initvar();
        //step6.2
        //call shownotes method first time app open then
        showNotes();



        //step 3
        //set on click button
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog box appear on click it
                Dialog dialog=new Dialog(MainActivity.this);
                //dialog layout pass here
                dialog.setContentView(R.layout.addnote);

                //id find of add note xml
                EditText edTitle,edContent;
                Button Savebtn;
                edTitle=dialog.findViewById(R.id.title);
                edContent=dialog.findViewById(R.id.content);
                Savebtn=dialog.findViewById(R.id.buttonsubmit);

                //step3.1
                // make  clickable button that savebtn work
                Savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //data to le rahe hai
                        String title=edTitle.getText().toString();
                        String content=edContent.getText().toString();
                        //check content field is empty or note
                        if (!content.equals("")){
//                            step 3.5
                            //agar data hai to room databse me storage hogi
                            databaseHelper.noteDao().addNotes(new Notes(title,content));
                            //show method for showing data
                            //step 6
                            showNotes();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, "Write your Note ,please", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

            }
        });
        //step 4
        //same create button pe kamm
        btncreateN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ye wahi kam karega jo fabadd button karega same same
                fabAdd.performClick();
            }
        });


    }

//database ka method hai

    public void showNotes() {
        //step 6
        ArrayList<Notes> arrnotes=(ArrayList<Notes>) databaseHelper.noteDao().getNotes();
        //step 6.1
        //setup visibility of task
        if (arrnotes.size()>0){
            llnotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //setup recycler view
            recyclerView.setAdapter(new RecyclerNotesAdapter(this,arrnotes,databaseHelper));

        }else {
            llnotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

    }


    //step 2
    public void initvar() {
        btncreateN=findViewById(R.id.btncreatenote);
        fabAdd=findViewById(R.id.floatcreatebtn);
        recyclerView=findViewById(R.id.recyclernotes);
        llnotes=findViewById(R.id.lnotes);
        //layoutmanger set to recycler
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        step 5
        //databse helper initilize here
        databaseHelper=DatabaseHelper.getInstance(this);

    }

//    public void setSupportActionBar(Toolbar supportActionBar) {
//        this.supportActionBar = supportActionBar;
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemid=item.getItemId();
        if (itemid==R.id.profile){
             Intent profile=new Intent(MainActivity.this, ProfileView.class);
             startActivity(profile);

        } else if (itemid==R.id.logout) {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            // Redirect to the login activity after logout
            Intent logout = new Intent(MainActivity.this, login.class);
            startActivity(logout);
            finish();

//            FirebaseAuth.getInstance().signOut();
//            Intent logout=new Intent(MainActivity.this, login.class);
//            startActivity(logout);
//            finish();

        }else{
            Toast.makeText(this, "Your Note is Here", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }
}