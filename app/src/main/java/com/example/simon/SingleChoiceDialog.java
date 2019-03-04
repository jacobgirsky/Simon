package com.example.simon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class SingleChoiceDialog extends DialogFragment {

    int postion = 0;

    public interface SingleChoiceListener {
        void onPostiveButtonClicked(String[] list, int position);
        void onNegativeButtonClicked();
    }

    SingleChoiceListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SingleChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + " SingleChoiceListener must implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = getActivity().getResources().getStringArray(R.array.choice_items);

        builder.setTitle("Select the game you want to play").setSingleChoiceItems(list, postion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postion = i;
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onPostiveButtonClicked(list, postion);

            }
        }). setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onNegativeButtonClicked();
            }
        });
        return builder.create();
    }
}
