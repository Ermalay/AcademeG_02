package ru.novil.sergey.navigationdraweractivity;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;

import static android.R.attr.format;

public class YTActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    TextView tvYTTitle, tvYTDescription, tvYTDate;
    ImageView ivYTArrow;
    LinearLayout llTitle;

    boolean bOnPress = true;

    private YouTubePlayerView playerView;
    String DEVELOPER_KEY = "AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk";

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    Cursor cursor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yt);

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.player_view_yt);
        youTubeView.initialize(DEVELOPER_KEY, this);

        tvYTTitle = (TextView) findViewById(R.id.tvYTTitle);
        ivYTArrow = (ImageView) findViewById(R.id.ivYTArrow);
        tvYTDate = (TextView) findViewById(R.id.tvYTDate);
        tvYTDescription = (TextView) findViewById(R.id.tvDescription);
        tvYTDescription.setVisibility(View.GONE);
        tvYTDescription.setMovementMethod(new ScrollingMovementMethod());
        llTitle = (LinearLayout) findViewById(R.id.llTitle);

        String dateStr = "15.09.2016";



//        tvYTDate.setText(publishedFormat(dateStr));



        returnCursorVId(getIntent().getStringExtra("pushkin"));
        if (cursor.moveToFirst()){
            tvYTTitle.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN)));
            tvYTDescription.setText("Описание: \n\n" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION_COLUMN)));
            tvYTDate.setText(publishedFormat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PUBLISHEDAT_COLUMN))));
//            tvYTDate.setText(publishedFormat("2016-11-24T19:22:14.000Z"));
        } else {
            tvYTDescription.setText("курсор пустой");
        }

        llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bOnPress){
                    tvYTDescription.setVisibility(View.VISIBLE);
                    ivYTArrow.setImageResource(R.drawable.ic_menu_send);
                    bOnPress = false;
                } else {
                    tvYTDescription.setVisibility(View.GONE);
                    ivYTArrow.setImageResource(R.drawable.ic_menu_camera);
                    bOnPress = true;
                }

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String publishedFormat(String oldDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
//        simpleDateFormat.applyPattern();
        Date date = null;
        try {
            date = simpleDateFormat.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String shortTime = "Опубликовано: " + sdf.format(date) + "г.";
        return shortTime;
    }

    public Cursor returnCursorVId(String sVId) {
        mDatabaseHelper = new DatabaseHelper(getBaseContext());
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM "
                + DatabaseHelper.DATABASE_TABLE_ACAGEMEG
                + " WHERE "
                + DatabaseHelper.VIDEO_ID_COLUMN
                + "='"
                + sVId
                + "'";

        cursor = mSqLiteDatabase.rawQuery(query, null);
        return cursor;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(getIntent().getStringExtra("pushkin"));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("YT Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
