package ru.novil.sergey.navigationdraweractivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import ru.novil.sergey.navigationdraweractivity.sqlite.DatabaseHelper;

public class Delete_It extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    TextView textView6;
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
        setContentView(R.layout.activity_delete__it);

        playerView = (YouTubePlayerView) findViewById(R.id.player_view);
        playerView.initialize(KEY, this);




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
