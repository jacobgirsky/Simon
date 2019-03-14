package com.example.simon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import java.util.Vector;

public class Game1Activity extends AppCompatActivity {
    Context context;
    ImageButton greenButton, redButton, yellowButton, blueButton;
    int x;
    final int CAPACITY = 500;
    int moves[] = new int[CAPACITY];
    int currentScore = 0, highScore;
    int numItemsInArray = 0, numberOfClicksEachLevel = 0, loseSound;
    public SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    Random rand = new Random();
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        context = getApplicationContext();
        // saves the high score
        SharedPreferences prefs = this.getSharedPreferences("GET_HIGH_SCORE", Context.MODE_PRIVATE);
        highScore = prefs.getInt("HIGH_SCORE", 0);

        // updates the textview for the high score
        runOnUiThread(new Runnable() {
            public void run() {
                TextView tv = findViewById(R.id.high_score_tv);
                tv.setText("High score: " + highScore);
                Log.i("HIGH SCORE", "High score: " + highScore);
            }
        });


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
                }

                if (moves[numberOfClicksEachLevel] != x) { // If the user gets it wrong
                    soundPool.play(loseSound, 1, 1, 1, 0, 1f);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Game1Activity.this);
                    alertDialogBuilder.setMessage("GAME OVER, your score was " + currentScore);
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    return true;
                }
                //if the user gets its right
                    Sound.makeSound(context,v.getId());
                    Sound.lightUp(v);
                    numberOfClicksEachLevel++;
                    final TextView tv = findViewById(R.id.current_score_tv);
                    TextView textView = findViewById(R.id.high_score_tv);

                    if (numItemsInArray == numberOfClicksEachLevel) {

                        currentScore++;
                        tv.setText("Current score: " + currentScore);

                        numberOfClicksEachLevel = 0;
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

    // adds the sounds to the correct buttons that are being pressed
   /* private void makeSound(int soundID) {
        //int audioRes = 0;
        Sound audioRes = new Sound();
        if (soundID == R.id.green_im) {
            //audioRes = R.raw.greenbutton;
            audioRes.setSound(R.raw.greenbutton);
        }
        else if (soundID == R.id.red_ib) {
           // audioRes = R.raw.redbutton;
            audioRes.setSound(R.raw.redbutton);
        }
        else if (soundID == R.id.yellow_ib) {
          //  audioRes = R.raw.yellowbutton;
            audioRes.setSound(R.raw.yellowbutton);
        }
        else if (soundID == R.id.blue_ib) {
           // audioRes = R.raw.bluebutton;
            audioRes.setSound(R.raw.greenbutton);
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(this, audioRes.getSound());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
*/
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
                   Sound.makeSound(context,R.id.green_im);
                    Sound.lightUp(greenButton);

                } else if (moves[click_index] == 2) {
                    Sound.makeSound(context,R.id.red_ib);
                    Sound.lightUp(redButton);
                } else if (moves[click_index] == 3) {
                    Sound.makeSound(context,R.id.yellow_ib);
                    Sound.lightUp(yellowButton);
                } else {
                    Sound.makeSound(context,R.id.blue_ib);
                    Sound.lightUp(blueButton);
                }
            }
        };

        handler.postDelayed(runnable, (1500) * click_index);

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

}


