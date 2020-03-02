package com.example.simon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class SimonPlus extends AppCompatActivity {
    Context context;
    ImageButton greenButton, redButton, yellowButton, blueButton, tealButton, purpleButton;
    int x;
    final int CAPACITY = 50;
    int moves[] = new int[CAPACITY];
    int currentScore = 0, highScore;
    int numItemsInArray = 0, numberOfClicks = 0, loseSound;
    public SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    final Handler handler = new Handler();
    Sound sound = new Sound();
    SimonOriginal simonOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        simonOriginal = new SimonOriginal();

        context = getApplicationContext();
        // saves the high score
        SharedPreferences prefs = this.getSharedPreferences("GET_HIGH_SCORE", Context.MODE_PRIVATE);
        highScore = prefs.getInt("HIGH_SCORE", 0);

        // updates the textview for the high score
        runOnUiThread(new Runnable() {
            public void run() {
                TextView tv = findViewById(R.id.high_score2_tv);
                tv.setText("High score: " + highScore);
                Log.i("HIGH SCORE", "High score: " + highScore);
            }
        });


        loseSound = soundPool.load(this, R.raw.lose, 1);

        String message = "<html>" +
                "<h2>How to Play</h2>" +
                "<p>The game will randomly pick one of the eight  buttons, light it up, and play a " +
                "sound. You must press the same button. Simon plays that button again, and then randomly" +
                " chooses another button. You must hit those two buttons in the correct order. Simon " +
                "keeps adding buttons growing the pattern, and you must keep pressing all the buttons, " +
                "until you hit a wrong button.</p>" +
                "</html>";

        AboutListener aboutListener = new AboutListener(message);

        findViewById(R.id.how_to_im).setOnClickListener(aboutListener);

        greenButton = findViewById(R.id.green_im);
        redButton = findViewById(R.id.red_ib);
        yellowButton = findViewById(R.id.yellow_ib);
        blueButton = findViewById(R.id.blue_ib);
        tealButton = findViewById(R.id.teal_ib);
        purpleButton = findViewById(R.id.purple_ib);

        greenButton.setOnTouchListener(clicked);
        redButton.setOnTouchListener(clicked);
        yellowButton.setOnTouchListener(clicked);
        blueButton.setOnTouchListener(clicked);
        tealButton.setOnTouchListener(clicked);
        purpleButton.setOnTouchListener(clicked);

        playGame();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    View.OnTouchListener clicked = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                switch (v.getId()) {
                    case R.id.green_im:
                        x = 1;
                        break;
                    case R.id.red_ib:
                        x = 2;
                        break;
                    case R.id.yellow_ib:
                        x = 3;
                        break;
                    case R.id.blue_ib:
                        x = 4;
                        break;
                    case R.id.teal_ib:
                        x = 5;
                        break;
                    case R.id.purple_ib:
                        x = 6;
                        break;
                }

                if (moves[numberOfClicks] != x) { // If the user gets it wrong
                    soundPool.play(loseSound, 1, 1, 1, 0, 1f);

                    getAlertDialog();

                    return true;
                }
                //if the user gets its right
                sound.makeSound(context, v.getId());
                sound.lightUp(v);
                numberOfClicks++;
                final TextView tv = findViewById(R.id.current_score_tv);
                TextView textView = findViewById(R.id.high_score_tv);

                if (numItemsInArray == numberOfClicks) {

                    currentScore++;
                    tv.setText("Current score: " + currentScore);

                    numberOfClicks = 0;
                    if (numItemsInArray > highScore) {
                        highScore = numItemsInArray;
                        SharedPreferences highScores = getSharedPreferences("GET_HIGH_SCORE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = highScores.edit();
                        editor.putInt("HIGH_SCORE", highScore);
                        editor.commit();

                        textView.setText("High score: " + highScore);

                    }
                    final Runnable runnable = new Runnable() {
                        public void run() {
                            playGame();
                        }
                    };
                    handler.postDelayed(runnable, 1500);
                }
            }
            return true;
        }
    };

    public void playGame() {
        addToArray();
        numItemsInArray++;
        for (int i = 0; i < numItemsInArray; i++) {
            simonClick(i);
        }
    }

    private void addToArray() {  // add random number to the first free position in the array
        for (int i = 0; i < CAPACITY; i++) {
            if (moves[i] == 0) {
                Random rand = new Random();
                moves[i] = rand.nextInt(4) + 1;
                break;
            }
        }
    }

    public void simonClick(final int click_index) {
        final Runnable runnable = new Runnable() {
            public void run() {
                if (moves[click_index] == 1) {
                    sound.makeSound(context, R.id.green_im);
                    sound.lightUp(greenButton);
                } else if (moves[click_index] == 2) {
                    sound.makeSound(context, R.id.red_ib);
                    sound.lightUp(redButton);
                } else if (moves[click_index] == 3) {
                    sound.makeSound(context, R.id.yellow_ib);
                    sound.lightUp(yellowButton);
                } else if (moves[click_index] == 4) {
                    sound.makeSound(context, R.id.blue_ib);
                    sound.lightUp(blueButton);
                } else if (moves[click_index] == 5) {
                    sound.makeSound(context, R.id.teal_ib);
                    sound.lightUp(tealButton);
                } else {
                    sound.makeSound(context, R.id.purple_ib);
                    sound.lightUp(purpleButton);
                }
            }
        };

        handler.postDelayed(runnable, (1300) * click_index);
    }

    private void getAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SimonPlus.this);
        String message = "<html>" +
                "<br><font color=#cc0029 size=><b>GAME OVER</b></font><br><br>" + "</html>";

        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setMessage(Html.fromHtml(message + "your score was " + currentScore));
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        TextView tv = dialog.findViewById(android.R.id.message); // sets html in TV
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

}


