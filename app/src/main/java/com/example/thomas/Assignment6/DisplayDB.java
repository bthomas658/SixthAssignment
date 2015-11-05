package com.example.thomas.Assignment6;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.thomas.demo5.R;

import org.apache.http.protocol.HTTP;

import java.util.List;

// make this like before where it extends the ListActivity
//like last time but instead of getting from preferences table get it from the database.
//uses cursor and cursor adapter instead of arraylist and array adapter
//would have listview in screen like before.
public class DisplayDB extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_db);
        //instead of array and preferences have the DB stuff here

        PersonDbHelper dbHelper = new PersonDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //
       //out of dataset which columns to use projection

        String[] projection = {
                PersonContract.PersonEntry.COLUMN_NAME_FIRST,
                PersonContract.PersonEntry.COLUMN_NAME_LAST,
                PersonContract.PersonEntry.COLUMN_PHONE,
                PersonContract.PersonEntry.COLUMN_EMAIL
        };

        String[] bind = {
                PersonContract.PersonEntry._ID,
                PersonContract.PersonEntry.COLUMN_NAME_FIRST,
                PersonContract.PersonEntry.COLUMN_NAME_LAST,
                PersonContract.PersonEntry.COLUMN_PHONE,
                PersonContract.PersonEntry.COLUMN_EMAIL
        };

        //now going to call method to return cursor

        Cursor cursor = db.query(PersonContract.PersonEntry.TABLE_NAME, //table to query
                bind,
                null, //columns for where, Null will return all rows
                null, //values for where
                null, //Group By, null is no group by
                null, //Having, null says return all rows
                PersonContract.PersonEntry.COLUMN_NAME_LAST + " ASC" //names in alpabetical order
                );

        //the list items from the layout, will find these in the row_item, should have named them better
        int[] to = new int[]{
                R.id.listItem, R.id.listItem2, R.id.listItem3, R.id.listItem4
        };

        //set up the Cursor Adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row_item, cursor, projection, to, 0);

        //put it to the screen
        this.setListAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_db, menu);
        return true;
    }
    @Override
    protected  void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        //Handle the on-click and display a toast, will do more work here later
        Cursor cursor = (Cursor) l.getItemAtPosition(position);

        //this is returning a cursor this time, so need to get the string out of the cursor
        String[] selectedEmail = {(String) cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_EMAIL))};
        String selectedLastName = (String) cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_LAST));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
        //put out data in the intent
        emailIntent.putExtra(Intent.EXTRA_EMAIL, selectedEmail); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, selectedLastName);
        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(emailIntent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.enterValues) {
            Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
