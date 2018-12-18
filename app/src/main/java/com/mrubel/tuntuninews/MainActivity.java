package com.mrubel.tuntuninews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    Button b;
    TextView addpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.mylist);
        b = (Button) findViewById(R.id.addingnewnews);
        addpost= (TextView) findViewById(R.id.add_new);
        fetchingData();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addinpost();
                try{
                    TimeUnit.MILLISECONDS.sleep(10);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                fetchingData();

            }
        });

    }

    void addinpost()
    {

        String url = "http://asdzxcfarid.pythonanywhere.com/chat/SaveText/";


        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String > getParams() {
                Map<String, String> parr = new HashMap<String, String>();

                parr.put("text", addpost.getText().toString());
                parr.put("marker","0");
                parr.put("email", "2");

                return parr;

            }

        };



        AppController.getInstance().addToRequestQueue(sq);
        Toast.makeText(getApplicationContext(), "Vua news published Successfully!", Toast.LENGTH_LONG).show();

    }


    void fetchingData(){


        String myURL = "http://asdzxcfarid.pythonanywhere.com/chat/SaveText/?format=json";//"http://fislam.pythonanywhere.com/json/?format=json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(myURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            final String[] news_text = new String[response.length()];
            final String[] news_marker = new String[response.length()];
            final String[] news_email = new String[response.length()];
                String  pk="";
                try {
                    pk =((JSONObject)response.get(response.length()-1)).getString("id");
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }


                for (int i =0; i < response.length(); i++){

                    try {

                        JSONObject jsonObject = (JSONObject) response.get(i);
                        news_text[i] =jsonObject.getString("text")+".."+pk+".. "+jsonObject.getString("marker");
                        news_email[i] = jsonObject.getString("email");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                lv.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.mylistview, R.id.textviewforlist, news_text));

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, Details.class);
                     //   intent.putExtra("title", news_text[position]);
                       // intent.putExtra("code", news_marker[position]);
                        //intent.putExtra("line", news_email[position]);
                        ///startActivity(intent);

                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Log", error);
            }
        });


        com.mrubel.tuntuninews.AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        Toast.makeText(getApplicationContext(), "Data Loaded Successfully!", Toast.LENGTH_SHORT).show();

    }




}
