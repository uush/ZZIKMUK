package com.cookandroid.zzikmuk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.zzikmuk.FoodMaterialDB.MainData;
import com.cookandroid.zzikmuk.FoodMaterialDB.RoomDB;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button btAdd;
    Switch matOnOff;
    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        btAdd = findViewById(R.id.bt_add);
        matOnOff = findViewById(R.id.mat_onoff);
        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(this);

        dataList = database.mainDao().getAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MainAdapter(MainActivity.this, dataList);

        recyclerView.setAdapter(adapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sText = editText.getText().toString().trim();
                if (!sText.equals("")) {
                    MainData data = new MainData();
                    data.setmName(sText);
                    data.setsOnOff(false);
                    database.mainDao().insert(data);

                    editText.setText("");

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}