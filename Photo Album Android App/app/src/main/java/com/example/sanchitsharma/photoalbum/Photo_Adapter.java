package com.example.sanchitsharma.photoalbum;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import photo.Photo;

/**
 * Created by ifndef on 4/30/2016.
 */
public class Photo_Adapter extends ArrayAdapter<Photo> {
    Context context;
    public Photo_Adapter(Context context, ArrayList<Photo> photo){
        super(context, R.layout.grid_item_layout, photo);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater albumInflater = LayoutInflater.from(getContext());
        View customView = albumInflater.inflate(R.layout.grid_item_layout, parent, false);

        String photo_name = getItem(position).name;
        String photo_path = getItem(position).path;

        TextView photo_text = (TextView) customView.findViewById(R.id.photo_text);
        ImageView photo_image = (ImageView) customView.findViewById(R.id.photo_image);
        photo_text.setText(photo_name);
        int draw = context.getResources().getIdentifier(photo_path, "drawable", context.getPackageName());
        photo_image.setImageResource(draw);

        return customView;
    }
}
