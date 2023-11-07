package com.example.harveytest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

ListView list;
TextView selected;
Button toAdd;
Button delete;
Button updateButton;

LinkedList<String> funkos;
Cursor mCursor;
String after = "";



View.OnClickListener toInsert = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddPopPage.class);
        startActivity(intent);

    }
};

View.OnClickListener updateL = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        updateList();
    }
};

    AdapterView.OnItemClickListener listSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selected.setText("Selected Item: " + funkos.get(i));
            //selectedTV.setText(" "+ i);

            //parse string for the national number, assign to a string
            String before = funkos.get(i);
            after = "";
            for (int d = 0; d < before.length(); d++) {
                char currentChar = before.charAt(d);
                if (currentChar == ',') {
                    break; // Exit the loop
                }
                // Do something with the current character
                after = after + currentChar;
            }

        }
    };

    View.OnClickListener del = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String mSelectionClause = FunkoProvider.COL_ID + " = ? ";

            String[] mSelectionArgs = {after.toString()};

            int mRowsDeleted = getContentResolver().delete(FunkoProvider.contentURI, mSelectionClause,
                    mSelectionArgs);

            updateList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.dbList);
        selected = findViewById(R.id.selectedTV);
        toAdd = findViewById(R.id.toInsert);
        delete = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);

        toAdd.setOnClickListener(toInsert);
        list.setOnItemClickListener(listSelect);
        updateButton.setOnClickListener(updateL);
        delete.setOnClickListener(del);


        updateList();

    }

    public void updateList(){
        mCursor = getContentResolver().query(
                FunkoProvider.contentURI, null, null,
                null, null);
        funkos = new LinkedList<>();
        if (mCursor != null) {
            mCursor.moveToFirst();
            if (mCursor.getCount() > 0) {
                while(mCursor.isAfterLast() == false) {
                    String idNum = Integer.toString(mCursor.getInt(1));
                    String name = mCursor.getString(2);
                    String number = Integer.toString(mCursor.getInt(3));;
                    String type = mCursor.getString(4);
                    String fandom = mCursor.getString(5);

                    String onOrOff = "";
                    int on = mCursor.getInt(6);
                    if(on == 1){
                        onOrOff = "On";
                    }else{
                        onOrOff = "Off";
                    }

                    String ult = mCursor.getString(7);
                    String price = Double.toString(mCursor.getDouble(8));;



                    funkos.add(new String(idNum + ", " + name + ", " + number + ", " +
                            type + ", " + fandom + ", " + onOrOff + ", " + ult + ", " + price + "."));
                    mCursor.moveToNext();
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, funkos);
        list.setAdapter(adapter);
    }
}