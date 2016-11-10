package ru.novil.sergey.navigationdraweractivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;

public class SecondFragment  extends Fragment {

    ListView lv_second;
    String itemname;
    String itemimage;
    TextView textView;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_second, container, false);
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        lv_second = (ListView) view.findViewById(R.id.lv_second);
        textView = (TextView) view.findViewById(R.id.textView4);
        Button button1 = (Button) view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
            }
        });
//        fillTextView4();

        return view;
    }

    public void onMyButton1Click(View view)
    {
        // выводим сообщение
        Toast.makeText(getActivity(), "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
    }

//
//    public void fillTextView4(){
//        cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_VIDEO, new String[]
//                {DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_VIDEO_ID}, null, null, null, null, null);
//        cursor.moveToFirst();
//        itemname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
//        itemimage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_VIDEO_ID));
//        textView.setText(itemimage + "  " + itemname);
//        cursor.close();
//    }

}