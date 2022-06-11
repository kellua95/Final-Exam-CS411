package com.example.mytodolistapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private static final String SHARD_PREF = "shared_pref";
    public Boolean VALUE = true;

    MyDatabaseHelper myDB;
    ArrayList<String> id, activity_title, checked;
    CustomAdapter customAdapter;

    RadioGroup radioGroup;
    RadioButton RBblue, RBblack;

    TextView TVColor;
    RecyclerView recyclerView;
    EditText name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        recyclerView = findViewById(R.id.recyclerView);
        radioGroup = findViewById(R.id.radio_group);
        RBblue = findViewById(R.id.RbGreen);
        RBblack = findViewById(R.id.Rbblack);
        TVColor = findViewById(R.id.textViewColor);
        name = findViewById(R.id.editTextTextName);
        LoadData();

        myDB = new MyDatabaseHelper(SecondActivity.this);
        id = new ArrayList<>();
        activity_title = new ArrayList<>();
        checked = new ArrayList<>();
        try {
        storeDataInArrays();
            customAdapter = new CustomAdapter(SecondActivity.this, this, id, activity_title, checked);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        if (!VALUE){
            TVColor.setTextColor(getResources().getColor(R.color.Blue));
            RBblue.setChecked(true);
        }else {
            TVColor.setTextColor(getResources().getColor(R.color.black));
            RBblack.setChecked(true);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_selected = item.getItemId();
        if (item_selected == R.id.Logout){
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
        }
        return true;
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                activity_title.add(cursor.getString(1));
                checked.add(cursor.getString(2));
            }
        }
    }

    private void LoadData() {
        SharedPreferences sh = getSharedPreferences(SHARD_PREF, MODE_PRIVATE);
        VALUE = sh.getBoolean("color", true);


    }

    public void ChangeColor(View view){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARD_PREF, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        int selectedId = radioGroup.getCheckedRadioButtonId();

        if(selectedId == R.id.Rbblack){
            myEdit.putBoolean("color", true);
            TVColor.setTextColor(getResources().getColor(R.color.black));
        }else {
            myEdit.putBoolean("color", false);
            TVColor.setTextColor(getResources().getColor(R.color.Blue));
        }
        myEdit.apply();
        Toast.makeText(this, "The color Change successfully", Toast.LENGTH_SHORT).show();
    }
    public void Add(View view){
        MyDatabaseHelper myDB = new MyDatabaseHelper(SecondActivity.this);
        myDB.addActivity(name.getText().toString().trim(), "false");
        try {
            storeDataInArrays();
            customAdapter = new CustomAdapter(SecondActivity.this, this, id, activity_title, checked);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
