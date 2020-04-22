package com.example.insertdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String receiveMsg;
    Handler readhandler;

    ReadThread mreadThread;
    ListView listView;

    private String[] nameList;
    private String[] genderList;
    private String[] banList;
    private String[] userInfo;
    private String[] kindergartenList;
    private String[] birtList;
    private String[] teeth1_haveList;
    private String[] teeth2_haveList;
    private String[] teeth3_haveList;
    private String[] teeth4_haveList;

    private EditText name, teeth1_have, teeth2_have, teeth3_have;
    private Button btn_send;
    private Button btn_modify;
    private Button btn_delete;

    boolean clicked=false;
    String flag = null;
    String result;

    Handler mhandler;
    CRUDThread mcrudThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtil.setNetworkPolicy();

        listView = findViewById(R.id.listView);

        readhandler = new Handler();
        mreadThread = new ReadThread();
        mreadThread.start();

        mcrudThread = new CRUDThread();
        mhandler = new Handler();

        name = (EditText)findViewById(R.id.editText0);
        teeth1_have = (EditText)findViewById(R.id.editText1);
        teeth2_have = (EditText)findViewById(R.id.editText2);
        teeth3_have = (EditText)findViewById(R.id.editText3);
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_modify = findViewById(R.id.btn_modify);
        btn_delete = findViewById(R.id.btn_delete);


        mcrudThread.start();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=true;
                flag = "send";
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=true;
                flag = "modify";
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=true;
                flag = "delete";
            }
        });
    }

    private class CRUDThread extends Thread{
        public void run(){
            while (true){
                if(clicked){

                    PHPRequest request = null;
                    try {
                        request = new PHPRequest("http://chamdong.hopto.org/servertask/SQLdata_CRUD.php");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Log.d("dpdpdpdpdp","No!!!!!!!!!!!");
                    }
                    result = request.PhPtest(String.valueOf(name.getText()),String.valueOf(teeth1_have.getText()),String.valueOf(teeth2_have.getText()),String.valueOf(teeth3_have.getText()),flag);

                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplication(),"들어감",Toast.LENGTH_SHORT).show();
                        }
                    });
                    clicked=false;
                }
            }
        }
    }


    class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                String url = "";
                InputStream is = null;
                try {
                    Log.e("main", "running");

                    is = new URL("http://chamdong.hopto.org/servertask/SQLdata_CRUD.php").openStream();

                    if (is != null) {
                        Log.e("main", "not null");
                    } else {
                        Log.e("main", "null");
                    }
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String str;
                    StringBuffer buffer = new StringBuffer();
                    while ((str = rd.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                    readhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText(receiveMsg);
                            jsonRead();
                            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,userInfo);
                            listView.setAdapter(adapter);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void jsonRead() {
        int list_cnt;
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(receiveMsg);
            JSONArray jsonArray = (JSONArray)jsonObject.get("result");
            list_cnt = jsonArray.size();
            Log.e("main", "list_cnt is "+list_cnt);

            nameList = new String[list_cnt];
            banList = new String[list_cnt];
            genderList = new String[list_cnt];
            userInfo = new String[list_cnt];
            kindergartenList = new String[list_cnt];
            birtList = new String[list_cnt];
            teeth1_haveList = new String[list_cnt];
            teeth2_haveList = new String[list_cnt];
            teeth3_haveList = new String[list_cnt];
            teeth4_haveList = new String[list_cnt];

            for (int i = 0; i < list_cnt; i++) {
                JSONObject tmp = (JSONObject)jsonArray.get(i);
                nameList[i] = (String)tmp.get("name");
                kindergartenList[i]=(String)tmp.get("kindergarten");
                birtList[i]=(String)tmp.get("bitrh");
                genderList[i] = (String)tmp.get("gender");
                banList[i] = (String)tmp.get("class");
                teeth1_haveList[i]=(String)tmp.get("teeth1_have");
                teeth2_haveList[i]=(String)tmp.get("teeth2_have");
                teeth3_haveList[i]=(String)tmp.get("teeth3_have");
                teeth4_haveList[i]=(String)tmp.get("teeth4_have");

                userInfo[i] = (String)tmp.get("name") + ' ' + (String)tmp.get("kindergarten") + ' ' +
                        (String)tmp.get("class")+ ' ' + (String)tmp.get("teeth1_have")+ ' ' + (String)tmp.get("teeth2_have")+ ' ' +
                        (String)tmp.get("teeth3_have");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
