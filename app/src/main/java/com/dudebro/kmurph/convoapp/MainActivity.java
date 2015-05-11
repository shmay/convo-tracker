package com.dudebro.kmurph.convoapp;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {
    public final static String NAME = "com.dudebro.kmurph.NAME";
    public static final String TAG = "MainActivity";

    final Context context = this;
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        final DbHandler h = new DbHandler(context);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, h.getAll());
        InputDialog.adapter = adapter;

        listView.setLongClickable(true);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                final String p = (String) adapter.getItem(pos);
                Person person = h.findPerson(p);

                if (h.noteCountForPerson(person) > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("Are you sure you'd like to destroy " + p + " and all of its associated notes?")
                            .setTitle("Ya sure?").setPositiveButton("yah", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            adapter.remove(p);
                            h.deletePerson(p);
                            adapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("nah", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                } else {
                    adapter.remove(p);
                    h.deletePerson(p);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
        });

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                DisplayPersonActivity.person = h.findPerson(itemValue);
                Intent intent = new Intent(context, DisplayPersonActivity.class);
                intent.putExtra(NAME, itemValue);
                startActivity(intent);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void clickButton(View view) {
        // Do something in response to button
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_plus) {
            InputDialog.person = null;
            InputDialog dialog = new InputDialog();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "dialog");
        }

        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Use ConvoTracker to remember questions / thoughts / ideas / whateva that you'd like to present to people")
                    .setTitle("ab00t").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
