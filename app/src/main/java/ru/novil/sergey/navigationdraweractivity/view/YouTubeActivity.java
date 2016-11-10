package ru.novil.sergey.navigationdraweractivity.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.novil.sergey.navigationdraweractivity.R;
import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;

public class YouTubeActivity extends AppCompatActivity {
//public class YouTubeActivity  extends YouTubeBaseActivity implements
//        YouTubePlayer.OnInitializedListener {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    int count;

    static private final String DEVELOPER_KEY = "AIzaSyD7VSUJPszW-64AZ4t_9EO90sUHXrkOzHk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
//        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
//        youTubeView.initialize(DEVELOPER_KEY, this);
//    }
//
//    public String videoTitle(){
//        Intent intent = getIntent();
//        count = intent.getIntExtra("pushkin", 0);
//        databaseHelper = new DatabaseHelper(getApplicationContext());
//        sqLiteDatabase = databaseHelper.getReadableDatabase();
//
//        cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseHelper.DATABASE_TABLE, null);
//        cursor.moveToPosition(count);
//        String itemName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.VIDEO_ID_COLUMN));
//        cursor.close();
//        return itemName;
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                        YouTubeInitializationResult error) {
//        Toast.makeText(this, "Блин!" + error.toString(), Toast.LENGTH_LONG)
//                .show();
//    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                        YouTubePlayer player, boolean wasRestored) {
//        player.loadVideo(videoTitle());
    }
}
