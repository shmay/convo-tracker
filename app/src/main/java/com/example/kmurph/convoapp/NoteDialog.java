package com.example.kmurph.convoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class NoteDialog extends DialogFragment {
    public static final String TAG = "NoteDialog";
    public static Person person;
    public static Note static_note;

    public static ArrayAdapter<String> adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = (inflater.inflate(R.layout.note_dialog, null));
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
                        String note = (String) input.getText().toString();

                        if (person == null) {
                            h.updateNote(note,static_note);
                            adapter.remove(static_note.getNote());
                            adapter.add(note);
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.add(note);
                            adapter.notifyDataSetChanged();
                            h.addNote(note, person);
                        }

                        person = null;
                    }
                })
                .setNegativeButton(R.string.input_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NoteDialog.this.getDialog().cancel();
                        person = null;
                    }
                });


        Log.d(TAG, "person: " + person);

        if (person == null) {
            EditText input = (EditText) view.findViewById(R.id.input);
            builder.setTitle("Edit Note");
            input.setText(static_note.getNote());
        } else {
            builder.setTitle("Add Note");
        }

        Dialog dialog = builder.create();

        return dialog;
    }
}