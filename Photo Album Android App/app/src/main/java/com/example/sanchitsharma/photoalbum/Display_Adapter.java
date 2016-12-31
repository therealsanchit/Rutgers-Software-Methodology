package com.example.sanchitsharma.photoalbum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import photo.Tag;

/**
 * Created by ifndef on 4/30/2016.
 */
public class Display_Adapter extends ArrayAdapter<Tag> {

    public Display_Adapter(Context context, ArrayList<Tag> tags){
        super(context,R.layout.tag_list, tags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater albumInflater = LayoutInflater.from(getContext());
        View customView = albumInflater.inflate(R.layout.tag_list, parent, false);

        Tag.Type type = getItem(position).type;
        String typeS = type.name();
        String value = getItem(position).value;

        TextView type_text_view = (TextView) customView.findViewById(R.id.tag_type_view);
        TextView value_text_view = (TextView) customView.findViewById(R.id.tag_type_value);

        type_text_view.setText(typeS);
        value_text_view.setText(value);

        return customView;
    }
}
