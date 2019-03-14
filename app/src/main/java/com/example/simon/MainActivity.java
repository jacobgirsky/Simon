package com.example.simon;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
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
                //singleChoiceDialog.getDialog().getWindow().setBackgroundDrawableResource(new ColorDrawable(Color.TRANSPARENT));

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
            Intent intent = new Intent(getApplicationContext(), Game1Activity.class);
            startActivity(intent);
        } else if (position == 1) {
            Intent intent = new Intent(getApplicationContext(), Game2Activity.class);
            startActivity(intent);
        }  else if (position == 2) {
            Intent intent = new Intent(getApplicationContext(), Game3Activity.class);
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
                    "<font color=#ffcc00><b>Song: </b></font><font color=#00B2EE> TBD</font><br><br>" +
                    "<font color=#ffcc00><b>Creator: </b></font><font color=#00B2EE> TBD</font><br><br>" +
                    "<font color=#ffcc00><b>Link: </b></font><font color=#00B2EE>"+
                    "   https://opengameart.org/content/caketown-cuteplayful\"</font><br><br>"+
                    "<font color=#ffcc00><b>License: </b></font><font color=#00B2EE> CC-BY 3.0</font>" +
                    "</html>";


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
