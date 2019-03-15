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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class SimonReverse extends AppCompatActivity {
    Context context;
    ImageButton greenButton, redButton, yellowButton, blueButton;
    int[] button_ids = new int[]{R.id.blue_ib, R.id.red_ib, R.id.green_im, R.id.yellow_ib};
    int x;
    final int CAPACITY = 500;
    Vector<Integer> simonPattern = new Vector<>();
    Vector<Integer> userPattern = new Vector<>();
    int currentScore = 0, highScore;
    int numItemsInArray = 0, numberOfClicks = 0, loseSound;
    public SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    Random rand = new Random();
    final Handler handler = new Handler();
    Boolean isSimonClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);

        // add a colorful text to the title_text view with html
        TextView title_tv = findViewById(R.id.title_tv);
        String text = "<font color=#cc0029> R</font><font color=#ffcc00>E</font>" +
                "<font color=#00B2EE>W</font><font color=#00ff00>I</font>" +
                "<font color=#ffcc00>N</font><font color=#cc0029>D</font>";
        title_tv.setText(Html.fromHtml(text));

        context = getApplicationContext();
        // saves the high score
        SharedPreferences prefs = this.getSharedPreferences("GET_HIGH_SCORE", Context.MODE_PRIVATE);
        highScore = prefs.getInt("HIGH_SCORE", 0);

        // updates the textview for the high score
        runOnUiThread(new Runnable() {
            public void run() {
                TextView tv = findViewById(R.id.high_score3_tv);
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

        //start the game:
        playGame();

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
                // if the user gets it wrong:
                if (userPattern.get(numberOfClicks) != x) {
                    soundPool.play(loseSound, 1, 1, 1, 0, 1f);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SimonReverse.this);
                    String message = "<html>" +
                            "<br><font color=#cc0029 size=><b>GAME OVER</b></font><br><br>" + "</html>";
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                    alertDialogBuilder.setMessage(Html.fromHtml(message + "your score was " + currentScore));
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;

                }
                //if the user gets its right
                Sound.makeSound(context, v.getId());
                Sound.lightUp(v);
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
            if (isSimonClick) {
                for (int k = 0; k < button_ids.length; k++) {
                    ImageButton imageButton = findViewById(button_ids[i]);
                    imageButton.setFocusableInTouchMode(false);
                }
            }
            isSimonClick = false;
        }

        //Vector<Integer> userPattern = new Vector<>(size);
        //Collections.copy(userPattern, simonPattern);
        //userPattern = (Vector)simonPattern.clone();

        for (int i = 0; i < simonPattern.size(); i++) {
            userPattern.add(simonPattern.get(i));
        }
        reversePattern();
    }

    private void addToArray() {  // add random number to the first free position in the array
        for (int i = 0; i < CAPACITY; i++) {
            simonPattern.add(Sound.random(4));
            break;
            // }

        }
    }

    // this method reverse the userPattern vector:
    private void reversePattern() {

        Collections.reverse(userPattern);

    }

    public void simonClick(final int click_index) {
        final Runnable runnable = new Runnable() {
            public void run() {
                if (simonPattern.get(click_index) == 1) {
                    Sound.makeSound(context, R.id.green_im);
                    Sound.lightUp(greenButton);
                    isSimonClick = true;
                } else if (simonPattern.get(click_index) == 2) {
                    Sound.makeSound(context, R.id.red_ib);
                    Sound.lightUp(redButton);
                    isSimonClick = true;
                } else if (simonPattern.get(click_index) == 3) {
                    Sound.makeSound(context, R.id.yellow_ib);
                    Sound.lightUp(yellowButton);
                    isSimonClick = true;
                } else {
                    Sound.makeSound(context, R.id.blue_ib);
                    Sound.lightUp(blueButton);
                    isSimonClick = true;
                }

            }
        };

        handler.postDelayed(runnable, (1500) * click_index);

    }

    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<h2>How to Play</h2>" +
                    "<p>The game will randomly pick one of the four buttons, light it up, and play a " +
                    "sound. You must press the same button.Simon plays that button again, and then randomly" +
                    " chooses another button. You must hit those  buttons in the reverse order! Simon " +
                    "keeps adding buttons growing the pattern, and you must keep pressing all the buttons, " +
                    "until you hit a wrong button.</p>" +
                    "</html>";


            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFD5D8DC));


            TextView tv = dialog.findViewById(android.R.id.message); // sets html in TV
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("highScore", highScore);
    }

    private class Bool {
    }
}


