package com.example.simon;

import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class AboutListener implements View.OnClickListener {

    private String message;

    AboutListener(String message) {
        this.message = message;
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView tv = dialog.findViewById(android.R.id.message); // sets html in TV
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
