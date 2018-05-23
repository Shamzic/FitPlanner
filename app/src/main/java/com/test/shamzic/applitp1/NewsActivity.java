package com.test.shamzic.applitp1;

import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }
    private void parseJSON() {
        String url = "https://newsapi.org/v2/everything?domains=eurosport.fr&apiKey=8a288b43e53d4739bb91efa0c4c61491";// eurosport !
        //String url = "https://newsapi.org/v2/everything?q=sport&sortBy=popularity&apiKey=8a288b43e53d4739bb91efa0c4c61491"; // sport dans le monde; trop general
        //String url = "https://newsapi.org/v2/top-headlines?country=fr&category=sport&apiKey=8a288b43e53d4739bb91efa0c4c61491"; // sport en france
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("articles");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject article = jsonArray.getJSONObject(i);
                        String articleTitle = article.getString("title");
                        String articleUrl = article.getString("url");
                        String articleImage = article.getString("urlToImage");

                        mExampleList.add(new ExampleItem(articleImage, articleTitle, articleUrl));
                    }

                    mExampleAdapter = new ExampleAdapter( NewsActivity.this, mExampleList);
                    mRecyclerView.setAdapter(mExampleAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

    }
}
