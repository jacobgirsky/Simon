package com.example.simon;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game1Activity extends AppCompatActivity {

    private ImageButton greenButton, redButton, yellowButton, blueButton;
    private int x;
    private final int CAPACITY = 500;
    private int moves[] = new int[CAPACITY];
    private int currentScore = 0, highScore;
    private int numItemsInArray = 0, numberOfClicksEachLevel = 0, loseSound;
    public SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    Random rand = new Random();
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        if (savedInstanceState == null) {
            highScore = 0;
        } else {
            highScore = savedInstanceState.getInt("highScore", 0);
        }

        loseSound = soundPool.load(this, R.raw.lose, 1);

        findViewById(R.id.how_to_im).setOnClickListener(new AboutListener());

        greenButton = findViewById(R.id.green_im);
        redButton = findViewById(R.id.red_ib);
        yellowButton = findViewById(R.id.yellow_ib);
        blueButton = findViewById(R.id.blue_ib);

        greenButton.setOnTouchListener(clicked);
        redButton.setOnTouchListener(clicked);
        yellowButton.setOnTouchListener(clicked);
        blueButton.setOnTouchListener(clicked);



        playGame();

    }

    // user clicking the button
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
                }

                // If the user gets it wrong
                if (moves[numberOfClicksEachLevel] != x) {
                    soundPool.play(loseSound, 1, 1, 1, 0, 1f);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Game1Activity.this);
                    alertDialogBuilder.setMessage("GAME OVER, your score was " + currentScore);
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int arg) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    return true;
                }
                //if the user gets its right
                makeSound(v.getId());
                lightUp(v);
                numberOfClicksEachLevel++;
                final TextView tv = findViewById(R.id.current_score_tv);
                TextView textView = findViewById(R.id.high_score_tv);

                if (numItemsInArray == numberOfClicksEachLevel) {

                    currentScore++;
                    tv.setText("Current score: " + currentScore);

                    numberOfClicksEachLevel = 0;
                    if (numItemsInArray > highScore) {
                        highScore = numItemsInArray;
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

    public void simonClick(final int click_index) {
        final Runnable runnable = new Runnable() {
            public void run() {
                if (moves[click_index] == 1) {
                    makeSound(R.id.green_im);
                    lightUp(greenButton);
                } else if (moves[click_index] == 2) {
                    makeSound(R.id.red_ib);
                    lightUp(redButton);
                } else if (moves[click_index] == 3) {
                    makeSound(R.id.yellow_ib);
                    lightUp(yellowButton);
                } else {
                    makeSound(R.id.blue_ib);
                    lightUp(blueButton);
                }
            }
        };

        handler.postDelayed(runnable, (1500) * click_index);

    }

    // adds the sounds to the correct buttons that are being pressed
    private void makeSound(int soundID) {
        int audioRes = 0;
        if (soundID == R.id.green_im) {
            audioRes = R.raw.greenbutton;
        }
        else if (soundID == R.id.red_ib) {
            audioRes = R.raw.redbutton;
        }
        else if (soundID == R.id.yellow_ib) {
            audioRes = R.raw.yellowbutton;
        }
        else if (soundID == R.id.blue_ib) {
            audioRes = R.raw.bluebutton;
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(this, audioRes);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }


    // makes the buttons light up
    private void lightUp(View v) {
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(300);
        mAnimation.setInterpolator(new LinearInterpolator());
        v.startAnimation(mAnimation);
    }



    private int random() {
        return rand.nextInt(4) + 1; // generate a random number between 1 and 4
    }

    private void addToArray() {  // add random number to the first free position in the array
        for (int i = 0; i < CAPACITY; i++) {
            if (moves[i] == 0) {
                moves[i] = random();
                break;
            }
        }
    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<h2>How to Play</h2>" +
                    "<p>The game will randomly pick one of the four buttons, light it up, and play a " +
                    "sound. You must press the same button. Simon plays that button again, and then randomly" +
                    " chooses another button. You must hit those two buttons in the correct order. Simon " +
                    "keeps adding buttons growing the pattern, and you must keep pressing all the buttons, " +
                    "until you hit a wrong button.</p>" +
                    "</html>"; // need to fix html


            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("OK" , null);

            AlertDialog dialog = builder.create();
            dialog.show();

            TextView tv = dialog.findViewById(android.R.id.message); // sets html in TV
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("highScore", highScore);
    }
}


