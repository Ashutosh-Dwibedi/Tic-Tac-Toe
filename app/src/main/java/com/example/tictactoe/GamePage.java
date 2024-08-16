package com.example.tictactoe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class GamePage extends AppCompatActivity implements View.OnClickListener {
    int selected_position;
    String symbol;
    int player = 0, counter = 0;
    AppCompatButton reset;
    String player1, player2;
    TextView playerView1, playerView2;
    MediaPlayer win_song, lose_song, draw_song, click_sound;
    GridLayout all_views_grid;
    ArrayList<View> all_buttons;
    AppCompatButton button_clicked;
    private final int[][] reference_matrix = new int[3][3];
    ArrayList<Integer> empty_buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        reset = findViewById(R.id.reset_button);
        playerView1 = findViewById(R.id.playerView1);
        playerView2 = findViewById(R.id.playerView2);
        all_views_grid = findViewById(R.id.gridLayout);
        win_song = MediaPlayer.create(this, R.raw.won);
        lose_song = MediaPlayer.create(this, R.raw.lost);
        draw_song = MediaPlayer.create(this, R.raw.draw_game_sound);
        click_sound = MediaPlayer.create(this, R.raw.button_click_sound);
        all_buttons = all_views_grid.getTouchables();
        Intent intent = getIntent();
        player1 = intent.getStringExtra("player1_name");
        player2 = intent.getStringExtra("player2_name");
        playerView1.setText(player1);
        playerView2.setText(player2);
        clickListenerSetter();
        matrixInitializer();
        emptyButtonsListMaker();
        if (player1.equals("AI")) {
            methodAI();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(GamePage.this).setTitle("Exit Current Match?")
                        .setMessage("(Your progress will not be saved)")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
            }
        });
    }
    // Basic operations section

    private void matrixInitializer() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                reference_matrix[i][j] = 0;
            }
        }
    }

    private void emptyButtonsListMaker() {
        empty_buttons.clear();
        for (int i = 0; i < 9; i++) {
            empty_buttons.add(i);
        }
    }

    private void clickListenerSetter() {
        reset.setOnClickListener(this);
        for (View view : all_buttons) {
            if (view instanceof AppCompatButton) {
                view.setOnClickListener(this);
            }
        }
    }

    // Support Logic functions section

    private void randoPosition() {
        Collections.shuffle(empty_buttons);
        selected_position = empty_buttons.get(0);
        implementAI(selected_position);
    }

    public void DelayMethod(AppCompatButton button) {
        new Handler().postDelayed(() -> {
            enabler();
            button.setText(symbol);
            winnerDecider();
        }, 500);
    }

    private void enabler() {
        for (View view : all_buttons) {
            if (view instanceof AppCompatButton) {
                view.setFocusable(true);
                view.setEnabled(true);
            }
        }
    }

    private void disabler() {
        for (View view : all_buttons) {
            if (view instanceof AppCompatButton) {
                view.setFocusable(false);
                view.setEnabled(false);
            }
        }
    }

    private ArrayList<Integer> matrixCalculator() {
        ArrayList<Integer> sum_list = new ArrayList<>();
        sum_list.add(reference_matrix[0][0] + reference_matrix[0][1] + reference_matrix[0][2]);
        sum_list.add(reference_matrix[1][0] + reference_matrix[1][1] + reference_matrix[1][2]);
        sum_list.add(reference_matrix[2][0] + reference_matrix[2][1] + reference_matrix[2][2]);
        sum_list.add(reference_matrix[0][0] + reference_matrix[1][0] + reference_matrix[2][0]);
        sum_list.add(reference_matrix[0][1] + reference_matrix[1][1] + reference_matrix[2][1]);
        sum_list.add(reference_matrix[0][2] + reference_matrix[1][2] + reference_matrix[2][2]);
        sum_list.add(reference_matrix[0][0] + reference_matrix[1][1] + reference_matrix[2][2]);
        sum_list.add(reference_matrix[0][2] + reference_matrix[1][1] + reference_matrix[2][0]);
        return sum_list;
    }

    private boolean winnerDecider() {
        ArrayList<Integer> matrix_calculator = matrixCalculator();
        if (matrix_calculator.contains(3)) {
            new Handler().postDelayed(() -> resultDialog("O"), 200);
            return false;
        } else if (matrix_calculator.contains(12)) {
            new Handler().postDelayed(() -> resultDialog("X"), 200);
            return false;
        } else if (counter == 9) {
            new Handler().postDelayed(this::drawDialog, 200);
            return false;
        }
        return true;
    }

    private void matrixUpdate(int i, int j) {
        if (symbol.equals("X"))
            reference_matrix[i][j] = 4;
        else if (symbol.equals("O")) {
            reference_matrix[i][j] = 1;
        }
    }

    void newGame() {
        counter = 0;
        player = 0;
        for (View view : all_buttons) {
            if (view instanceof AppCompatButton) {
                ((AppCompatButton) view).setText("");
            }
        }
        enabler();
        matrixInitializer();
        emptyButtonsListMaker();
        if (player1.equals("AI")) {
            methodAI();
        }
    }

    private void actionOnClick(AppCompatButton clicked_button, int i, int j) {
        counter++;
        if (player == 0) {
            player = 1;
            symbol = "O";
            clicked_button.setText("X");
            reference_matrix[i][j] = 4;
        } else if (player == 1) {
            player = 0;
            symbol = "X";
            clicked_button.setText("O");
            reference_matrix[i][j] = 1;
        }
        if (player1.equals("AI") || player2.equals("AI")) {
            disabler();
            if (counter <= 3) {
                methodAI();
            } else if (winnerDecider())
                methodAI();
        } else if (counter > 4) {
            winnerDecider();
        }
    }

    // AI logic section

    private void methodAI() {
        counter++;
        if (counter <= 9) {
            if (player1.equals("AI")) {
                if (player == 0) {
                    symbol = "X";
                    player = 1;
                    if (counter < 3 || strategicAssignment())
                        randoPosition();
                }
            } else if (player2.equals("AI")) {
                if (player == 1) {
                    symbol = "O";
                    player = 0;
                    if (counter < 2 || strategicAssignment())
                        randoPosition();
                }
            }
        }
    }

    private void calculativeAssign(int rcd_index) {
        if (rcd_index <= 2) {
            for (int j = 0; j <= 2; j++) {
                if (reference_matrix[rcd_index][j] == 0) {
                    implementAI(3 * rcd_index + j);
                    return;
                }
            }
        } else if (rcd_index <= 5) {
            for (int j = 0; j <= 2; j++) {
                if (reference_matrix[j][rcd_index - 3] == 0) {
                    implementAI((3 * j) + (rcd_index - 3));
                    return;
                }
            }
        } else if (rcd_index == 6) {
            for (int j = 0; j <= 2; j++) {
                if (reference_matrix[j][j] == 0) {
                    implementAI((3 * j) + j);
                    return;
                }
            }
        } else if (rcd_index == 7) {
            for (int j = 0; j <= 2; j++) {
                int k = 2;
                if (reference_matrix[j][k - j] == 0) {
                    implementAI((3 * j) + (k - j));
                    return;
                }
            }
        }
    }

    private boolean strategicAssignment() {
        int rcd_index;
        ArrayList<Integer> matrix_rcd_calculator = matrixCalculator();
        if (symbol.equals("X")) {
            if (matrix_rcd_calculator.contains(8)) {
                rcd_index = matrix_rcd_calculator.indexOf(8);
                calculativeAssign(rcd_index);
                return false;
            } else if (matrix_rcd_calculator.contains(2)) {
                rcd_index = matrix_rcd_calculator.indexOf(2);
                calculativeAssign(rcd_index);
                return false;
            }
        } else if (symbol.equals("O")) {
            if (matrix_rcd_calculator.contains(2)) {
                rcd_index = matrix_rcd_calculator.indexOf(2);
                calculativeAssign(rcd_index);
                return false;
            } else if (matrix_rcd_calculator.contains(8)) {
                rcd_index = matrix_rcd_calculator.indexOf(8);
                calculativeAssign(rcd_index);
                return false;
            }
        }
        return true;
    }

    private void implementAI(int selected_position) {
        DelayMethod((AppCompatButton) all_buttons.get(selected_position));
        empty_buttons.remove((Integer) selected_position);
        matrixUpdate(selected_position / 3, selected_position % 3);
    }

    // Customization section

    private void drawDialog() {
        draw_song.start();
        Dialog draw_dialog = new Dialog(this);
        draw_dialog.setContentView(R.layout.draw_dialog);
        Objects.requireNonNull(draw_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        draw_dialog.setOnDismissListener(dialog -> newGame());
        draw_dialog.show();
    }

    private void resultDialog(String symbol) {
        Dialog result_dialog = new Dialog(this);
        result_dialog.setContentView(R.layout.custom_result_dialog);
        TextView winner_details = result_dialog.findViewById(R.id.winnerShowText);
        if (player1.equals("AI")) {
            if (symbol.equals("X")) {
                lose_song.start();
                winner_details.setText(player1);
            } else {
                win_song.start();
                winner_details.setText(player2);
            }
        } else if (player2.equals("AI")) {
            if (symbol.equals("O")) {
                lose_song.start();
                winner_details.setText(player2);
            } else {
                win_song.start();
                winner_details.setText(player1);
            }
        } else {
            win_song.start();
            if (symbol.equals("X"))
                winner_details.setText(player1);
            else
                winner_details.setText(player2);
        }
        Objects.requireNonNull(result_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        result_dialog.setOnDismissListener(dialog -> newGame());
        result_dialog.show();
    }

    @Override
    public void onClick(View v) {
        click_sound.start();
        button_clicked = (AppCompatButton) v;
        if (v == reset) {
            enabler();
            newGame();
        } else {
            if (button_clicked.getText().toString().isEmpty()) {
                int click_index = all_buttons.indexOf(v);
                empty_buttons.remove((Integer) click_index);
                actionOnClick(button_clicked, click_index / 3, click_index % 3);
            }
        }
    }
}