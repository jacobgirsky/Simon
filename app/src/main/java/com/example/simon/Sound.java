package com.example.simon;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import java.util.Random;

public class Sound extends Activity {
    private int sound;


    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getSound() {
        return this.sound;
    }

    // adds the sounds to the correct buttons that are being pressed
    public static void makeSound(Context context, int soundID) {
        Sound sound = new Sound();
        if (soundID == R.id.green_im) {
            sound.setSound(R.raw.greenbutton);
        } else if (soundID == R.id.red_ib) {
            sound.setSound(R.raw.redbutton);
        } else if (soundID == R.id.yellow_ib) {
            sound.setSound(R.raw.yellowbutton);
        } else if (soundID == R.id.blue_ib) {
            sound.setSound(R.raw.bluebutton);
        } else if (soundID == R.id.teal_ib) {
        sound.setSound(R.raw.tealbutton);
        } else {
            sound.setSound(R.raw.purplebutton);
        }

        sound.startMediaPlayer(context, sound.getSound());

    }


    public void startMediaPlayer(Context context, int audioRes) {

        MediaPlayer mediaPlayer = MediaPlayer.create(context, audioRes);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }

    public static void lightUp(View v) {
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(300);
        mAnimation.setInterpolator(new LinearInterpolator());
        v.startAnimation(mAnimation);
    }

    public static int random(int random) {
        Random rand = new Random();
        return rand.nextInt(random) + 1; // generate a random number between 1 and 4
    }


}



