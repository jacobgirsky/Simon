package com.example.simon;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class Game1Activity extends AppCompatActivity {

    ImageButton greenButton, redButton, yellowButton, blueButton;
    int x;
    final int CAPACITY = 500;
    int moves[] = new int[CAPACITY];
    int currentScore = 0;
    int highScore = 0;
    public int numItemsInArray = 0, k = 0, numberOfClicksEachLevel = 0, loseSound, hardness;
    public SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    Random rand = new Random();
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

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
                        x = 4;
                        break;
                    case R.id.blue_ib:
                        x = 3;
                        break;
                }
                if (moves[numberOfClicksEachLevel] != x) { // on wrong click
                    soundPool.play(loseSound, 1, 1, 1, 0, 1f);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Game1Activity.this);
                    alertDialogBuilder.setMessage("GAME OVER, your score was " + currentScore);
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    return true;
                }
                //on success
                playSound(v.getId());
                lightUp(v);
                numberOfClicksEachLevel++;
                TextView tv = findViewById(R.id.current_score_tv);
                TextView textView = findViewById(R.id.high_score_tv);

                if (numItemsInArray == numberOfClicksEachLevel) { //if 4 boxes shown, then activate  function
                    //playGame only after 4 clicks have been made by the user

                    currentScore++;
                    tv.setText("Current score: " + currentScore);

                    numberOfClicksEachLevel = 0;
                    if (numItemsInArray > highScore) {
                        highScore = numItemsInArray;
                        textView.setText("High score: " + highScore);

                    }
                    final Runnable r = new Runnable() {
                        public void run() {
                            playGame();
                        }
                    };
                    handler.postDelayed(r, 2000 - 500 * hardness);
                }
            }
            return true;
        }
    };

    private void playSound(int id) {
        //function that play sound according to sound ID
        int audioRes = 0;
        switch (id) {
            case R.id.green_im:
                audioRes = R.raw.greenbutton;
                break;
            case R.id.red_ib:
                audioRes = R.raw.redbutton;
                break;
            case R.id.yellow_ib:
                audioRes = R.raw.yellowbutton;
                break;
            case R.id.blue_ib:
                audioRes = R.raw.bluebutton;
                break;
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

    public void playGame() {
        addToArray();
        numItemsInArray++;
        for (k = 0; k < numItemsInArray; k++) {
            click(k);
        }
    }

    public void click(final int click_index) {
        //Function that clicks one place randomally on the view
        final Runnable runnable = new Runnable() {
            public void run() {
                if (moves[click_index] == 1) {
                    playSound(R.id.green_im);
                    lightUp(greenButton);
                } else if (moves[click_index] == 2) {
                    playSound(R.id.red_ib);
                    lightUp(redButton);
                } else if (moves[click_index] == 3) {
                    playSound(R.id.yellow_ib);
                    lightUp(yellowButton);
                } else {
                    playSound(R.id.blue_ib);
                    lightUp(blueButton);
                }
            }
        };

        handler.postDelayed(runnable, (2000 - 500 * hardness) * click_index);
    }


    private int random() {
        return rand.nextInt(4) + 1; // generate random number between 1 and 4
    }

    private void addToArray() {  // add random number to the first free position in the array
        for (int i = 0; i < CAPACITY; i++) {
            if (moves[i] == 0) {
                moves[i] = random();
                break;
            }
        }
    }

    private void lightUp(View v) {
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(300);
        mAnimation.setInterpolator(new LinearInterpolator());
        v.startAnimation(mAnimation);
    }

    private void reset() {//reset the game to initial state
        for (int i = 0; i < CAPACITY; i++) {
            moves[i] = 0;
        }
        numberOfClicksEachLevel = 0;
        numItemsInArray = 0;
        currentScore = 0;
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
