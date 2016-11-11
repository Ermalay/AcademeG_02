package ru.novil.sergey.navigationdraweractivity.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.novil.sergey.navigationdraweractivity.MainActivity;
import ru.novil.sergey.navigationdraweractivity.R;
import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;
import ru.novil.sergey.navigationdraweractivity.sqlite.MyAsyncTask;

public class SplashScreen extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    Cursor cursor;

    String title, description, url, videoId, videoIdPre, nextPageToken, prevPageToken, cursorVideoID, itemVideoID;
    String pageToken = "";

    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
//        myAsyncTask.execute();

        new ParseTaskAka().execute();



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(intent);
//                new ParseTaskAka().execute();
//                finish();
//            }
//        }, 5*1000);

    }

    private class ParseTaskAka extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                String firstPartURL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet";
                String maxResults = "&maxResults=";
                String maxResultsKey = "6";
                String prePageToken = "&pageToken=";
                String playlistId = "&playlistId=";
//                String playlistIdKey = "UUM0RSbJnk0nAUvfH4Pp7mjQ";        //мой канал
//                String playlistIdKey = "UUQeaXcwLUDeRoNVThZXLkmw";      //Big Test Drive
//                String playlistIdKey = "UU0lT9K8Wfuc1KPqm6YjRf1A";      //AcademeG
                String playlistIdKey = "UUL1C1f9HWf3Hyct4aqBJi1A";      //AcademeG2ndCH

                    //AcademeG DailyStream
//                https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=3&playlistId=UUEVNTzTFSGkZGTjVE9ipXpg&key=AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk


                String lastPartURL = "&key=";
                String developerKey = "AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk";

                URL url = new URL(firstPartURL + maxResults + maxResultsKey + prePageToken + pageToken
                        + playlistId + playlistIdKey + lastPartURL + developerKey);
//                    URL url = new URL("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=1&pageToken=&playlistId=UUM0RSbJnk0nAUvfH4Pp7mjQ&key=AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk");

//                    printURL(url.toString());       //показывает строку url


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);

                if (dataJsonObj.has("nextPageToken")) {
                    nextPageToken = dataJsonObj.getString("nextPageToken");
                } else if (dataJsonObj.has("prevPageToken")) {
                    prevPageToken = dataJsonObj.getString("prevPageToken");
                }

                JSONArray items = dataJsonObj.getJSONArray("items");

                mDatabaseHelper = new DatabaseHelper(getBaseContext());
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                contentValues = new ContentValues();

                returnCursor();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);//берём каждый пункт из массива items
                    JSONObject snippet = item.getJSONObject("snippet");//из пункта берём объект по ключу "snippet"
                    title = snippet.getString("title");//из объекта snippet берём строку по ключу title
                    description = snippet.getString("description");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    url = medium.getString("url");
                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                    videoId = resourceId.getString("videoId");


                        if (compareSQLiteAndJSON(videoId)){     //Если такого videoId ещё нет в Базе
                            fillSQLite();                       //или База пустая
                        }
                }

                cursor.close();
                mSqLiteDatabase.close();

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();

            }catch (JSONException e) {e.printStackTrace();}
        }
    }//Конец ParseTask

    public void fillSQLite (){
        contentValues.put(DatabaseHelper.TITLE_COLUMN, title);
        contentValues.put(DatabaseHelper.URL_COLUMN, url);
        contentValues.put(DatabaseHelper.DESCRIPTION_COLUMN, description);
        contentValues.put(DatabaseHelper.VIDEO_ID_COLUMN, videoId);
        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE_ACAGEMEG_2ND_CH, null, contentValues); //добавляем contentValues в SQLite
    }

    public boolean compareSQLiteAndJSON (String videoId){
        returnCursor();
        if (cursor.getCount() > 0){                             // Если в Базе что-то есть
            for (int i = 0; i < cursor.getCount(); i++){        // Перебираем cursor
                cursor.moveToPosition(i);                       // cursor на позицию i
                String c = cursor.getString(cursor.getColumnIndex(DatabaseHelper.VIDEO_ID_COLUMN));
                if (c.equals(videoId)){                     // если cursor на позиции i равен строке videoId
                    return false;                           // возвращаем false
                }
            }  return true;                                     //если нет совпадений в Базе - возвращаем true
        } else {
            return true;                                        //Если в Базе ничего нет - возвращаем true
        }

    }

    public Cursor returnCursor (){
        mDatabaseHelper = new DatabaseHelper(getBaseContext());
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String query = "select * from " + DatabaseHelper.DATABASE_TABLE_ACAGEMEG_2ND_CH;
        cursor = mSqLiteDatabase.rawQuery(query, null);
        return cursor;
    }
}
