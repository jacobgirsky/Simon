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

public class MainActivity extends AppCompatActivity implements SingleChoiceDialog.SingleChoiceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.about_button).setOnClickListener(new AboutListener());
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

    // adds functionality to the about button
    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<br><font color=#cc0029 size=><b>About the game</b></font><br><br>" +
                    "<font color=#ffcc00><b>Developers: </b></font><font color=#00B2EE> Bushra and Jacob</font><br><br>" +
                    "<font color=#ffcc00><b>Sounds: </b></font><font color=#00B2EE> All sounds came from freesound.org</font><br><br>" +
                    "<font color=#ffcc00><b>Images: </b></font><font color=#00B2EE> All images came from openclipart.org</font><br><br>" +
                    "<font color=#ffcc00><b>Links: </b></font>"+
                    " <a href=\"https://www.freesound.org/html/\">freesound.org</a>\"</font>" +
                    " <a href=\"https://www.openclipart.org/html/\">openclipart.org</a>\"</font><br><br>" + "</html>";


            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("OK" , null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);

            TextView tv = dialog.findViewById(android.R.id.message); // sets html in TV
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}
