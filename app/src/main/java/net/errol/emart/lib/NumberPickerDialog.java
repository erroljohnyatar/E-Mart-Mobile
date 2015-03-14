package net.errol.emart.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import net.errol.emart.R;

/**
 * Created by JasmineGayle on 3/12/2015.
 */
public class NumberPickerDialog extends DialogFragment {

    private Activity ACTIVITY = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ACTIVITY = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = (LayoutInflater)
                ACTIVITY.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.number_picker_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.quantity)
            .setView(npView)
            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            })
            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}