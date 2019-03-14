package com.example.simon;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

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
        }

        sound.startMediaPlayer(context, sound.getSound());

    }


    public void startMediaPlayer(Context context, int audioRes) {

        MediaPlayer mediaPlayer = (MediaPlayer) MediaPlayer.create(context, audioRes);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}

