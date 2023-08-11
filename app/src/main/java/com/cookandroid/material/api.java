package com.cookandroid.material;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cookandroid.material.FoodMaterialDB.MainData;
import com.cookandroid.material.FoodMaterialDB.RoomDB;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class api extends AppCompatActivity{
    private RoomDB database;
    private List<MainData> dataList;
    private List<String> onList;

    EditText edit, edit1;
    TextView text;
    XmlPullParser xpp;


    String key = "be78fb42745649858710";
    //    String clientSecret = "C9ee7O6EuI";
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api);

        edit= (EditText)findViewById(R.id.edit);
        edit1= (EditText)findViewById(R.id.edit1);
        text= (TextView)findViewById(R.id.result);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getOn();
        text.append("\n등록된 기피 성분 : ");
        for(int i = 0; i < dataList.size(); i++){
            text.append(dataList.get(i).getmName()+" ");
        }

        Button cameraBtn=(Button) findViewById(R.id.camera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
        Bundle intent = getIntent().getExtras();
        if(intent != null){
            edit1.setText(intent.getString("TEXT"));
        }
        else
            return;

    }
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data= String.valueOf(getXmlData());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    public void mOnClick1(View v){
        switch (v.getId()){
            case R.id.button1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data=getXmlData1();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }
    public class Item {
        public String name = "";
        public String[] foods;

    }

    public class ReportNum {
        public String rNum = "";

        public String getrNum() {
            return rNum;
        }

        public void setrNum(String rNum) {
            this.rNum = rNum;
        }
    }


    ReportNum rNum = new ReportNum();
    String getXmlData(){
        ArrayList<Item> items = new ArrayList<Item>();      //배열이나 ArrayList로 구현
        StringBuffer buffer=new StringBuffer();
        String str = edit.getText().toString();//EditText에 작성된 Text얻어오기
        String name = "";
        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";


        String queryUrl="http://openapi.foodsafetykorea.go.kr/api/be78fb42745649858710/C002/xml/1/8/PRDLST_NM="+str;
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();


            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        Item item = new Item();
                        tag= xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("PRDLST_NM")) {
                            buffer.append("상품명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("BSSH_NM")) {
                            buffer.append("업소명 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if (tag.equals("RAWMTRL_NM")) {
                            buffer.append("원재료명 :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n\n");//줄바꿈 문자 추가
                            name = xpp.getText();
                            String[] names = name.split(",");
                            item.foods = names;

                            ArrayList<String> data = new ArrayList<String>();
                            for(int i = 0; i < dataList.size(); i++){
                                data.add(dataList.get(i).getmName());
                            }

                            buffer.append("포함된 기피 성분 : ");

                            int cnt = 0;
                            for(String a : names) {
                                boolean isEquals = false;

                                for(String b : data) {
                                    if(a.contains(b)) isEquals = true;
                                }
                                if(isEquals) {
                                    cnt++;
                                    buffer.append(a +" ");
                                    SpannableString string = new SpannableString(a);
                                    string.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, a.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            buffer.append("\n포함된 기피 성분 개수 : " + cnt + "개");
                            buffer.append("\n\n");
                            buffer.append("--------------------------------------------------------------------\n\n");
                        }
                        items.add(item);

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n\n\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getXmlData1(){

        StringBuffer buffer=new StringBuffer();
        String str= edit1.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        String queryUrl="http://openapi.foodsafetykorea.go.kr/api/be78fb42745649858710/C005/xml/1/1/BAR_CD="+str;
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();


            xpp.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("PRDLST_REPORT_NO")) {
                            xpp.next();
                            rNum.setrNum(xpp.getText());
                            buffer.append(getXmlData2(rNum.getrNum()));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getXmlData2(String str){
        ArrayList<Item> items = new ArrayList<Item>();      //배열이나 ArrayList로 구현
        StringBuffer buffer=new StringBuffer();
        //String str = "";//EditText에 작성된 Text얻어오기
        //String str = rNum.getrNum();//EditText에 작성된 Text얻어오기
        String name = "";
        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";


        String queryUrl="http://openapi.foodsafetykorea.go.kr/api/be78fb42745649858710/C002/xml/1/1/PRDLST_REPORT_NO="+str;
        //+rNum.getrNum();
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();


            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        Item item = new Item();
                        tag= xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("PRDLST_NM")) {
                            buffer.append("상품명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("BSSH_NM")) {
                            buffer.append("업소명 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if (tag.equals("RAWMTRL_NM")) {
                            buffer.append("원재료명 :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n\n");//줄바꿈 문자 추가
                            name = xpp.getText();
                            String[] names = name.split(",");
                            item.foods = names;

                            ArrayList<String> data = new ArrayList<String>();
                            for(int i = 0; i < dataList.size(); i++){
                                data.add(dataList.get(i).getmName());
                            }

                            buffer.append("포함된 기피 성분 : ");

                            int cnt = 0;
                            for(String a : names) {
                                boolean isEquals = false;

                                for(String b : data) {
                                    if(a.contains(b)) isEquals = true;
                                }
                                if(isEquals) {
                                    cnt++;
                                    buffer.append(a +" ");
                                    SpannableString string = new SpannableString(a);
                                    string.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, a.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            buffer.append("\n포함된 기피 성분 개수 : " + cnt + "개");
                            buffer.append("\n\n");
                            buffer.append("--------------------------------------------------------------------\n\n");
                        }
                        items.add(item);

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n\n\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....


}