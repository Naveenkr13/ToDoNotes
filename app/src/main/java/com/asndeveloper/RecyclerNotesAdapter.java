package com.asndeveloper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asndeveloper.todonotes.MainActivity;
import com.asndeveloper.todonotes.R;

import java.util.ArrayList;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {

    //step 1
    Context context;
    ArrayList<Notes> arrNotes=new ArrayList<>();

    //step of database reference
    DatabaseHelper databaseHelper;

    //Constructor Created

    public RecyclerNotesAdapter(Context context, ArrayList<Notes> arrNotes, DatabaseHelper databaseHelper){
        this.context=context;
        this.arrNotes=arrNotes;
        this.databaseHelper=databaseHelper;
    }


    //step 3 implement onCreateViewHolder,OnBindViewHolder,getItemCount

    @NonNull
    @Override
    public RecyclerNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.noterow,parent,false));

    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerNotesAdapter.ViewHolder holder, int position) {
        // title show
        holder.txtTitleV.setText(arrNotes.get(position).getTitle());
        //content show
        holder.txtContentV.setText(arrNotes.get(position).getContent());
        //delete image button click pe
        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteitem(position);
            }
        });

        // click on note or touch linear layout means card view to update data
        holder.TapOnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create update method and call from here
                updatenote(position);
            }
        });


    }
    //create method for update notes data
    private void updatenote(int position) {
        Notes noteUpdate=arrNotes.get(position);
        //dialog box refer here means put their xml reference here
        Dialog updateDialog=new Dialog(context);
        updateDialog.setContentView(R.layout.update);
        // id find in xml found
        EditText edTitle,edContent;
        Button updatebtn;
        edTitle=updateDialog.findViewById(R.id.title);
        edContent=updateDialog.findViewById(R.id.content);
        updatebtn=updateDialog.findViewById(R.id.updatesubmit);

        // existing value set to this dialog
        edTitle.setText(noteUpdate.getTitle());
        edContent.setText(noteUpdate.getContent());

        // ab jb update button pe click karega tb new data submit or updated hoke dikhaye ga
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get karenge jo updated data enter kiya hai user ne
                String updatedTitle=edTitle.getText().toString();
                String updatedContent=edContent.getText().toString();

                //jo screen pe dikhega after update hoke data to eske liye set karenge
                noteUpdate.setTitle(updatedTitle);
                noteUpdate.setContent(updatedContent);

                // database ke command ko call karenge aur noteupdate object ko put karenge as below
                databaseHelper.noteDao().updateNotes(noteUpdate);
                //refresh the recycler view
                ((MainActivity)context).showNotes();
                updateDialog.dismiss();
            }
        });

        updateDialog.show();

    }
    //method for delete notes
    public void deleteitem(int pos) {
        //create alert dialog box
        AlertDialog dialog=new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are You Sure Want to Delete this note ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which notes delete then update in data
                        //delete hogya
                        databaseHelper.noteDao().deleteNotes(new Notes(arrNotes.get(pos).getId(),
                                arrNotes.get(pos).getTitle(),arrNotes.get(pos).getContent()
                        ));
                        //ab show karenge delete note hoone ke baad by calling showNotes method from Mainactivity
                        ((MainActivity)context).showNotes();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }


    //step 2
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleV,txtContentV;
        ImageButton imgdelete;

        LinearLayout TapOnNote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // id find that is used to show data title and content
            txtTitleV=itemView.findViewById(R.id.titleshow);
            txtContentV=itemView.findViewById(R.id.noteshow);
            // click or touch on note linear layout
            TapOnNote=itemView.findViewById(R.id.linearRow);
            // click delete imagebutton id find here
            imgdelete=itemView.findViewById(R.id.deletebutton);
        }
    }
}

