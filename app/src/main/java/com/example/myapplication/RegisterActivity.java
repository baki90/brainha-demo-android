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

public class RegisterActivity extends AppCompatActivity {

    EditText et_sid;
    EditText et_password;
    EditText et_name;
    EditText et_email;
    EditText et_department;
    Button bt_register;

    public boolean check_null(){
        if(et_sid.getText()==null) return false;
        if(et_password.getText()==null) return false;
        if(et_name.getText()==null) return false;
        if(et_department.getText()==null) return false;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        et_sid=(EditText)findViewById(R.id.register_id_input);
        et_password=(EditText)findViewById(R.id.register_password_input);
        et_name=(EditText)findViewById(R.id.register_name_input);
        et_email=(EditText)findViewById(R.id.register_email_input);
        et_department=(EditText)findViewById(R.id.register_department_input);

        bt_register=(Button)findViewById(R.id.button_register);

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_null()) new JSONTask().execute("");
                else Toast.makeText(getApplicationContext(), "필수 내용을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        String jwt;

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
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sid", Integer.parseInt(et_sid.getText().toString()));
                jsonObject.accumulate("password", et_password.getText());
                jsonObject.accumulate("name", et_name.getText());
                jsonObject.accumulate("email", et_email.getText());
                jsonObject.accumulate("departmentId",1);//test

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL("http://192.168.43.219:3000/users");
                    //URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "application/json");
                    //con.setRequestProperty("Authorization","Bearer " + jwt);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    jwt=parseJSONData(reader);

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
                            Toast.makeText(getApplicationContext(),
                                    "등록되었습니다.",Toast.LENGTH_LONG).show();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);//result==jwt
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
    }
}