package com.dudebro.kmurph.convoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class InputDialog extends DialogFragment {
    public static final String TAG = "InputDialog";
    public static Person person;
    public static DisplayPersonActivity displayPersonActivity;

    public static ArrayAdapter<String> adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = (inflater.inflate(R.layout.input_dialog, null));
        final Context context = getActivity();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.input_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText input = (EditText) view.findViewById(R.id.input);
                        DbHandler h = new DbHandler(context);
                        String name = (String) input.getText().toString();

                        if (person == null) {
                            h.addPerson(name);
                            adapter.add(name);
                            adapter.notifyDataSetChanged();
                        } else {
                            h.updateName(name, person);
                            displayPersonActivity.setTitle("notes for " + name);
                        }

                        person = null;
                    }
                })
                .setNegativeButton(R.string.input_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InputDialog.this.getDialog().cancel();
                        person = null;
                    }
                });

        if (person == null) {
            builder.setTitle("Add Person");
        } else {
            EditText input = (EditText) view.findViewById(R.id.input);
            builder.setTitle("Edit Name");
            input.setText(person.getName());
        }

        Dialog dialog = builder.create();

        return dialog;
    }
}