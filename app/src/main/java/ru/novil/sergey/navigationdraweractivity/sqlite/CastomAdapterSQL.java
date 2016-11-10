package ru.novil.sergey.navigationdraweractivity.sqlite;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.novil.sergey.navigationdraweractivity.R;

/**
 * Created by Sergey on 24.09.2016.
 */
public class CastomAdapterSQL extends ArrayAdapter {

    private final Activity context;
    private final String[] itemname;


    public CastomAdapterSQL(Activity context, String[] itemname) {
        super(context, R.layout.my_list, itemname);

        this.context = context;
        this.itemname = itemname;
    }

    @Override
    public View getView (int position,View view,ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.my_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);

        txtTitle.setText(itemname[position]);


        return rowView;
    }
}
