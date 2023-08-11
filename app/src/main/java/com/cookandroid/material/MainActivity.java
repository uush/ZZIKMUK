package com.cookandroid.material;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.cookandroid.material.FoodMaterialDB.MainDao;
import com.cookandroid.material.FoodMaterialDB.MainData;
import com.cookandroid.material.FoodMaterialDB.RoomDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editText;
    ImageView ImgAdd;
    TextView TextGroup;
    Switch matOnOff;
    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    MainAdapter adapter;

    private List<MainData> onList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.search_view);
        ImgAdd = findViewById(R.id.material_add);
        TextGroup = findViewById(R.id.Group);
        matOnOff = findViewById(R.id.mat_onoff);
        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(this);

        dataList = database.mainDao().getAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MainAdapter(MainActivity.this, dataList);

        recyclerView.setAdapter(adapter);

        MainData data = new MainData();

//        onList.addAll(database.mainDao().getOn());      //이게 맞나
//
//        OnList onList1 = new OnList();
//        onList.addAll(database.mainDao().getOn());

        //authorNameTextView.setText(book.getAuthor().getName());

        ImgAdd.setOnClickListener(new View.OnClickListener() {   //사용자가 원하는 성분 등록
            @Override
            public void onClick(View v) {
                String sText = editText.getText().toString().trim();
                if (!sText.equals("")) {
                    data.setmName(sText);                       //등록할 성분이름 설정
                    data.setsOnOff(false);                      //등록할 성분 스위치 off 상태로 설정
                    database.mainDao().insert(data);            //위에 설정한 내용 바탕으로 데이터베이스에 insert 수행

                    editText.setText("");

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        TextGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}