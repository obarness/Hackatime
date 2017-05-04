package com.example.hacktime.pocketwords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private String jsonStr;
    HashMap<String, String> words = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //example of how to pull data from server
        //result will hold a string of the json data
        getHttpQuery("http://172.22.6.60/getRoots",new VolleyCallback(){
           @Override
            public void onSuccess(String result){
               jsonStr = new String(result);
               jsonStringToArrays(jsonStr);
               initViews();
            }
        });


    }
    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<WordCard> wordcards = prepareData();
        WordGrid adapter = new WordGrid(getApplicationContext(),wordcards);
        recyclerView.setAdapter(adapter);

    }
    private ArrayList<WordCard> prepareData(){
        ArrayList<WordCard> word_cards = new ArrayList<>();
        Set set = words.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            WordCard wordcard = new WordCard();
            wordcard.setWord_text(mentry.getKey().toString());
            wordcard.setWord_image_url(mentry.getValue().toString());
            word_cards.add(wordcard);
        }
        return word_cards;
    }

    private void getHttpQuery(String url,final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onSuccess(error.getMessage());
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }

    private void jsonStringToArrays(String str){
        try {
            JSONArray jsonArr = new JSONArray(str);
            for(int i=0;i<jsonArr.length();i++){
                JSONObject e = jsonArr.getJSONObject(i);
                words.put(e.getString("text"),e.getString("imageSource"));
            }
        } catch (final JSONException e) {
            System.out.println("Json parsing error: " + e.getMessage());
        }
    }
}