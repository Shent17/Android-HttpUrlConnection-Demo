package com.example.httpurlconnectiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://www.wanandroid.com/article/list/0/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_1 = (Button) findViewById(R.id.get_btn);
        Button button_2 = (Button) findViewById(R.id.load_btn);
        TextView textView = (TextView) findViewById(R.id.text_1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //文件字节流存储
//                new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
//                        String str = get();
//                        save(str);
//                    }
//                }.start();

                //SharedPreference存储
                SharedPreferences.Editor editor = getSharedPreferences("data1", MODE_PRIVATE).edit();
                editor.putString("name","Star");
                editor.putInt("age", 22);
                editor.putBoolean("married", false);
                editor.apply();

            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //文件字节流从SD卡中读取
//                String msg = load();
//                textView.setText(msg);

                //从SharedPreference中获取数据
                SharedPreferences pref = getSharedPreferences("data1", MODE_PRIVATE);
                String name = pref.getString("name", "");
                int age = pref.getInt("age", 0);
                boolean married = pref.getBoolean("married", false);
                Log.d("MainActivity", "name is " + name);
                Log.d("MainActivity", "age is " + age);
                Log.d("MainActivity", "married is " + married);
            }
        });

        //Gson gson = new Gson();

    }

    private String get() {
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                byte[] b = new byte[1024];
                int len;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while((len = in.read(b) )> -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();
                Log.d("MainActivityTag", msg);
                return msg;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String str) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(str);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null) {
                content.append(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(reader != null) {
                try{
                    reader.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return content.toString();
    }
}