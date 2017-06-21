package com.example.deeshaiesc.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;

    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView LVitems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        LVitems = (ListView) findViewById(R.id.LVitems);
        LVitems.setAdapter(itemsAdapter);

        // mock data
        //items.add("First Item");
        //items.add("Second Item");

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText ETnewitem = (EditText) findViewById(R.id.ETnewitem);
        String itemText = ETnewitem.getText().toString();
        itemsAdapter.add(itemText);
        ETnewitem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener()
    {
        Log.i("MainActivity", "Setting up Listener on list view");

        LVitems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i("MainActivity", "Item removed from list:" + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // set up items listener for edit (regular click)
        LVitems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                // pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                // display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    //handle results from
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE)
        {
            //extract updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            //extract original position of the edited item
            int position = data.getExtras().getInt(ITEM_POSITION);

            //update the model with the new item text at the edited position
            items.set(position, updatedItem);

            //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();

            //persist the change model
            Toast.makeText(this, "Item updated succesfuly", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile()
    {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems()
    {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e)
        {
            Log.e("Main Activity", "Error reading file", e);
            items = new ArrayList<>();
        }

    }

    private void writeItems()
    {
        try
        {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e)
        {
            Log.e("Main Activity", "Error writing file", e);
        }

    }
}
