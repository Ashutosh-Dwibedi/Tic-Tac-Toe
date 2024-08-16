package com.example.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    AppCompatButton single_player_button, multiplayer_button, exit_button;
    MediaPlayer music,click_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        single_player_button =findViewById(R.id.single_player_button);
        multiplayer_button =findViewById(R.id.multiplayer_button);
        exit_button =findViewById(R.id.exit_button);
        single_player_button.setOnClickListener(this);
        multiplayer_button.setOnClickListener(this);
        exit_button.setOnClickListener(this);
        music=MediaPlayer.create(this,R.raw.tic_tac_toe_bg_sound);
        click_sound=MediaPlayer.create(this,R.raw.button_click_sound);
        music.setLooping(true);
        music.start();
    }

    @Override
    public void onClick(View v) {
        click_sound.start();
        if(v==single_player_button){
            Intent intent=new Intent(this,Ai_mode.class);
            startActivity(intent);
        } else if(v==multiplayer_button){
            Intent intent=new Intent(this,Player_page.class);
            startActivity(intent);
        } else if(v==exit_button){
            finishAffinity();
            System.exit(0);
        }
    }

    @Override
    protected void onPause() {
        if (music.isPlaying())
            music.pause();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        music.start();
        super.onPostResume();
    }
}