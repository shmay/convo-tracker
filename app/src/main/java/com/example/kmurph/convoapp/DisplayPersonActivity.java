package com.example.kmurph.convoapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DisplayPersonActivity extends ActionBarActivity {
    final Context context = this;
    ListView listView;
    public static final String TAG = "DisplayPersonActivity";
    public static Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_person);

        Intent intent = getIntent();

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        final DbHandler h = new DbHandler(context);

        setTitle("notes for " + person.getName());

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, h.notesForPerson(person));
        NoteDialog.adapter = adapter;

        listView.setLongClickable(true);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                Log.d(TAG, "long click");
                Log.d(TAG, "id: " + id);
                Note note = h.getNoteFromPos(pos, person);
                adapter.remove(note.getNote());
                h.deleteNote(note);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = h.getNoteFromPos(position, person);

                NoteDialog.person = null;
                NoteDialog.static_note = note;
                NoteDialog dialog = new NoteDialog();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "dialog");
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_display_person, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.i("yar", "val: " + id);

        if (id == R.id.note_plus) {
            NoteDialog dialog = new NoteDialog();
            NoteDialog.person = DisplayPersonActivity.person;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "dialog");
        }

        if (id == R.id.note_edit) {
            InputDialog.person = person;
            InputDialog.displayPersonActivity = this;
            InputDialog dialog = new InputDialog();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "dialog");
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
