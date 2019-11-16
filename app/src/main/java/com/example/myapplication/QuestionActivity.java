package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuestionActivity extends AppCompatActivity {

    String jwt;
    //{title,content,tags}

    EditText et_title;
    EditText et_content;
    //tags?
    Button bt_qregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent;
        intent=getIntent();
        try {jwt=intent.getExtras().getString("jwt");}
        catch(NullPointerException e){
            e.printStackTrace();
        }
        et_title=(EditText)findViewById(R.id.et_title);
        et_content=(EditText)findViewById(R.id.et_contents);
        bt_qregister=(Button)findViewById(R.id.quest_update);

        bt_qregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("");
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        //String jwt;

        //parser 필요가없음
        public String[] parseJSONData(String input){
            String[] result=new String[3];
            try{
                JSONObject jsonObject=new JSONObject(input);

                result[0]=jsonObject.getString("title");
                result[1]=jsonObject.getString("content");
                //result[2]=jsonObject.getString("tags");
                return result;
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject에 보낼 내용을 담기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("title", et_title.getText());
                jsonObject.accumulate("content", et_content.getText());
                //tag

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL("http://192.168.43.219:3000/boards/1/posts");
                    //URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//보내기모드
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");//서버로 json을 보내겠다
                    con.setRequestProperty("Accept", "application/json");//서버로부터 json을 받겠다
                    con.setRequestProperty("Authorization","Bearer " + jwt);
                    con.setDoOutput(true);//받겠다
                    con.setDoInput(true);//보내겠다
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();
                    //받아온 json읽어서 return
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String json=reader.readLine();

                    return json;

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //doinbackground 위에 함수의 return을 result로 받아서 처리하는 함수
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);//result==jwt
            Intent intent=new Intent(getApplicationContext(),boardActivity.class);
            intent.putExtra("jwt",jwt);
            startActivity(intent);
        }
    }
}


