package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class boardActivity extends AppCompatActivity {
    String jwt;
    String jsonData;
    List<menuList> menulist;
    ListAdapter2 listadapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent;
        intent = getIntent();
        try {
            jwt = intent.getExtras().getString("jwt");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("jwt", jwt);
                startActivity(intent);
            }
        });

        menulist = new LinkedList<>();
        listView=(ListView)findViewById(R.id.post_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), postActivity.class);
                startActivity(intent);
            }
        });

        new boardActivity.JSONTask().execute("");

    }


    public class JSONTask extends AsyncTask<String, String, String> {

        //parser
        public String parseJSONData(BufferedReader bufferedReader) {
            try {
                String json = bufferedReader.readLine();
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("http://192.168.43.219:3000/boards/1/posts");
                    //URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");//보내기모드
                    con.setRequestProperty("Cache-Control", "no-cache");
                    //con.setRequestProperty("Content-Type", "application/json");//서버로 json을 보내겠다
                    con.setRequestProperty("Accept", "application/json");//서버로부터 json을 받겠다
                    con.setDoOutput(false);//받겠다
                    con.setDoInput(true);//보내겠다
                    con.connect();


                    //받아온 json읽어서 return
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    jsonData=sb.toString();

                    return jsonData;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
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

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            jsonParsing(result);
        }
    }


    private void jsonParsing(String json) {
        try {
            //json array name
            JSONArray menuArray = new JSONArray(json);

            for (int i = 0; i < menuArray.length(); i++) {
                JSONObject menuObject = menuArray.getJSONObject(i);
                System.out.println("title:"+menuObject.getString("title")+"contents"+menuObject.getString("content"));
                menuList menu = new menuList("익명", menuObject.getString("title"), menuObject.getString("content"), " ");
                menulist.add(menu);
            }
            listadapter = new ListAdapter2(this, menulist);
            listView.setAdapter(listadapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
