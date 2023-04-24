package com.example.melody;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="FAVSongs";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="songs";


    private static final String SONG_PATH="song_path";
    private static final String SONG_TITLE="song_title";
    private static final String SONG_ARTIST="song_artist";
    private static final String SONG_ALBUM="song_album";
    private static final String SONG_DURATION="song_duration";


//    private String path;
//    private String title;
//    private String artist;
//    private String album;
//    private String duration;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    db.execSQL(" CREATE TABLE "+ TABLE_NAME +" ( " + SONG_PATH + " TEXT UNIQUE  , "  + SONG_TITLE + " TEXT , " + SONG_ARTIST + " TEXT, "   + SONG_ALBUM + " TEXT , " + SONG_DURATION + " TEXT "+ " ) ; ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        onCreate(db);

    }


    public void addSongs( String song_path ,String song_title , String song_artist , String song_album , String song_duration ){


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(SONG_PATH,song_path);
        contentValues.put(SONG_TITLE,song_title);
        contentValues.put(SONG_ARTIST,song_artist);
        contentValues.put(SONG_ALBUM,song_album);
        contentValues.put(SONG_DURATION,song_duration);





        db.insert(TABLE_NAME,null,contentValues);

    }


    public ArrayList<Music_Model> fetchFavSongs(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);

        ArrayList<Music_Model> tempArr = new ArrayList<>();

        cursor.moveToFirst();

        do {

            Music_Model tempModel = new Music_Model();
            tempModel.setPath(cursor.getString(0));
            tempModel.setTitle(cursor.getString(1));
            tempModel.setArtist(cursor.getString(2));
            tempModel.setArtist(cursor.getString(3));
            tempModel.setDuration(cursor.getString(4));


            tempArr.add(tempModel);

        }while (cursor.moveToNext());


        return tempArr;
    }

    public void removeSongs(String song_path ){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE " + SONG_PATH + " LIKE " +"'%"+ song_path + "%'" );


    }



}
