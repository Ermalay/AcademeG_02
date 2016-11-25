package ru.novil.sergey.navigationdraweractivity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.novil.sergey.navigationdraweractivity.other.MyApplication;
import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;
import ru.novil.sergey.navigationdraweractivity.sqlite.MyAsyncTask;
import ru.novil.sergey.navigationdraweractivity.view.SlidingTabLayout;
import ru.novil.sergey.navigationdraweractivity.view.SplashScreen;

public class FirstFragment extends Fragment {

    public String[] itemName, itemImage, itemDescription, itemPublished, itemChannelTitle;
    String title, publishedAt, channelTitle, description, url, videoId, videoIdPre, nextPageToken, prevPageToken, cursorVideoID, itemVideoID;
    String pageToken = "";

    boolean loadingMore = true;
    boolean bNextUrl = true;

    ParseTask parseTask;
    ParseTask2nd parseTask2nd;

//    MyApplication myApplication;

    AdapterListVideo adapterListVideo, adapterListVideo1;

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    Cursor cursor;
    ListView lv1, lv2, lv3;
    public TextView tv, tv1, tv2, tv22, tv23, tv3, tv33, tv333;
    ContentValues contentValues;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    Context context;

    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        tv = (TextView) rootView.findViewById(R.id.tv);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be
        // done AFTER the ViewPager has had it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);



//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
//                mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) getActivity());
//        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
    }


//    @Override
//    public void onRefresh() {
//        Toast.makeText(getActivity(), "SwipeRefreshLayout", Toast.LENGTH_SHORT).show();
//    }

    // Adapter
    class SamplePagerAdapter extends PagerAdapter implements SwipeRefreshLayout.OnRefreshListener {

        /**
         * Вернуть Количество страниц для отображения
         */
        @Override
        public int getCount() {
            return 5;
        }

        /**
         * Возвратите True, если значение, возвращенное из такой же объект, как вид
         * добавлены в ViewPager.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at position. This is important as what
         * this method returns is what is displayed in the SlidingTabLayout.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0){
                return "Лента";
            } else if (position == 1) {
                return "AcademeG";
            } else if (position == 2) {
                return "AcademeG 2hd";
            } else {
                return "Вкладка " + (position + 1);
            }

        }

        /**
         * Создать представление, которое будет отображаться в позиции. Здесь мы
         * надуть макет из ресурсов приложения, а затем измените текст
         * целью обозначить позицию.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                Toast.makeText(getActivity(), "Запущена вкладка №1", Toast.LENGTH_SHORT).show();
                // Надуть новый макет с наших ресурсов
                View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item_1,
                        container, false);
                lv1 = (ListView) view.findViewById(R.id.lv1);
                TextView tv7 = (TextView) view.findViewById(R.id.tv7);
                tv7.setText(pageToken);
//вывод списка lv1
                mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
                mSwipeRefreshLayout.setOnRefreshListener(this);
                mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);

                View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layot, null, false);
                lv1.addFooterView(footerView);

                fillListView("video_academeg", lv1);

//                lv1.setOnScrollListener(new AbsListView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(AbsListView absListView, int i) {
//
//                    }
//
//                    @Override
//                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                        if ((i == i2 - 7) && loadingMore){
//                            loadingMore = false;
//                            pageToken = myApplication.getsNextPageTokenAca();
////                            pageToken = nextPageToken;
//                            new ParseTask4All().execute();
//                        }
//                    }
//                });

//конец вывода списка lv1
                container.addView(view);
                return view;

                //Вторая вкладка
            } if (position == 1){
                View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item_2,
                        container, false);

                tv2 = (TextView) view.findViewById(R.id.tv2);
                tv22 = (TextView) view.findViewById(R.id.tv22);
                tv23 = (TextView) view.findViewById(R.id.tv23);
                lv2 = (ListView) view.findViewById(R.id.lv2);

                mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
                mSwipeRefreshLayout.setOnRefreshListener(this);
                mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);

                View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layot, null, false);
                lv2.addFooterView(footerView);

//вывод списка lv2

                fillListView("video_academeg WHERE channelTitle='AcademeG'", lv2);


//конец вывода списка lv2

//                lv2.setOnScrollListener(new AbsListView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(AbsListView absListView, int i) {
//
//                    }
//
//                    @Override
//                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                        if ((i == i2 - 5) && loadingMore){
//                            loadingMore = false;
//                            pageToken = nextPageToken;
//                            new ParseTask().execute();
//                        }
//                    }
//                });
//
//                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
////                        Intent intent = new Intent(getActivity(), YouTubeActivity.class);
//
//                        returnCursor();
//                        cursor.moveToPosition(position);
//                        String vId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.VIDEO_ID_COLUMN));
//                        cursor.close();
//
//                        Intent intent = new Intent(getActivity(), Delete_It.class);
//                        intent.putExtra("pushkin", vId);
//                        startActivity(intent);
//                        Toast.makeText(getActivity(), "onItemClick - " + position, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//                Button buttonPager2 = (Button) view.findViewById(R.id.buttonPager2);
//                buttonPager2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "Кнопочку нажали!!!", Toast.LENGTH_SHORT).show();
//                        pageToken = nextPageToken;
//                        new ParseTask().execute();
////                        SplashScreen splashScreen = new SplashScreen();
////                        splashScreen.
//                    }
//                });
//
//
//                new ParseTask().execute();  //читаем JSON и заполняем SQLite
//
//                tv2.setText(nextPageToken);
                container.addView(view);
                return view;


            } if (position == 2){
                View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item_3,
                        container, false);

                tv3 = (TextView) view.findViewById(R.id.tv3);
                tv33 = (TextView) view.findViewById(R.id.tv33);
                tv333 = (TextView) view.findViewById(R.id.tv333);
                lv3 = (ListView) view.findViewById(R.id.lv3);

                mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_3);
                mSwipeRefreshLayout.setOnRefreshListener(this);
                mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);

                View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layot, null, false);
                lv3.addFooterView(footerView);

//вывод списка lv3

                fillListView("video_academeg WHERE channelTitle='AcademeG 2nd CH'", lv3);


//конец вывода списка lv3

                lv3.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                        if ((i == i2 - 5) && loadingMore){
                            loadingMore = false;
//                            final MyApplication myApplication = (MyApplication) getActivity().getApplication();
//                            pageToken = myApplication.getPageTokenAca2nd();
//                            pageToken = nextPageToken;
                            new ParseTask2nd().execute();
                        }
                    }
                });

                lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        Intent intent = new Intent(getActivity(), YouTubeActivity.class);

                        returnCursor();
                        cursor.moveToPosition(position);
                        String vId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.VIDEO_ID_COLUMN));
                        cursor.close();

                        Intent intent = new Intent(getActivity(), Delete_It.class);
                        intent.putExtra("pushkin", vId);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "onItemClick - " + position, Toast.LENGTH_SHORT).show();
                    }
                });


//                Button buttonPager2 = (Button) view.findViewById(R.id.buttonPager2);
//                buttonPager2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "Кнопочку нажали!!!", Toast.LENGTH_SHORT).show();
//                        pageToken = nextPageToken;
//                        new ParseTask().execute();
////                        SplashScreen splashScreen = new SplashScreen();
////                        splashScreen.
//                    }
//                });


//                new ParseTask().execute();  //читаем JSON и заполняем SQLite
//                new ParseTask2nd().execute();

//                tv3.setText(nextPageToken);
                final MyApplication myApplication = (MyApplication) getActivity().getApplication();
                tv3.setText(myApplication.getPageTokenAca2nd());


                container.addView(view);
                return view;
            }
            //Другие вкладки справа
            else {
                // Надуть новый макет с наших ресурсов
                View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                        container, false);
                // Добавить созданный вид на ViewPager
                container.addView(view);

                // Retrieve a TextView from the inflated View, and update it's text
                TextView title = (TextView) view.findViewById(R.id.item_title);
                title.setText(String.valueOf(position + 1));

                // Return the View
                return view;
            }
        }

        /**
         * Destroy the item from the ViewPager. In our case this is simply
         * removing the View.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(), "!!! onRefresh() !!!", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(true);
            new ParseTask().execute();

        }
    }

    private class ParseTask4All extends AsyncTask<Void, Void, String> {

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
//                    String playlistIdKey = "UUM0RSbJnk0nAUvfH4Pp7mjQ";    //мой канал
//                String playlistIdKey = "UUQeaXcwLUDeRoNVThZXLkmw";      //Big Test Drive
                String playlistIdKey = "UUL1C1f9HWf3Hyct4aqBJi1A";      //AcademeG2nd

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


                mDatabaseHelper = new DatabaseHelper(getActivity());
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                contentValues = new ContentValues();

                returnCursor();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);//берём каждый пункт из массива items
                    JSONObject snippet = item.getJSONObject("snippet");//из пункта берём объект по ключу "snippet"
                    title = snippet.getString("title");//из объекта snippet берём строку по ключу title
                    description = snippet.getString("description");
                    publishedAt = snippet.getString("publishedAt");
                    channelTitle = snippet.getString("channelTitle");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    url = medium.getString("url");
                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                    videoId = resourceId.getString("videoId");

                    //Если такого videoId ещё нет в Базе или База пустая
                    if (compareSQLiteAndJSON(videoId)){
                        fillSQLite(DatabaseHelper.DATABASE_TABLE_ACAGEMEG);
                    }
                }

                cursor.close();
                mSqLiteDatabase.close();

                fillArrayItems(returnCursor());           //заполняем массивы для адаптера

                Parcelable state = lv1.onSaveInstanceState();

                fillAdapterListVideo();     //заполняем ListView адаптером
                lv1.setAdapter(adapterListVideo);

                lv1.onRestoreInstanceState(state);
                mSwipeRefreshLayout.setRefreshing(false);//указываем об окончании обновления страницы
                loadingMore = true;             //Вызываем onScroll только один раз
                Toast.makeText(getActivity(), "!!! new ParseTask().execute(); !!!", Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {e.printStackTrace();}
        }
    }//Конец ParseTask4All

    private class ParseTask extends AsyncTask<Void, Void, String> {

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
//                    String playlistIdKey = "UUM0RSbJnk0nAUvfH4Pp7mjQ";    //мой канал
                    String playlistIdKey = "UUQeaXcwLUDeRoNVThZXLkmw";      //Big Test Drive
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


                        mDatabaseHelper = new DatabaseHelper(getActivity());
                        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                        contentValues = new ContentValues();

                        returnCursor();

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);//берём каждый пункт из массива items
                            JSONObject snippet = item.getJSONObject("snippet");//из пункта берём объект по ключу "snippet"
                            title = snippet.getString("title");//из объекта snippet берём строку по ключу title
                            description = snippet.getString("description");
                            publishedAt = snippet.getString("publishedAt");
                            channelTitle = snippet.getString("channelTitle");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject medium = thumbnails.getJSONObject("medium");
                            url = medium.getString("url");
                            JSONObject resourceId = snippet.getJSONObject("resourceId");
                            videoId = resourceId.getString("videoId");

                            //Если такого videoId ещё нет в Базе или База пустая
                            if (compareSQLiteAndJSON(videoId)){
                                fillSQLite(DatabaseHelper.DATABASE_TABLE_ACAGEMEG);
                            }
                        }

                        cursor.close();
                        mSqLiteDatabase.close();

                        fillArrayItems(returnCursor());           //заполняем массивы для адаптера

                        Parcelable state = lv2.onSaveInstanceState();

//                        fillAdapterListVideo();     //заполняем ListView адаптером
                        lv2.setAdapter(adapterListVideo);


                        lv2.onRestoreInstanceState(state);
                        mSwipeRefreshLayout.setRefreshing(false);//указываем об окончании обновления страницы
                        loadingMore = true;             //Вызываем onScroll только один раз
                        Toast.makeText(getActivity(), "!!! new ParseTask().execute(); !!!", Toast.LENGTH_SHORT).show();
                    }catch (JSONException e) {e.printStackTrace();}
            }
        }//Конец ParseTask

    private class ParseTask2nd extends AsyncTask<Void, Void, String> {
//        myApplication.getPageToken()

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
//                    String playlistIdKey = "UUM0RSbJnk0nAUvfH4Pp7mjQ";    //мой канал
//                String playlistIdKey = "UUQeaXcwLUDeRoNVThZXLkmw";      //Big Test Drive
                String playlistIdKey = "UUL1C1f9HWf3Hyct4aqBJi1A";      //AcademeG2nd

                String lastPartURL = "&key=";
                String developerKey = "AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk";


                final MyApplication myApplication = (MyApplication) getActivity().getApplication();

                URL url = new URL(firstPartURL + maxResults + maxResultsKey + prePageToken + myApplication.getPageTokenAca2nd() //+ pageToken
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

                    final MyApplication myApplication = (MyApplication) getActivity().getApplication();
                    myApplication.setPageTokenAca2nd(dataJsonObj.getString("nextPageToken"));
//                    nextPageToken = dataJsonObj.getString("nextPageToken");
                } else if (dataJsonObj.has("prevPageToken")) {
                    prevPageToken = dataJsonObj.getString("prevPageToken");
                }

                JSONArray items = dataJsonObj.getJSONArray("items");


                mDatabaseHelper = new DatabaseHelper(getActivity());
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                contentValues = new ContentValues();

                returnCursor();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);//берём каждый пункт из массива items
                    JSONObject snippet = item.getJSONObject("snippet");//из пункта берём объект по ключу "snippet"
                    title = snippet.getString("title");//из объекта snippet берём строку по ключу title
                    description = snippet.getString("description");
                    publishedAt = snippet.getString("publishedAt");
                    channelTitle = snippet.getString("channelTitle");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    url = medium.getString("url");
                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                    videoId = resourceId.getString("videoId");

                    //Если такого videoId ещё нет в Базе или База пустая
                    if (compareSQLiteAndJSON(videoId)){
                        fillSQLite(DatabaseHelper.DATABASE_TABLE_ACAGEMEG);
                    }
                }

                cursor.close();
                mSqLiteDatabase.close();

                fillArrayItems(returnCursor());           //заполняем массивы для адаптера

                Parcelable state = lv3.onSaveInstanceState();

                fillAdapterListVideo();     //заполняем ListView адаптером
                lv3.setAdapter(adapterListVideo);

                lv3.onRestoreInstanceState(state);
                mSwipeRefreshLayout.setRefreshing(false);//указываем об окончании обновления страницы
                loadingMore = true;             //Вызываем onScroll только один раз
                Toast.makeText(getActivity(), "!!! new ParseTask().execute(); !!!", Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {e.printStackTrace();}
        }
    }//Конец ParseTask2nd

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
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.DATABASE_TABLE_ACAGEMEG;
        cursor = mSqLiteDatabase.rawQuery(query, null);
        return cursor;
    }

//    public Cursor returnCursor2nd (){
//        mDatabaseHelper = new DatabaseHelper(getActivity());
//        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
//        String query = "select * from " + DatabaseHelper.DATABASE_TABLE_ACAGEMEG_2ND_CH;
//        cursor = mSqLiteDatabase.rawQuery(query, null);
//        return cursor;
//    }

    public void fillSQLite (String sDataBaseTable){
        contentValues.put(DatabaseHelper.TITLE_COLUMN, title);
        contentValues.put(DatabaseHelper.URL_COLUMN, url);
        contentValues.put(DatabaseHelper.DESCRIPTION_COLUMN, description);
        contentValues.put(DatabaseHelper.VIDEO_ID_COLUMN, videoId);
        contentValues.put(DatabaseHelper.PUBLISHEDAT_COLUMN, publishedAt);
        contentValues.put(DatabaseHelper.CHANNEL_TITLE_COLUMN, channelTitle);
//        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE_ACAGEMEG, null, contentValues); //добавляем contentValues в SQLite
        mSqLiteDatabase.insert(sDataBaseTable, null, contentValues); //добавляем contentValues в SQLite
    }

    public void fillAdapterListVideo(){
//        AdapterListVideo adapterListVideo = new AdapterListVideo(getActivity(), itemName, itemImage, itemDescription);
        adapterListVideo = new AdapterListVideo(getActivity(), itemName, itemImage, itemDescription, itemPublished, itemChannelTitle);
//        lv2.setAdapter(adapterListVideo);
//        loadingMore = true;
    }

    //Заполняем массивы из БД для лист-адаптера
    public String[] fillArrayItems2nd (Cursor cursor, String columnName){
        String[] arr = new String[cursor.getCount()];
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                arr[i] = cursor.getString(cursor.getColumnIndex(columnName));
                cursor.moveToNext();
            }
        }
        return arr;
    }

    public void fillArrayItems (Cursor cursor){

//        tv23.setText("В cursor.getCount() - " + Integer.toString(cursor.getCount()) + " позиций.");
//        tv33.setText("В cursor.getCount() - " + Integer.toString(cursor.getCount()) + " позиций.");

        if (cursor.getCount() > 0){

            if (cursor.moveToFirst()){
                itemName = new String[cursor.getCount()];
                itemImage = new String[cursor.getCount()];
                itemDescription = new String[cursor.getCount()];
                itemPublished = new String[cursor.getCount()];
                itemChannelTitle = new String[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++){
                    itemName[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN));
                    itemImage[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.URL_COLUMN));
                    itemDescription[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                    itemPublished[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PUBLISHEDAT_COLUMN));
                    itemChannelTitle[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CHANNEL_TITLE_COLUMN));
                    cursor.moveToNext();
                }

            } else {
                tv2.setText("херня какая-то.");
            }

        } else {
            tv2.setText("в курсоре ничего нет - База пустая");
        }

        cursor.close();
        mSqLiteDatabase.close();
    }

    public void fillListView (String databaseTable, ListView someLv){
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String query =
                "SELECT * FROM "
                        + databaseTable
                ;
//                                + " WHERE "
//                                + DatabaseHelper.CHANNEL_TITLE_COLUMN
//                                + "='AcademeG 2nd CH'";

        cursor = mSqLiteDatabase.rawQuery(query, null);

        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                String[] itemName0 = new String[cursor.getCount()];
                String[] itemImage0 = new String[cursor.getCount()];
                String[] itemDescription0 = new String[cursor.getCount()];
                String[] itemPublished0 = new String[cursor.getCount()];
                String[] itemChannelTitle0 = new String[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++){
                    itemName0[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN));
                    itemImage0[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.URL_COLUMN));
                    itemDescription0[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                    itemPublished0[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PUBLISHEDAT_COLUMN));
                    itemChannelTitle0[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CHANNEL_TITLE_COLUMN));
                    cursor.moveToNext();
                }
                AdapterListVideo adapterListVideo1 = new AdapterListVideo(
                        getActivity(),
                        itemName0,
                        itemImage0,
                        itemDescription0,
                        itemPublished0,
                        itemChannelTitle0);
                someLv.setAdapter(adapterListVideo1);
            }





        } else {
            Toast.makeText(getActivity(), "Курсор пустой!!!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        mSqLiteDatabase.close();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startAsyncTaskInParallel(AsyncTask task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }
}
