package com.example.simon;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SingleChoiceDialog.SingleChoiceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String message = "<html>" +
                "<br><font color=#cc0029><b>About the game</b></font><br><br>" +
                "<font color=#ffffff>Developer: </font><font color=#ffffff>Jacob Girsky </font><br><br>" +
                "<font color=#ffffff>Sounds: </font><font color=#ffffff> All sounds came from freesound.org</font><br><br>" +
                "<font color=#ffffff>Images: </font><font color=#ffffff> All images came from openclipart.org</font><br><br>" +
                "<font color=#ffffff>Links: </font>"+
                " <a href=\"https://www.freesound.org/html/\">freesound.org</a>\"</font>" +
                " <a href=\"https://www.openclipart.org/html/\">openclipart.org</a>\"</font><br><br>" + "</html>";

        AboutListener aboutListener = new AboutListener(message);

        findViewById(R.id.about_button).setOnClickListener(aboutListener);

        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment singleChoiceDialog = new SingleChoiceDialog();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(), "Single Choice Dialog");

            }
        });
        // add a colorful text to simon_textview with html
        TextView simon_tv = findViewById(R.id.simon_textview);
        String text = "<font color=#cc0029>S</font><font color=#ffcc00>I</font>" +
                "<font color=#00B2EE>M</font><font color=#00ff00>O</font>" +
                "<font color=#ffcc00>N</font>";
        simon_tv.setText(Html.fromHtml(text));

    }

    @Override
    public void onPostiveButtonClicked(String[] list, int position) {
        if (position == 0) {
            Intent intent = new Intent(getApplicationContext(), SimonOriginal.class);
            startActivity(intent);
        } else if (position == 1) {
            Intent intent = new Intent(getApplicationContext(), SimonPlus.class);
            startActivity(intent);
        }  else if (position == 2) {
            Intent intent = new Intent(getApplicationContext(), SimonReverse.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

}
