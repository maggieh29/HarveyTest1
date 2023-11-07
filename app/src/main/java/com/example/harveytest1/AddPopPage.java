package com.example.harveytest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddPopPage extends AppCompatActivity {

    TextView onOffTV;
    EditText idNum;
    EditText name;
    EditText num;
    EditText type;
    EditText fandom;
    EditText ultimate;
    EditText price;
    RadioGroup group;
    RadioButton on;
    RadioButton off;


    Button addToDB;

    int idInput;
    int numberInput;
    double priceInput;

    String nameInput;
    String typeInput;
    String fanInput;
    String ultInput;
    Boolean onOrOff;

    View.OnClickListener addingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(group.getCheckedRadioButtonId() == -1){
                Toast.makeText(getApplicationContext(), "Please select on or off", Toast.LENGTH_LONG).show();
            }else{
                addFunko();
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pop_page);

        idNum = findViewById(R.id.idTV);
        name = findViewById(R.id.nameTV);
        num = findViewById(R.id.numTV);
        type = findViewById(R.id.typeTV);
        fandom = findViewById(R.id.fanTV);
        ultimate = findViewById(R.id.ultTV);
        price = findViewById(R.id.priceTV);
        group = findViewById(R.id.rGroup);
        on = findViewById(R.id.onButton);
        off = findViewById(R.id.offButton);


        addToDB = findViewById(R.id.submitButton);
        addToDB.setOnClickListener(addingListener);



    }

    public void getValues(){
      nameInput = name.getText().toString();
       typeInput = type.getText().toString();
       fanInput = fandom.getText().toString();
       ultInput = ultimate.getText().toString();
        onOrOff = getOnOff();

        try {
            idInput = Integer.parseInt(idNum.getText().toString());
            numberInput = Integer.parseInt(num.getText().toString());
            priceInput = Double.parseDouble(price.getText().toString());
        } catch (NumberFormatException e) {
            //if a number field is empty the parseInt method does not work without throwing an error
            //needed a try/catch statement
            Toast.makeText(getApplicationContext(), "Invalid numeric input", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void addFunko(){

        getValues();


        if (nameInput.isEmpty() || typeInput.isEmpty() || fanInput.isEmpty() || ultInput.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
        } else {

            if(doesIdExist(idInput) == true){
                updateEntry(idInput);
            }else {
                ContentValues mNewValues = new ContentValues();

                mNewValues.put(FunkoProvider.COL_ID, idInput);
                mNewValues.put(FunkoProvider.COL_POPNAME, nameInput);
                mNewValues.put(FunkoProvider.COL_POPNUMBER, numberInput);
                mNewValues.put(FunkoProvider.COL_TYPE, typeInput);
                mNewValues.put(FunkoProvider.COL_FANDOM, fanInput);
                mNewValues.put(FunkoProvider.COL_ON, onOrOff);
                mNewValues.put(FunkoProvider.COL_ULTIMATE, ultInput);
                mNewValues.put(FunkoProvider.COL_PRICE, priceInput);


                getContentResolver().insert(FunkoProvider.contentURI, mNewValues);
                Toast.makeText(getApplicationContext(), "Added to database", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean getOnOff(){
            if(on.isChecked()){
                return true;
            }else{
                return false;
            }
    }

    public boolean doesIdExist(int idNumber){
        String[] projection = {FunkoProvider.COL_ID};
        String selection = FunkoProvider.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(idNumber)};

        Cursor cursor = getContentResolver().query(
                FunkoProvider.contentURI, projection, selection, selectionArgs, null);

        //if the cursor finds a match and has more than 0 instances of the id number, the
        //boolean is true, if not the boolean is false
        boolean exists = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

    public void updateEntry(int existingID){
        String mSelectionClause = FunkoProvider.COL_ID + " = ? ";

        String a = String.valueOf(existingID);
        String[] mSelectionArgs = {a.toString()};

        int mRowsDeleted = getContentResolver().delete(FunkoProvider.contentURI, mSelectionClause,
                mSelectionArgs);

        Toast.makeText(getApplicationContext(), "Entry already exists under ID, updated other information for the entry", Toast.LENGTH_LONG).show();

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(FunkoProvider.COL_ID, idInput);
        mNewValues.put(FunkoProvider.COL_POPNAME, nameInput);
        mNewValues.put(FunkoProvider.COL_POPNUMBER, numberInput);
        mNewValues.put(FunkoProvider.COL_TYPE, typeInput);
        mNewValues.put(FunkoProvider.COL_FANDOM, fanInput);
        mNewValues.put(FunkoProvider.COL_ON, onOrOff);
        mNewValues.put(FunkoProvider.COL_ULTIMATE, ultInput);
        mNewValues.put(FunkoProvider.COL_PRICE, priceInput);


        getContentResolver().insert(FunkoProvider.contentURI, mNewValues);
        Toast.makeText(getApplicationContext(), "Added to database", Toast.LENGTH_LONG).show();

    }
}