package ru.novil.sergey.navigationdraweractivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;

public class YTActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    TextView tvYTActivityTitle, tvDescription;
    int count;
    String string;

    private YouTubePlayerView playerView;
    String KEY = "AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk";

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_yt);

        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // устанавливаем linLayout как корневой элемент экрана
        setContentView(linLayout, linLayoutParam);

        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        playerView = new YouTubePlayerView(this);
        playerView.initialize(KEY, this);
        linLayout.addView(playerView);


        TextView tvYTActivityTitle = new TextView(this);
        TextView tvYTActivityDescription = new TextView(this);


//        playerView = (YouTubePlayerView) findViewById(R.id.player_view_yt);
//        playerView.initialize(KEY, this);
//        tvYTActivityTitle = (TextView) findViewById(R.id.tvYTActivityTitle);
//        tvDescription = (TextView) findViewById(R.id.tvDescription);
//
        returnCursorVId(getIntent().getStringExtra("pushkin"));
        if (cursor.moveToFirst()){
            tvYTActivityTitle.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN)));
            tvYTActivityDescription.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION_COLUMN)));
//            tvYTActivityTitle.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN)));
//            tvDescription.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION_COLUMN)));
        } else {
            tvYTActivityTitle.setText("курсор пустой");
        }
        tvYTActivityTitle.setLayoutParams(lpView);
        tvYTActivityDescription.setLayoutParams(lpView);
        linLayout.addView(tvYTActivityTitle);
        linLayout.addView(tvYTActivityDescription);


    }

    public Cursor returnCursorVId (String sVId){
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
        if(!b){
            youTubePlayer.cueVideo(getIntent().getStringExtra("pushkin"));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
