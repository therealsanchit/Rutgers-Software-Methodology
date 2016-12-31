package com.example.sanchitsharma.photoalbum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import io.DataIO;
import photo.Album;
import photo.Photo;
import photo.Tag;
import usr.User;

public class DisplayActivity extends AppCompatActivity {
    User u;
    int albumIndex;
    int photoIndex;
    Album currentAlbum;
    Photo currentPhoto;

    final Context context = this;

    ListView tag_list_view;
    Display_Adapter display_adapter;
    ArrayList<Tag> currentTags;

    private Button addtagButton;
    private Button removetagButton;
    private Button leftButton;
    private Button rightButton;
    private Button closeButton;

    ImageView image_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        addtagButton = (Button) findViewById(R.id.addTag);
        removetagButton = (Button) findViewById(R.id.removeTag);
        leftButton = (Button) findViewById(R.id.leftPhoto);
        rightButton = (Button) findViewById(R.id.rightPhoto);
        closeButton = (Button) findViewById(R.id.close);

        u = (User)getIntent().getSerializableExtra(this.getString(R.string.User_Info));
        albumIndex = getIntent().getIntExtra(this.getString(R.string.Album_Index), 0);
        photoIndex = getIntent().getIntExtra(this.getString(R.string.Photo_Index), 0);
        if(albumIndex >= 0)
            currentAlbum = u.getAlbum(albumIndex);
        else
            currentAlbum = (Album)getIntent().getSerializableExtra(this.getString(R.string.Album_Info));
        currentPhoto = currentAlbum.getPhoto(photoIndex);

        Toast.makeText(DisplayActivity.this, currentPhoto.name, Toast.LENGTH_SHORT).show();

        currentTags = new ArrayList<Tag>();
        currentTags.addAll(currentPhoto.tags);
        display_adapter = new Display_Adapter(this, currentTags);
        tag_list_view = (ListView) findViewById(R.id.tagView);
        tag_list_view.setAdapter(display_adapter);

        image_display = (ImageView)findViewById(R.id.image_display);
        int draw = context.getResources().getIdentifier(currentPhoto.path, "drawable", context.getPackageName());
        image_display.setImageResource(draw);

        addtagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final Spinner tag_type = new Spinner(context);
                tag_type.setAdapter(new ArrayAdapter<Tag.Type>(context, android.R.layout.simple_spinner_item, Tag.Type.values()));
                final EditText tag_value = new EditText(context);
                tag_value.setHint("Tag Value");

                layout.addView(tag_type);
                layout.addView(tag_value);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Details")
                        .setMessage("Enter Tag Information")
                        .setView(layout)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Tag.Type result_type = (Tag.Type)tag_type.getSelectedItem();
                                String result_value = tag_value.getText().toString();

                                if (result_value.isEmpty() || result_type == null)
                                    return;

                                Tag t = new Tag(result_type, result_value);
                                currentPhoto.addToTags(t);

                                Toast.makeText(DisplayActivity.this, "Added Tag", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(DisplayActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        removetagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final Spinner tag_type = new Spinner(context);
                tag_type.setAdapter(new ArrayAdapter<Tag.Type>(context, android.R.layout.simple_spinner_item, Tag.Type.values()));
                final EditText tag_value = new EditText(context);
                tag_value.setHint("Tag Value");

                layout.addView(tag_type);
                layout.addView(tag_value);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Details")
                        .setMessage("Delete Tag")
                        .setView(layout)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Tag.Type result_type = (Tag.Type)tag_type.getSelectedItem();
                                String result_value = tag_value.getText().toString();

                                if (result_value.isEmpty() || result_type == null)
                                    return;

                                if(!currentPhoto.deleteFromTags(result_type, result_value)){
                                    Toast.makeText(DisplayActivity.this, "Tag not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Toast.makeText(DisplayActivity.this, "Deleted Tag", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(DisplayActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                photoIndex = --photoIndex<0? currentAlbum.photos.size()-1:photoIndex;
                refreshPhoto(photoIndex);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                photoIndex = ++photoIndex>=currentAlbum.photos.size()? 0:photoIndex;
                refreshPhoto(photoIndex);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try{
                        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                        intent.putExtra(context.getString(R.string.User_Info), u);
                        intent.putExtra(context.getString(R.string.Album_Index), albumIndex);
                        intent.putExtra(context.getString(R.string.Album_Info), currentAlbum);
                        startActivity(intent);
                } catch (Exception e) { Toast.makeText(DisplayActivity.this, "Cannot Open", Toast.LENGTH_SHORT).show();}
            }
        });

    }

    public void saveData(){
        if(DataIO.save(context, u))
            Toast.makeText(DisplayActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(DisplayActivity.this, "FAILED TO SAVE", Toast.LENGTH_LONG).show();
    }
    public void refreshList(){
        currentTags.clear();
        currentTags.addAll(currentPhoto.tags);
        display_adapter.notifyDataSetChanged();
        tag_list_view.setAdapter(display_adapter);
    }

    public void refreshPhoto(int position){
        currentPhoto = currentAlbum.getPhoto(position);
        image_display = (ImageView)findViewById(R.id.image_display);
        int draw = context.getResources().getIdentifier(currentPhoto.path, "drawable", context.getPackageName());
        image_display.setImageResource(draw);

        currentTags.clear();
        currentTags.addAll(currentPhoto.tags);
        display_adapter.notifyDataSetChanged();
    }
}
