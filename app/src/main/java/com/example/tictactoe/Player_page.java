package com.example.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class Player_page extends AppCompatActivity {

    String player1="Player-1",player2="Player-2";
    EditText editText1,editText2;
    AppCompatButton play;
    MediaPlayer mediaPlayer,click_sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_page);
        mediaPlayer= MediaPlayer.create(this,R.raw.tic_tac_toe_bg_sound);
        click_sound=MediaPlayer.create(this,R.raw.button_click_sound);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        editText1=findViewById(R.id.player2_edit_text);
        editText2=findViewById(R.id.player1_edit_text);
        play=findViewById(R.id.enterButton);
        play.setOnClickListener(v -> {
            click_sound.start();
            if (!editText1.getText().toString().isEmpty()){
                player1 = editText1.getText().toString();
            }
            if (!editText2.getText().toString().isEmpty()){
                player2 = editText2.getText().toString();
            }
            if (player1.equals("AI")&&player2.equals("AI"))
                player1=player2="Al";
            else if(player1.equals("AI"))
                player1="Al";
            else if (player2.equals("AI")) {
                player2="Al";
            }
            Intent intent = new Intent(this, GamePage.class);
            intent.putExtra("player1_name", player1);
            intent.putExtra("player2_name", player2);
            startActivity(intent);
            finish();
        });
    }
    @Override
    protected void onPause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        mediaPlayer.start();
        super.onPostResume();
    }
}