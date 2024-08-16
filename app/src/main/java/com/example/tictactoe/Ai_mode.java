package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ai_mode extends AppCompatActivity implements View.OnClickListener {
    EditText filled_name;
    TextView select_symbol;
    AppCompatButton select_x,select_o,play,clicked_button;
    String name,symbol_choice="";
    MediaPlayer mediaPlayer,click_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_mode);
        mediaPlayer= MediaPlayer.create(this,R.raw.tic_tac_toe_bg_sound);
        click_sound=MediaPlayer.create(this,R.raw.button_click_sound);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        filled_name=findViewById(R.id.fill_name);
        select_x=findViewById(R.id.pl_x);
        select_o=findViewById(R.id.pl_o);
        play=findViewById(R.id.play_button);
        select_symbol=findViewById(R.id.select_symbol);
        select_x.setOnClickListener(this);
        select_o.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    private void intentFunction(String player1_name, String player2_name){
        Intent ai_intent=new Intent(this,GamePage.class);
        ai_intent.putExtra("player1_name",player1_name);
        ai_intent.putExtra("player2_name",player2_name);
        startActivity(ai_intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        click_sound.start();
        clicked_button=(AppCompatButton) v;
        switch(clicked_button.getText().toString()){
            case "X":
                symbol_choice="X";
                select_x.setBackgroundColor(getColor(R.color.purple_500));
                select_o.setBackgroundColor(getColor(R.color.Brown));
                break;
            case "O":
                symbol_choice="O";
                select_o.setBackgroundColor(getColor(R.color.purple_500));
                select_x.setBackgroundColor(getColor(R.color.Brown));
                break;
            case "PLAY":
                name=filled_name.getText().toString();
                if (name.isEmpty())
                    name="Player";
                else if (name.equals("AI")) {
                    name="Al";
                }
                switch (symbol_choice) {
                    case "":
                       Toast.makeText(this, "Please choose a symbol to play!", Toast.LENGTH_SHORT).show();
                       break;
                    case "X":
                        intentFunction(name,"AI");
                        break;
                    case "O":
                        intentFunction("AI",name);
                        break;
                }
        }
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