package com.example.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btnPlay, btnNext,btnPrev,btnForward,btnBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar seekBar;

    ImageView imageView;
    String songName;
    public static final String EXTRA_NAME="song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Now Playing...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnPlay=(Button) findViewById(R.id.btnPlay);
        btnNext=(Button) findViewById(R.id.btnNext);
        btnPrev=(Button) findViewById(R.id.btnPrev);
        btnForward=(Button) findViewById(R.id.fastForward);
        btnBackward=(Button) findViewById(R.id.fastBackward);

        txtSongName=(TextView) findViewById(R.id.TxtSong);
        txtSongStart=(TextView) findViewById(R.id.txtSongStart);
        txtSongEnd=(TextView) findViewById(R.id.txtSongEnd);
        seekBar=findViewById(R.id.seekBar);
        imageView=findViewById(R.id.imgView);

        if (mediaPlayer!=null){
            mediaPlayer.start();
            mediaPlayer.release();
        }

        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();
        mySongs=(ArrayList)bundle.getParcelableArrayList("songs");
        String sName=intent.getStringExtra("songname");
        position=bundle.getInt("position",0);
        txtSongName.setSelected(true);
        Uri uri= Uri.parse(mySongs.get(position).toString());
        songName=mySongs.get(position).getName();
        txtSongName.setText(songName);

        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        updateSeekBar=new Thread(){
            @Override
            public void run(){
                int total=mediaPlayer.getDuration();
                int current=0;
                while (current<total){
                    try {
                        sleep(500);
                        current=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(current);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }

                }
            }

        };
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_700),PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime=createTime(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);

        final Handler handler=new Handler();
        final int delay=1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime=createTime(mediaPlayer.getCurrentPosition());
                txtSongStart.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);



        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);

                }
                else
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnNext.performClick();
            }
        });






        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=(position+1)%mySongs.size();
                Uri uri =Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                songName=mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();
                startAnimation(imageView,360f);

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)<0)?(mySongs.size()-1):position-1;
                Uri uri =Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                songName=mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();
                startAnimation(imageView,-360f);

            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }
            }
        });


        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });

    }

    public void startAnimation(View view, Float degree){
        ObjectAnimator obj=ObjectAnimator.ofFloat(imageView,"rotation",0f,degree);
        obj.setDuration(1000);
        AnimatorSet set=new AnimatorSet();
        set.playTogether(obj);
        set.start();
    }

    public String createTime(int duration){
        String time=" ";
        int min=duration/1000/60;
        int sec=duration/1000%60;
        time=time+min+":";
        if (sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }

}