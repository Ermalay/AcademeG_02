package ru.novil.sergey.navigationdraweractivity.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    // имя базы данных
    public static final String DATABASE_NAME = "videodatabase.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String TITLE_COLUMN = "title";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String VIDEO_ID_COLUMN = "videoId";
    public static final String URL_COLUMN = "url";
    public static final String PUBLISHEDAT_COLUMN = "publishedAt";

    // имя таблицы
    public static final String DATABASE_TABLE_ACAGEMEG = "video_academeg";
    public static final String DATABASE_TABLE_ACAGEMEG_2ND_CH = "video_academeg_2nd_ch";

    private static final String DATABASE_CREATE_SCRIPT_ACAGEMEG = "create table " + DATABASE_TABLE_ACAGEMEG
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + TITLE_COLUMN + " text not null, "
            + DESCRIPTION_COLUMN + " text, "
            + VIDEO_ID_COLUMN + " text, "
            + PUBLISHEDAT_COLUMN + " text, "
            + URL_COLUMN + " text);";

    private static final String DATABASE_CREATE_SCRIPT_ACAGEMEG_2ND_CH = "create table " + DATABASE_TABLE_ACAGEMEG_2ND_CH
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + TITLE_COLUMN + " text not null, "
            + DESCRIPTION_COLUMN + " text, "
            + VIDEO_ID_COLUMN + " text, "
            + PUBLISHEDAT_COLUMN + " text, "
            + URL_COLUMN + " text);";

//    + COLUMN_ID_2ND_CH + " integer primary key autoincrement, "
//            + TITLE_COLUMN_2ND_CH + " text not null, "
//            + DESCRIPTION_COLUMN_2ND_CH + " text, "
//            + VIDEO_ID_COLUMN_2ND_CH + " text, "
//            + URL_COLUMN_2ND_CH + " text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public DatabaseHelper(MyAsyncTask myAsyncTask) {
//        super(null, DATABASE_NAME, null, DATABASE_VERSION);
//
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT_ACAGEMEG);
        db.execSQL(DATABASE_CREATE_SCRIPT_ACAGEMEG_2ND_CH);

//        db.execSQL("insert into "
//                + DATABASE_TABLE + " ("
//                + TITLE_COLUMN + ", "
//                + URL_COLUMN + ") values ('Какое-то Название', 'какой-то url адрес');");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_ACAGEMEG);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_ACAGEMEG_2ND_CH);
        // Создаём новую таблицу
        onCreate(db);
    }
}