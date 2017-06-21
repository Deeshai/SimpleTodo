package com.example.deeshaiesc.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.deeshaiesc.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.deeshaiesc.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    //track edit text
    EditText ETitemtext;

    //position of edited item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //resolve edit text from layout
        ETitemtext = (EditText) findViewById(R.id.ETitemtext);

        //set edit text value from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        //update the title bar of the activity
        getSupportActionBar().setTitle("Edit Item");

    }

    //handler for save button
    public void onSaveItem(View v)
    {
        //prepare new intent for result
        Intent i = new Intent();

        //pass updated item text as extra
        i.putExtra(ITEM_TEXT, ETitemtext.getText().toString());

        //pass original postion as extra
        i.putExtra(ITEM_POSITION, position);

        //set the intnt as the result of the activity
        setResult(RESULT_OK, i);

        //close the activity and redirect to main
        finish();
    }
}
