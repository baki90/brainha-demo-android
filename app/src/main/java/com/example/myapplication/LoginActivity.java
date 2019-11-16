package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    EditText et_sid;
    EditText et_password;

    Button bt_register;
    Button bt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_sid=(EditText)findViewById(R.id.editText6);
        et_password=(EditText)findViewById(R.id.editText5);
        bt_login = (Button) findViewById(R.id.login_btn);
        bt_register=(Button) findViewById(R.id.button8);

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("");
            }
        });

    }

    public class JSONTask extends AsyncTask<String, String, String> {
        String jwt;

        //parser
        public String parseJSONData(BufferedReader bufferedReader){
            String jwt=null;
            try{
                String json=bufferedReader.readLine();
                JSONObject jsonObject=new JSONObject(json);

                jwt=jsonObject.getString("token");
                return jwt;
            }catch(IOException e) {
                e.printStackTrace();
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
                jsonObject.accumulate("sid", et_sid.getText());
                jsonObject.accumulate("password", et_password.getText());

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL("http://192.168.43.219:3000/users/auth");
                    //URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//보내기모드
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");//서버로 json을 보내겠다
                    con.setRequestProperty("Accept", "application/json");//서버로부터 json을 받겠다
                    //jwt를 보내면서 보낼때 아래줄 주석 풀면됨
                    //con.setRequestProperty("Authorization","Bearer " + jwt);
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
                    jwt=parseJSONData(reader);//json parsing

                    return jwt;

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
            if(result!=null){
                Intent intent=new Intent(getApplicationContext(),menuActivity.class);
                intent.putExtra("jwt",result);
                startActivity(intent);}
        }
    }
}
