package com.example.deeshaiesc.simpletodo;

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
