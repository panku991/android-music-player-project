package com.example.melody;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {

    //getting mediaPlayer instance
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    //initializing all attributes
    TextView titleTv,currentTime,totalTime,albumTv,artistTv;
    SeekBar seekBar;
    ImageView nextBtn,preBtn,musicIcon,repeatBtn,shuffleBtn,favoriteBtn,queueBtn,back;
    FloatingActionButton pausePlay;
    ArrayList<Music_Model> arrMusic;
    Music_Model currentSong;

    //boolean flags for respective actions
    boolean repeatFlag=false , favoriteFlag=false , shuffleFlag=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        init();



        arrMusic = (ArrayList<Music_Model>) getIntent().getSerializableExtra("LIST");


        setResourceWithMusic();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));


                    titleTv.setSelected(true);
                    albumTv.setSelected(true);


                    //



                    //

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if(repeatFlag==false) {
                                playNextSong();
                            }
                            else{
                                playMusic();
                            }
                        }
                    });


                    if(mediaPlayer.isPlaying()){

                        pausePlay.setImageResource(R.drawable.pause);


                    }
                    else{

                        pausePlay.setImageResource(R.drawable.play);

                    }

                }
                new Handler().postDelayed(this,100);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }


    public void init(){

        titleTv = findViewById(R.id.song_page_song_title);
        currentTime = findViewById(R.id.song_page_current_time);
        totalTime = findViewById(R.id.song_page_total_time);
        artistTv = findViewById(R.id.song_page_song_artist);
        albumTv = findViewById(R.id.song_page_song_album);

        seekBar = findViewById(R.id.song_page_seek_bar);
        pausePlay = findViewById(R.id.img_btn_play);
        nextBtn = findViewById(R.id.img_btn_next);
        preBtn = findViewById(R.id.img_btn_pre);
        repeatBtn = findViewById(R.id.img_btn_repeat);
        shuffleBtn = findViewById(R.id.img_btn_shuffle);
        musicIcon = findViewById(R.id.song_page_music_icon);
        queueBtn = findViewById(R.id.queue_button);
        favoriteBtn = findViewById(R.id.favorite_Button);






    }


    public void setResourceWithMusic(){


        currentSong = arrMusic.get(MyMediaPlayer.currentIndex);


        byte[] image = getAlbumArt_img(currentSong.getPath());

        if (image != null){
            Glide.with(getApplicationContext()).asBitmap().load(image).into(musicIcon);
        }
        else{
            Glide.with(getApplicationContext()).load(R.mipmap.img).into(musicIcon);
        }

        titleTv.setText(currentSong.getTitle());
        artistTv.setText(currentSong.getArtist());
        albumTv.setText(currentSong.getAlbum());

        totalTime.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(view -> pausePlay());
        nextBtn.setOnClickListener(view -> playNextSong());
        preBtn.setOnClickListener(view -> playPreSong());
        repeatBtn.setOnClickListener(view -> repeatSong());
        favoriteBtn.setOnClickListener(view -> favoriteSong());
        queueBtn.setOnClickListener(view -> queueSongs());
        shuffleBtn.setOnClickListener(view -> shuffleSongs());


        favoriteBtn.setImageResource(R.drawable.favorite_border);


        playMusic();




    }



    private void playMusic(){

        mediaPlayer.reset();

        try {

            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void playNextSong() {

        if(!shuffleFlag){
                if (MyMediaPlayer.currentIndex == arrMusic.size() - 1) {
                    return;
                } else {
                    MyMediaPlayer.currentIndex += 1;
                    mediaPlayer.reset();
                    favoriteFlag = false;
                    setResourceWithMusic();

                }
        }
        else {
            shufflegetsong();
        }


    }

    private void playPreSong() {
            if(!shuffleFlag){

                    if (MyMediaPlayer.currentIndex == 0) {
                        return;
                    } else {
                        MyMediaPlayer.currentIndex -= 1;
                        mediaPlayer.reset();
                        favoriteFlag = false;
                        setResourceWithMusic();

                    }

            }
            else {
                shufflegetsong();
            }


    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else{
            mediaPlayer.start();
        }
    }

    private void repeatSong(){
        if (repeatFlag) {
            Toast.makeText(this, "Replaying Removed..", Toast.LENGTH_SHORT).show();
            mediaPlayer.setLooping(false);
            repeatBtn.setImageResource(R.drawable.repeat);
            repeatFlag = false;
        } else {
            Toast.makeText(this, "Replaying Added..", Toast.LENGTH_SHORT).show();
            mediaPlayer.setLooping(true);
            repeatBtn.setImageResource(R.drawable.repeat_one);
            repeatFlag = true;
        }
    }

    private void favoriteSong(){
        if (favoriteFlag) {
            Toast.makeText(this, "Favorite removed..", Toast.LENGTH_SHORT).show();
//
//            DBHelper dbHelper = new DBHelper(this);
//            dbHelper.removeSongs(currentSong.getPath());

            favoriteBtn.setImageResource(R.drawable.favorite_border);
            favoriteFlag = false;


        } else {
            Toast.makeText(this, "Favorite Added..", Toast.LENGTH_SHORT).show();

            DBHelper dbHelper = new DBHelper(this);
            dbHelper.addSongs(currentSong.getPath(),currentSong.getTitle(),currentSong.getArtist(),currentSong.getAlbum(),currentSong.getDuration());

            favoriteBtn.setImageResource(R.drawable.favorite);
            favoriteFlag = true;
        }
    }

    public void queueSongs(){
        Intent goQueue = new Intent(this, Queue.class);
        startActivity(goQueue);
    }

    public void shuffleSongs(){
        if(shuffleFlag){
            Toast.makeText(this, "shuffle off..", Toast.LENGTH_SHORT).show();
            shuffleBtn.setImageResource(R.drawable.shuffle);
            shuffleFlag=false;
        }else {
            Toast.makeText(this, "shuffle on..", Toast.LENGTH_SHORT).show();
            shuffleBtn.setImageResource(R.drawable.shuffle_on);
            shuffleFlag=true;
        }

    }

    public void shufflegetsong(){
        Random rand = new Random();
        int pos = rand.nextInt(arrMusic.size()-1);
        MyMediaPlayer.currentIndex=pos;
        mediaPlayer.reset();
        favoriteFlag = false;
        setResourceWithMusic();
    }



    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));

    }

    private byte[] getAlbumArt_img(String path){
        Uri uri = Uri.parse(path);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }


}