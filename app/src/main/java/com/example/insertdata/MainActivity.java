package com.example.insertdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    private EditText index,data1, data2, data3;
    private Button btn_send;
    private Button btn_modify;
    private Button btn_delete;

    boolean clicked=false;
    String flag = null;
    String result;

    Handler mhandler;
    connectThread connectThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtil.setNetworkPolicy();
        connectThread = new connectThread();
        mhandler = new Handler();

        index = (EditText)findViewById(R.id.editText0);
        data1 = (EditText)findViewById(R.id.editText1);
        data2 = (EditText)findViewById(R.id.editText2);
        data3 = (EditText)findViewById(R.id.editText3);
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_modify = findViewById(R.id.btn_modify);
        btn_delete = findViewById(R.id.btn_delete);


        connectThread.start();
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

    private class connectThread extends Thread{
        public void run(){
            while (true){
                if(clicked){

                    PHPRequest request = null;
                    try {
                        request = new PHPRequest("http://chamdong.hopto.org/test/Data_insert.php");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Log.d("dpdpdpdpdp","No!!!!!!!!!!!");
                    }
                    result = request.PhPtest(String.valueOf(index.getText()),String.valueOf(data1.getText()),String.valueOf(data2.getText()),String.valueOf(data3.getText()),flag);

                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(result.equals("1")){
                                Toast.makeText(getApplication(),"들어감",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplication(),"안 들어감",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    clicked=false;
                }
            }
        }
    }
}
