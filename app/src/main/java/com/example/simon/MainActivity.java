package com.example.simon;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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


    }

    @Override
    public void onPostiveButtonClicked(String[] list, int position) {
        Intent intent = new Intent(getApplicationContext(), Game1Activity.class);
        startActivity(intent);
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    // adds functionality to the about button
    class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html>" +
                    "<h2>About the game</h2>" +
                    "<p>The creators are Bushra and Jacob</p>" +
                    "<p><b>Song</b> TBD</p>" +
                    "<p><b>Creator:</b> TBD</p>" +
                    "<p><b>Link</b></p> " +
                    "<p><https://opengameart.org/content/caketown-cuteplayful</p>"+
                    "<p><b>License</b> CC-BY 3.0</p>" +
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
