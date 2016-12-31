package com.example.sanchitsharma.photoalbum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import photo.Album;

/**
 * Created by kidoc_000 on 4/28/2016.
 */
public class Album_Adapter extends ArrayAdapter<Album> {

    public Album_Adapter(Context context,  ArrayList<Album> album) {
        super(context, R.layout.album_list, album);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater albumInflater = LayoutInflater.from(getContext());
        View customView = albumInflater.inflate(R.layout.album_list, parent, false);

        String album_name = getItem(position).name;
        TextView album_text = (TextView) customView.findViewById(R.id.album_text);

        album_text.setText(album_name);

        return customView;
    }
}
