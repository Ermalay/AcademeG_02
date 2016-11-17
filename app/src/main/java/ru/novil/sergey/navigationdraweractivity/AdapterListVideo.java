package ru.novil.sergey.navigationdraweractivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AdapterListVideo extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemImage;
    private final String[] itemName;
    private final String[] itemDescription;
    private final String[] itemPublished;


    public AdapterListVideo(Activity context, String[] itemName, String[] itemImage, String[] itemDescription, String[] itemPublished){
        super(context, R.layout.my_list, itemName);

        this.context = context;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemDescription = itemDescription;
        this.itemPublished = itemPublished;
    }


    @Override
    public View getView (int position,View view,ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_list, parent, false);
        TextView item = (TextView) rowView.findViewById(R.id.item);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        TextView publish = (TextView) rowView.findViewById(R.id.tvPublished);

        item.setText(itemName[position]);
        desc.setText(itemDescription[position]);
        publish.setText(itemPublished[position]);

        Picasso
                .with(context)
                .load(itemImage[position])
                .into(icon);

        return rowView;
    }
}
