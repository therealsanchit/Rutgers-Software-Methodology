package com.example.sanchitsharma.photoalbum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import io.DataIO;
import photo.Album;
import photo.Photo;
import photo.Tag;
import usr.User;


public class HomeActivity extends AppCompatActivity{

    private User u;
    private int position;

    private Button deleteButton;
    private Button openButton;
    private Button createButton;
    private Button renameButton;
    private Button searchButton;

    final Context context = this;

    ListView album_list_view;
    Album_Adapter album_adapter;

    View.OnClickListener on_view_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* create and read the user's saved info
        *   this will make the albums and photos.
        */

        u = new User("user");
        position = -1;

        if(!u.setAlbums(context, new DataIO.IOUserReader()))
            Log.d("MSG", "FAILED LOADING DATA");

        Log.d("MSG", Integer.toString(u.albums.size()));
        deleteButton = (Button) findViewById(R.id.deleteButton);
        openButton = (Button) findViewById(R.id.openButton);
        createButton = (Button) findViewById(R.id.createButton);
        renameButton = (Button) findViewById(R.id.renameButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        album_adapter = new Album_Adapter(this, u.albums);
        album_list_view = (ListView) findViewById(R.id.albumListView);
        album_list_view.setAdapter(album_adapter);

        album_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album a = (Album)parent.getItemAtPosition(position);
                String value = a.name;
                Toast.makeText(HomeActivity.this, value, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = album_list_view.getCheckedItemPosition();
                if (position > -1) {
                    try {
                        Album a = u.albums.get(position);
                        if(u.removeAlbum(a))
                            Toast.makeText(HomeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) { Toast.makeText(HomeActivity.this, "Cannot Delete", Toast.LENGTH_SHORT).show();}
                } else {
                    Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData();
                refreshList();
            }
        });
        //open
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = album_list_view.getCheckedItemPosition();
                if(position > -1) {
                    try{
                        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                        intent.putExtra(context.getString(R.string.User_Info), u);
                        intent.putExtra(context.getString(R.string.Album_Index), position);
                        startActivity(intent);
                    } catch (Exception e) { Toast.makeText(HomeActivity.this, "Cannot Open", Toast.LENGTH_SHORT).show();}
                }else{
                    Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        //create
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtUrl = new EditText(context);

                txtUrl.setHint("Enter name here");

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Details")
                        .setMessage("Enter Name for album")
                        .setView(txtUrl)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String result = txtUrl.getText().toString();
                                if (result.isEmpty())
                                    return;

                                Album a = new Album(result);
                                if(!u.addAlbum(a)) {
                                    Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Toast.makeText(HomeActivity.this, "Created", Toast.LENGTH_SHORT).show();
                                saveData();
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(HomeActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        //rename
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = album_list_view.getCheckedItemPosition();
                if (position > -1) {
                    try {
                        final EditText txtUrl = new EditText(context);
                        txtUrl.setHint("Enter new name here");

                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("Details")
                                .setMessage("Enter Name for album")
                                .setView(txtUrl)
                                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String result = txtUrl.getText().toString();
                                        if (result.isEmpty()) {
                                            Toast.makeText(HomeActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else if (u.album_exists(result)){
                                            Toast.makeText(HomeActivity.this, "Already exists", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Album a = u.getAlbum(position);
                                        a.name = result;

                                        Toast.makeText(HomeActivity.this, "Renamed", Toast.LENGTH_SHORT).show();
                                        saveData();
                                        refreshList();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Toast.makeText(HomeActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Cannot Open", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        .setMessage("Search By Tag")
                        .setView(layout)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Tag.Type result_type = (Tag.Type)tag_type.getSelectedItem();
                                String result_value = tag_value.getText().toString();

                                Album search_results = new Album("search results");
                                search_results.photos = u.searchByTagPair(result_type, result_value);

                                position = -1;
                                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                                intent.putExtra(context.getString(R.string.User_Info), u);
                                intent.putExtra(context.getString(R.string.Album_Info), search_results);
                                intent.putExtra(context.getString(R.string.Album_Index), position);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(HomeActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveData(){
        if(DataIO.save(context, u))
            Toast.makeText(HomeActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(HomeActivity.this, "FAILED TO SAVE", Toast.LENGTH_LONG).show();
    }
    public void refreshList(){
        album_adapter.notifyDataSetChanged();
        album_list_view.setAdapter(album_adapter);
    }
}


