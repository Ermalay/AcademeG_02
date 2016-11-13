package ru.novil.sergey.navigationdraweractivity.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.novil.sergey.navigationdraweractivity.FirstFragment;
import ru.novil.sergey.navigationdraweractivity.MainActivity;
import ru.novil.sergey.navigationdraweractivity.view.SplashScreen;

public class MyAsyncTask extends AsyncTask <Void, Void, String> {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String title, description, url, videoId, videoIdPre, nextPageToken, prevPageToken, cursorVideoID, itemVideoID;
    String resultJson = "";
    String pageToken = "";

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    ContentValues contentValues;
    Context context;
    Cursor cursor;

    SplashScreen splashScreen;
    MainActivity mainActivity;
    FirstFragment firstFragment;

    public MyAsyncTask(FirstFragment firstFragment) {
        this.firstFragment = firstFragment;
    }

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        // получаем данные с внешнего ресурса
        try {
            String firstPartURL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet";
            String maxResults = "&maxResults=";
            String maxResultsKey = "6";
            String prePageToken = "&pageToken=";
            String playlistId = "&playlistId=";
//                    String playlistIdKey = "UUM0RSbJnk0nAUvfH4Pp7mjQ";    //мой канал
//            String playlistIdKey = "UUQeaXcwLUDeRoNVThZXLkmw";      //Big Test Drive
            String playlistIdKey = "UU0lT9K8Wfuc1KPqm6YjRf1A";      //Akademeg
            String lastPartURL = "&key=";
            String developerKey = "AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk";

            URL url = new URL(firstPartURL + maxResults + maxResultsKey + prePageToken + pageToken
                    + playlistId + playlistIdKey + lastPartURL + developerKey);
                    //Большой тест-драйв
//                    URL url = new URL("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=1&pageToken=&playlistId=UUM0RSbJnk0nAUvfH4Pp7mjQ&key=AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk");
                    //AcademeG
//                    URL url = new URL("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&pageToken=&playlistId=UU0lT9K8Wfuc1KPqm6YjRf1A&key=AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk");

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


            mDatabaseHelper = new DatabaseHelper(context);
            mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
            contentValues = new ContentValues();

//            returnCursor();

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

//                contentValues.put(DatabaseHelper.TITLE_COLUMN, title);
//                mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, contentValues);
            }
//            mSqLiteDatabase.close();


        }catch (JSONException e) {e.printStackTrace();}
    }

    public void fillSQLite (){
        contentValues.put(DatabaseHelper.TITLE_COLUMN, title);
        contentValues.put(DatabaseHelper.URL_COLUMN, url);
        contentValues.put(DatabaseHelper.DESCRIPTION_COLUMN, description);
        contentValues.put(DatabaseHelper.VIDEO_ID_COLUMN, videoId);
        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE_ACAGEMEG, null, contentValues); //добавляем contentValues в SQLite
    }

    public boolean compareSQLiteAndJSON (String videoId){
//        returnCursor();
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
        mDatabaseHelper = new DatabaseHelper(context);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String query = "select * from " + DatabaseHelper.DATABASE_TABLE_ACAGEMEG;
        cursor = mSqLiteDatabase.rawQuery(query, null);
        return cursor;
    }
}
