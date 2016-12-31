package com.example.sanchitsharma.photoalbum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import io.DataIO;
import photo.Album;
import photo.Photo;
import photo.Tag;
import usr.User;

public class GalleryActivity extends AppCompatActivity {
    User u;
    Album currentAlbum;

    int albumIndex;
    int position;

    final Context context = this;

    GridView photo_grid_view;
    Photo_Adapter photo_adapter;

    private Button displayButton;
    private Button addButton;
    private Button editButton;
    private Button removeButton;
    private Button moveButton;
    private Button tagButton;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        displayButton = (Button) findViewById(R.id.displayButton);
        addButton = (Button) findViewById(R.id.addButton);
        editButton = (Button) findViewById(R.id.editButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        moveButton = (Button) findViewById(R.id.moveButton);
        tagButton = (Button) findViewById(R.id.tagButton);
        closeButton = (Button) findViewById(R.id.closeButton);

        u = (User)getIntent().getSerializableExtra(this.getString(R.string.User_Info));
        albumIndex = getIntent().getIntExtra(this.getString(R.string.Album_Index), 0);
        if(albumIndex >= 0)
            currentAlbum = u.albums.get(albumIndex);
        else
            currentAlbum = (Album)getIntent().getSerializableExtra(context.getString(R.string.Album_Info));
        position = -1;
        //test
        Toast.makeText(GalleryActivity.this, currentAlbum.name, Toast.LENGTH_SHORT).show();

        photo_adapter = new Photo_Adapter(this, currentAlbum.photos);
        photo_grid_view = (GridView) findViewById(R.id.gridview);
        photo_grid_view.setAdapter(photo_adapter);

        photo_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int pos, long id) {
                position = pos;
                Toast.makeText(GalleryActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //display
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(position <= -1) {
                    position = 0;
                }
                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra(context.getString(R.string.User_Info), u);
                intent.putExtra(context.getString(R.string.Album_Index), albumIndex);
                intent.putExtra(context.getString(R.string.Album_Info), currentAlbum);
                intent.putExtra(context.getString(R.string.Photo_Index), position);
                startActivity(intent);
            }
        });

        //add
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText photo_name = new EditText(context);
                final EditText photo_path = new EditText(context);
                photo_name.setHint("Enter name here");
                photo_path.setHint("Enter path here");

                layout.addView(photo_name);
                layout.addView(photo_path);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Details")
                        .setMessage("Enter Photo Information")
                        .setView(layout)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String result_name = photo_name.getText().toString();
                                String result_path = photo_path.getText().toString();
                                if (result_name.isEmpty() || result_path.isEmpty())
                                    return;


                                Photo p = new Photo(result_name, result_path);
                                currentAlbum.addToPhotos(p);

                                Toast.makeText(GalleryActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(GalleryActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        //edit
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(position <= -1) {
                    Toast.makeText(GalleryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText photo_name = new EditText(context);
                photo_name.setHint("Enter new name here");

                layout.addView(photo_name);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Details")
                        .setMessage("Enter Photo Information")
                        .setView(layout)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String result_name = photo_name.getText().toString();
                                if (result_name.isEmpty())
                                    return;

                                Photo p = currentAlbum.getPhoto(position);
                                if(p == null)
                                    return;

                                p.name = result_name;

                                Toast.makeText(GalleryActivity.this, "Edited", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(GalleryActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        //remove
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //position = photo_grid_view.getCheckedItemPosition();
                if (position > -1) {
                    try {
                        Photo p = currentAlbum.getPhoto(position);
                        if(p != null && currentAlbum.deleteFromPhotos(p))
                            Toast.makeText(GalleryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) { Toast.makeText(GalleryActivity.this, "Cannot Delete", Toast.LENGTH_SHORT).show();return;}
                } else {
                    Toast.makeText(GalleryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                position = -1;
                saveData();
                refreshList();
            }
        });

        //move
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(position <= -1) {
                    Toast.makeText(GalleryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText album_name = new EditText(context);
                album_name.setHint("Album Name");

                layout.addView(album_name);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Details")
                        .setMessage("Enter Name of Destination Album")
                        .setView(layout)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Photo p = currentAlbum.getPhoto(position);
                                if (p == null)
                                    return;

                                String result_name = album_name.getText().toString();

                                if (result_name.isEmpty())
                                    return;

                                Album moveTo = u.getAlbum(result_name);
                                if (moveTo == null) {
                                    Toast.makeText(GalleryActivity.this, "Album not found", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (moveTo.findPhoto(p.path)){
                                    Toast.makeText(GalleryActivity.this, "Photo already exists", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                moveTo.addToPhotos(p);
                                currentAlbum.deleteFromPhotos(p);

                                Toast.makeText(GalleryActivity.this, "Moved", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(GalleryActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        // REALLY FOR THE DISPLAY ACTIVITY
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position <= -1) {
                    Toast.makeText(GalleryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                                Photo p = currentAlbum.getPhoto(position);
                                if (p == null)
                                    return;

                                Tag.Type result_type = (Tag.Type)tag_type.getSelectedItem();
                                String result_value = tag_value.getText().toString();

                                if (result_value.isEmpty() || result_type == null)
                                    return;

                                Tag t = new Tag(result_type, result_value);
                                p.addToTags(t);

                                Toast.makeText(GalleryActivity.this, "Added Tag", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(GalleryActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        //close
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPhotoPathExists(ArrayList<Photo> pList, String path){
        for(Photo p: pList){
            if(p.path.equals(path)){
                return true;
            }
        }
        return false;
    }

    private boolean checkPhotoNameExists(ArrayList<Photo> pList, String name){
        for(Photo p: pList){
            if(p.name.equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public void saveData(){
        if(DataIO.save(context, u))
            Toast.makeText(GalleryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(GalleryActivity.this, "FAILED TO SAVE", Toast.LENGTH_LONG).show();
    }
    public void refreshList(){
        photo_adapter.notifyDataSetChanged();
        photo_grid_view.setAdapter(photo_adapter);
    }
}
